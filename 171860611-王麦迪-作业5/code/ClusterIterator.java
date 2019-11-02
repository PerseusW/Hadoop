import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class ClusterIterator
{
    public static class ClusterMapper extends Mapper<LongWritable, Text, IntWritable, Cluster>
    {
        private ArrayList<Cluster> clusters = new ArrayList<>();
        private Path clusterPath;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            FileSystem fileSystem = FileSystem.get(context.getConfiguration());
            clusterPath = new Path(context.getConfiguration().get("clusterPath"));
			String line = new String();
	    	FSDataInputStream inputStream = fileSystem.open(clusterPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			while((line = reader.readLine()) != null){
				Cluster cluster = new Cluster(line);
				clusters.add(cluster);
			}
	        reader.close();
	        inputStream.close();
        }

        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            Point point = new Point(line.toString());
            int closestClusterId = Integer.MAX_VALUE;
            double closetClusterDistance = Double.MAX_VALUE;
            for (int i = 0; i < clusters.size(); i++) {
                Point center = clusters.get(i).getCenter();
                double distance = center.distanceFrom(point);
                if (closetClusterDistance > distance) {
                    closetClusterDistance = distance;
                    closestClusterId = i + 1;
                }
            }
            context.write(new IntWritable(closestClusterId), new Cluster(-1,1,point));
        }
    }

    public static class PointCombiner extends Reducer<IntWritable, Cluster, IntWritable, Cluster>
    {
        @Override
        protected void reduce(IntWritable clusterId, Iterable<Cluster> clusters, Context context) throws IOException, InterruptedException {
            int clusterNum = 0;
            int xSum = 0;
            int ySum = 0;
            for (Cluster cluster: clusters) {
                clusterNum++;
                xSum += cluster.getCenter().getX();
                ySum += cluster.getCenter().getY();
            }
            Cluster cluster = new Cluster(clusterId.get(), clusterNum, new Point(xSum / clusterNum, ySum / clusterNum));
            context.write(clusterId, cluster);
        }
    }

    public static class ClusterCombiner extends Reducer<IntWritable, Cluster, Cluster, NullWritable>
    {
        @Override
        protected void reduce(IntWritable clusterId, Iterable<Cluster> clusters, Context context) throws IOException, InterruptedException {
            for (Cluster cluster: clusters) {
                context.write(cluster, NullWritable.get());
            }
        }
    }
}