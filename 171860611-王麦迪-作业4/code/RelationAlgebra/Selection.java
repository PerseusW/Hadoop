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

public class Selection 
{
	public static class SelectionMap extends Mapper<LongWritable, Text, RelationA, NullWritable> {
		private int selectedCol;
		private String value;
		
		@Override
		protected void setup(Context context) throws IOException,InterruptedException {
			selectedCol = context.getConfiguration().getInt("selectedCol", 0);
			value = context.getConfiguration().get("value");
		}
		
		@Override
		public void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
			RelationA record = new RelationA(line.toString());
			if (value.startsWith("s")) {
				if (record.columnSmallerThan(selectedCol, value.substring(1))) {
					context.write(record, NullWritable.get());
				}
			}
			else if (value.startsWith("g")) {
				if (record.columnGreaterThan(selectedCol, value.substring(1))) {
					context.write(record, NullWritable.get());
				}
			}
			else if(record.columnEquals(selectedCol, value)) {
				context.write(record, NullWritable.get());
			}
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
        if (args.length != 4) {
            System.err.println("Usage: Select <input path> <output path> <selectedCol> <value>");
            System.exit(2);
		}
		
		Configuration configuration = new Configuration();
		configuration.setInt("selectedCol", Integer.parseInt(args[2]));
		configuration.set("value", args[3]);

		Job selectionJob = Job.getInstance(configuration, "SelectionJob");
		selectionJob.setJarByClass(Selection.class);
		
		selectionJob.setMapperClass(SelectionMap.class);
		selectionJob.setMapOutputKeyClass(RelationA.class);
		selectionJob.setMapOutputValueClass(NullWritable.class);

		selectionJob.setNumReduceTasks(0);

		FileInputFormat.addInputPath(selectionJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(selectionJob, new Path(args[1]));
		
		selectionJob.waitForCompletion(true);
	}
}