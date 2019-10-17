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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Union {
	public static class UnionMap extends Mapper<LongWritable, Text, RelationA, IntWritable>{

		@Override
		public void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException{
			RelationA record = new RelationA(line.toString());
			context.write(record, new IntWritable(1));
		}
	}

	public static class UnionReduce extends Reducer<RelationA, IntWritable, RelationA, NullWritable>
	{
		@Override
		public void reduce(RelationA key, Iterable<IntWritable> value, Context context) throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
		Job unionJob = Job.getInstance();
		unionJob.setJobName("unionJob");
		unionJob.setJarByClass(Union.class);
	
		unionJob.setMapperClass(UnionMap.class);
		unionJob.setMapOutputKeyClass(RelationA.class);
		unionJob.setMapOutputValueClass(IntWritable.class);

		unionJob.setReducerClass(UnionReduce.class);
		unionJob.setOutputKeyClass(RelationA.class);
		unionJob.setOutputValueClass(NullWritable.class);

		unionJob.setInputFormatClass(TextInputFormat.class);
		unionJob.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(unionJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(unionJob, new Path(args[1]));
		
		unionJob.waitForCompletion(true);
	}
}
