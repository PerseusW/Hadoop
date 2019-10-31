import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class KMeans
{
    public static class KMeansMapper extends Mapper<LongWritable, Text, LongWritable, Point>
    {
        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            Point point = new Point(line.toString());
            context.write(offSet,point);
        }
    }

    public static class KMeansCombiner extends Reducer<LongWritable, Point, LongWritable, Text>
    {
        @Override
        protected void reduce(LongWritable offSet, Iterable<Point> points, Context context) throws IOException, InterruptedException {
            for (Point point: points) {
                context.write(offSet, new Text(point.toString()));
            }
        }
    }
}