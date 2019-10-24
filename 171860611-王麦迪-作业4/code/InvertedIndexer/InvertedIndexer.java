import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.StringBuilder;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndexer {

  public static class InvertedIndexMapper extends Mapper<LongWritable, Text, Term, NullWritable> {
    @Override
    protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
      FileSplit fileSplit = (FileSplit) context.getInputSplit();
      String fileName = fileSplit.getPath().getName();
      StringTokenizer token = new StringTokenizer(line.toString());
      for (;token.hasMoreTokens();) {
        Term termCount = new Term();
        termCount.setTerm(token.nextToken());
        termCount.setCount(1);
        termCount.setDocument(fileName);
        context.write(termCount, NullWritable.get());
      }
    }
  }

  public static class InvertedIndexCombiner extends Reducer<Term, NullWritable, Term, NullWritable> {
    @Override
    protected void reduce(Term key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
      int count = 0;
      for (NullWritable value: values) {
        count ++;
      }
      key.setCount(count);
      context.write(key, NullWritable.get());
    }
  }

  public static class InvertedIndexReducer extends Reducer<Term, NullWritable, Text, Text> {
    private String previousTerm = new String();
    StringBuilder documents = new StringBuilder();
    @Override
    protected void reduce(Term key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
      if (previousTerm.isEmpty()) {
        previousTerm = key.getTerm();
        documents.append(key.getCount() + "@" + key.getDocument() + ";");
      }
      else if (previousTerm.equals(key.getTerm())) {
        documents.append(key.getCount() + "@" + key.getDocument() + ";");
      }
      else {
        context.write(new Text(previousTerm), new Text(documents.toString()));
        previousTerm = key.getTerm();
        documents = new StringBuilder();
        documents.append(key.getCount() + "@" + key.getDocument() + ";");
      }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
      context.write(new Text(previousTerm), new Text(documents.toString()));
      super.cleanup(context);
    }
  }

  public static void main(String[] args) throws Exception {
    Job job = Job.getInstance();
    job.setJobName("InvertedIndexer");
    job.setJarByClass(InvertedIndexer.class);
    FileInputFormat.setInputPaths(job, new Path(args[0]));
    job.setMapperClass(InvertedIndexMapper.class);
    job.setMapOutputKeyClass(Term.class);
    job.setMapOutputValueClass(NullWritable.class);
    job.setCombinerClass(InvertedIndexCombiner.class);
    job.setReducerClass(InvertedIndexReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.waitForCompletion(true);
  }
}