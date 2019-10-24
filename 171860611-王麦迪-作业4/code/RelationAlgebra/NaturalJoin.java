import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class NaturalJoin {
	public static class NaturalJoinMap extends Mapper<LongWritable, Text, IntWritable, Text>{

		@Override
		public void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
			String[] tuple = line.toString().split(",");
			context.write(new IntWritable(Integer.parseInt(tuple[0])), line);
		}
	}
	
	public static class NaturalJoinReduce extends Reducer<IntWritable,Text,Text,NullWritable>{
		@Override
		public void reduce(IntWritable key, Iterable<Text> value, Context context) throws IOException,InterruptedException {
			String[] aFragments = null;
			String[] bFragments = null;
			for (Text val: value) {
				String[] fragments = val.toString().split(",");
				if (fragments.length == 4) {
					aFragments = fragments;
				}
				else if (fragments.length == 3) {
					bFragments = fragments;
				}
				else {
					System.err.println("Invalid Input");
					System.exit(2);
				}
			}
			String joinedRelation = aFragments[0] + "," + aFragments[1] + "," + aFragments[2] + "," + bFragments[1] + "," + aFragments[3] + "," + bFragments[2];
			context.write(new Text(joinedRelation), NullWritable.get());
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
		if (args.length != 3) {
			System.err.println("Usage: NaturalJoin <input file a> <input file b> <output directory>");
			System.exit(2);
		}
		
		Job naturalJoinJob = Job.getInstance();
		naturalJoinJob.setJobName("NaturalJoinJob");
		naturalJoinJob.setJarByClass(NaturalJoin.class);
		
		naturalJoinJob.setMapperClass(NaturalJoinMap.class);
		naturalJoinJob.setMapOutputKeyClass(IntWritable.class);
		naturalJoinJob.setMapOutputValueClass(Text.class);

		naturalJoinJob.setReducerClass(NaturalJoinReduce.class);
		naturalJoinJob.setOutputKeyClass(Text.class);
		naturalJoinJob.setOutputValueClass(NullWritable.class);

		FileInputFormat.addInputPath(naturalJoinJob, new Path(args[0]));
		FileInputFormat.addInputPath(naturalJoinJob, new Path(args[1]));
		FileOutputFormat.setOutputPath(naturalJoinJob, new Path(args[2]));
		
		naturalJoinJob.waitForCompletion(true);
	}
}
