import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



public class Difference {
	public static class DifferenceMap extends Mapper<LongWritable, Text, RelationA, IntWritable>{
		String base;
		IntWritable one = new IntWritable(1);
		IntWritable two = new IntWritable(2);

		@Override
		protected void setup(Context context) throws IOException,InterruptedException{
			base = context.getConfiguration().get("base");
		}
		
		@Override
		public void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
			FileSplit fileSplit = (FileSplit) context.getInputSplit();
			String fileName = fileSplit.getPath().getName();
			RelationA record = new RelationA(line.toString());
			if (base.endsWith(fileName)) {
				context.write(record, one);
			}
			else {
				context.write(record, two);
			}
		}
	}
	
	public static class DifferenceReduce extends Reducer<RelationA, IntWritable, RelationA, NullWritable>{
		@Override
		public void reduce(RelationA key, Iterable<IntWritable> value, Context context) throws IOException,InterruptedException {
			int sum = 0;
			for (IntWritable val: value) {
				sum += val.get();
			}
			if (sum == 1) {
				context.write(key, NullWritable.get());
			}
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
		Job differenceJob = Job.getInstance();
		differenceJob.setJobName("DifferenceJob");
		differenceJob.setJarByClass(Difference.class);
		differenceJob.getConfiguration().set("base", args[2]);

		FileInputFormat.addInputPath(differenceJob, new Path(args[0]));
		
		differenceJob.setInputFormatClass(TextInputFormat.class);
		
		differenceJob.setMapperClass(DifferenceMap.class);
		differenceJob.setMapOutputKeyClass(RelationA.class);
		differenceJob.setMapOutputValueClass(IntWritable.class);

		differenceJob.setReducerClass(DifferenceReduce.class);
		differenceJob.setOutputKeyClass(RelationA.class);
		differenceJob.setOutputValueClass(NullWritable.class);

		differenceJob.setOutputFormatClass(TextOutputFormat.class);

		FileOutputFormat.setOutputPath(differenceJob, new Path(args[1]));
		
		differenceJob.waitForCompletion(true);
	}
}
