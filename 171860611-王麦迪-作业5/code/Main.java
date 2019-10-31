import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Main
{
    public static void main (String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Usage: Main <Input path> <Output path> <Number of clusters> <Number of iterations>");
            System.exit(-1);
        }
        
        Job job = Job.getInstance();
        job.setJobName("KMeans Job");
        job.setJarByClass(KMeans.class);

        job.setMapperClass(KMeans.KMeansMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Point.class);

        job.setReducerClass(KMeans.KMeansCombiner.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}