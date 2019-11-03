import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;


public final class ClusterGenerator {

    public static class generateMapper extends Mapper<LongWritable, Text, DescIntWritable, Point>
    {
        private int count = 0;
        private DescIntWritable marker = new DescIntWritable();
        private Point point = new Point();

        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            count++;
            marker.set(count);
            point.setByLine(line.toString());
            context.write(marker,point);
        }
    }

    public static class generateReducer extends Reducer<DescIntWritable, Point, Cluster, NullWritable>
    {
        private int count = 0;
        private int pointNum = 0;
        private int outputClusterNum = 0;
        private int interval = 0;
        private Cluster cluster = new Cluster();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            outputClusterNum = context.getConfiguration().getInt("clusterNum",0);
        }

        @Override
        protected void reduce(DescIntWritable index, Iterable<Point> points, Context context) throws IOException, InterruptedException {
            if (index.get() > pointNum) {
                pointNum = index.get();
            }
            interval = pointNum / outputClusterNum;
            if (count % interval == 0) {
                for (Point point: points) {
                    cluster.setClusterId((count / interval) + 1);
                    cluster.setCenter(point);
                    context.write(cluster, NullWritable.get());
                }
            }
            count++;
        }
    }
}
