import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MatrixMultiply {
  public static int MRow = 0;
  public static int MCol = 0;
  public static int NCol = 0;

  public static class MatrixMapper extends Mapper<Object, Text, Text, Text> {
    private Text resultMatrixPos = new Text();
    private Text initMatrixVal = new Text();

    public void setup(Context context) throws IOException {
      Configuration conf = context.getConfiguration();
      NCol = Integer.parseInt(conf.get("NCol"));
      MRow = Integer.parseInt(conf.get("MRow"));
    }

    public void map(Object key, Text line, Context context) throws IOException, InterruptedException {
      FileSplit fileSplit = (FileSplit) context.getInputSplit();
      String fileName = fileSplit.getPath().getName();
      if (fileName.contains("M")) {
        String[] keyValueTuple = line.toString().split("\t");
        String[] keyTuple = keyValueTuple[0].split(",");
        int i = Integer.parseInt(keyTuple[0]);
        int j = Integer.parseInt(keyTuple[1]);
        int Mij = Integer.parseInt(keyValueTuple[1]);
        for (int k = 1; k < NCol + 1; k++) {
          resultMatrixPos.set(i + "," + k);
          initMatrixVal.set("M" + "," + j + "," + Mij);
          context.write(resultMatrixPos, initMatrixVal);
        }
      }
      else if (fileName.contains("N")) {
        String[] keyValueTuple = line.toString().split("\t");
        String[] keyTuple = keyValueTuple[0].split(",");
        int j = Integer.parseInt(keyTuple[0]);
        int k = Integer.parseInt(keyTuple[1]);
        int Njk = Integer.parseInt(keyValueTuple[1]);
        for (int i = 1; i < MRow + 1; i++) {
          resultMatrixPos.set(i + "," + k);
          initMatrixVal.set("N" + "," + j + "," + Njk);
          context.write(resultMatrixPos, initMatrixVal);
        }
      }
    }
  }

  public static class MatrixReducer extends Reducer<Text, Text, Text, Text> {

    public void setup(Context context) throws IOException {
      Configuration conf = context.getConfiguration();
      MCol = Integer.parseInt(conf.get("MCol"));
    }

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      int[] M = new int[MCol + 1];
      int[] N = new int[MCol + 1];
      for (Text val : values) {
        String[] tuple = val.toString().split(",");
        if (tuple[0].equals("M")) {
          M[Integer.parseInt(tuple[1])] = Integer.parseInt(tuple[2]);
        }
        else {
          N[Integer.parseInt(tuple[1])] = Integer.parseInt(tuple[2]);
        }
          
      }
      for (int j = 1; j < MCol + 1; j++) {
        sum += M[j] * N[j];
      }
      context.write(key, new Text(Integer.toString(sum)));
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      System.err.println("Usage: MatrixMultiply <M inputPath> <N inputPath> <outputPath>");
      System.exit(2);
    }
    String[] MParameters = args[0].split("_");
    MRow = Integer.parseInt(MParameters[1]);
    MCol = Integer.parseInt(MParameters[2]);
    String[] NParameters = args[1].split("_");
    int NRow = Integer.parseInt(NParameters[1]);
    NCol = Integer.parseInt(NParameters[2]);
    if (NRow != MCol) {
      System.err.println("Error: Matrix M<" + MRow + ", " + MCol + "> and N<" + NRow + ", " + NCol + "> cannot multiply");
      System.exit(2);
    }

    Configuration conf = new Configuration();
    conf.setInt("MRow", MRow);
    conf.setInt("MCol", MCol);
    conf.setInt("NCol", NCol);

    Job job = new Job(conf, "MatrixMultiply");
    job.setJarByClass(MatrixMultiply.class);
    job.setMapperClass(MatrixMapper.class);
    job.setReducerClass(MatrixReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.setInputPaths(job, new Path(args[0]), new Path(args[1]));
    FileOutputFormat.setOutputPath(job, new Path(args[2]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}