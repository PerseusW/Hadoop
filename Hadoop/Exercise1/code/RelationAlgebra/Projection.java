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


public class Projection {
	public static class ProjectionMap extends Mapper<LongWritable, Text, Text, NullWritable>{
		private int col;
		@Override
		protected void setup(Context context) throws IOException,InterruptedException{
			col = context.getConfiguration().getInt("col", 0);
		}
		
		@Override
		public void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException{
			RelationA record = new RelationA(line.toString());
			context.write(new Text(record.getColumn(col)), NullWritable.get());
		}	
	}
	
	public static class ProjectionReduce extends Reducer<Text, NullWritable, Text, NullWritable>{
		@Override
		public void reduce(Text key, Iterable<NullWritable> value, Context context) throws IOException,InterruptedException{
			context.write(key, NullWritable.get());
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
		if (args.length != 3) {
			System.err.println("Usage: Projection <input file> <output directory> <column id>");
			System.exit(2);
		}


		Job projectionJob = Job.getInstance();
		projectionJob.setJobName("ProjectionJob");
		projectionJob.setJarByClass(Projection.class);
		projectionJob.getConfiguration().setInt("col", Integer.parseInt(args[2]));
		
		projectionJob.setMapperClass(ProjectionMap.class);
		projectionJob.setMapOutputKeyClass(Text.class);
		projectionJob.setMapOutputValueClass(NullWritable.class);

		projectionJob.setReducerClass(ProjectionReduce.class);
		projectionJob.setOutputKeyClass(Text.class);
		projectionJob.setOutputValueClass(NullWritable.class);

		FileInputFormat.addInputPath(projectionJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(projectionJob, new Path(args[1]));
		
		projectionJob.waitForCompletion(true);
	}
}
