import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Selection {
	public static class SelectionMap extends Mapper<LongWritable, Text, RelationA, NullWritable>{
		private int col;
		private String value;
		
		@Override
		protected void setup(Context context) throws IOException,InterruptedException{
			col = context.getConfiguration().getInt("col", 0);
			value = context.getConfiguration().get("value");
		}
		
		@Override
		public void map(LongWritable offSet, Text line, Context context)throws IOException, InterruptedException{
			RelationA record = new RelationA(line.toString());
			if (value.startsWith("s")) {
				if (record.smallerThanCondition(col, value.substring(1))) {
					context.write(record, NullWritable.get());
				}
			}
			else if(record.isCondition(col, value))
				context.write(record, NullWritable.get());
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
        if (args.length != 4) {
            System.err.println("Usage: Select <input path> <output path> <col> <value>");
            System.exit(2);
		}
		
		Configuration configuration = new Configuration();
		configuration.setInt("col", Integer.parseInt(args[2]));
		configuration.set("value", args[3]);

		Job selectionJob = new Job(configuration, "SelectionJob");
		selectionJob.setJarByClass(Selection.class);
		
		selectionJob.setMapperClass(SelectionMap.class);
		selectionJob.setMapOutputKeyClass(RelationA.class);
		selectionJob.setMapOutputValueClass(NullWritable.class);

		selectionJob.setNumReduceTasks(0);

		FileInputFormat.addInputPath(selectionJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(selectionJob, new Path(args[1]));
		
		selectionJob.waitForCompletion(true);
		System.out.println("Job finished");
	}
}