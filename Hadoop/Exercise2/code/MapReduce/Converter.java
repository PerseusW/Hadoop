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


public class Converter
{
    public static class PointsToCountedPoints extends Mapper<LongWritable, Text, DescIntWritable, Point>
    {
        private Point point = new Point();
        DescIntWritable counter = new DescIntWritable(0);

        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            point.setByLine(line.toString());
            counter.set(counter.get() + 1);
            context.write(counter,point);
        }
    }

    public static class CountedPointsToInitialClusters extends Reducer<DescIntWritable, Point, Cluster, NullWritable> {
        private int totalNum = 0;
        private int clusterNum = 0;
        private int selector = 0;
        private Cluster cluster = new Cluster();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            clusterNum = context.getConfiguration().getInt("clusterNum", 0);
        }

        @Override
        protected void reduce(DescIntWritable count, Iterable<Point> points, Context context) throws IOException, InterruptedException {
            int index = count.get();
            if (index > totalNum) {
                totalNum = index;
                selector = totalNum/clusterNum;
            }
            if (index % selector == 0) {
                for (Point point: points) {
                    cluster.setClusterId(index/selector);
                    cluster.setPointNum(1);
                    cluster.setCenter(point);
                    context.write(cluster, NullWritable.get());
                }
            }
        }
    }

    public static class PointsToPointClusters extends Mapper<LongWritable, Text, IntWritable, Cluster>
    {
        private ArrayList<Cluster> clusters = new ArrayList<>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            FileSystem fileSystem = FileSystem.get(context.getConfiguration());
            Path clusterPath = new Path(context.getConfiguration().get("clusterPath"));
            FileStatus[] fileList = fileSystem.listStatus(clusterPath);
            for (int i = 0; i < fileList.length; i++) {
                FSDataInputStream inputStream = fileSystem.open(fileList[i].getPath());
                BufferedReader lineReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = new String();
                while((line = lineReader.readLine()) != null){
                    Cluster cluster = new Cluster(line);
                    clusters.add(cluster);
                }
                lineReader.close();
                inputStream.close();
            }
        }

        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            Point point = new Point(line.toString());
            int closestClusterId = 0;
            double closetClusterDistance = Double.MAX_VALUE;
            for (int i = 0; i < clusters.size(); i++) {
                Point center = clusters.get(i).getCenter();
                double distance = center.distanceFrom(point);
                if (closetClusterDistance > distance) {
                    closetClusterDistance = distance;
                    closestClusterId = clusters.get(i).getClusterId();
                }
            }
            context.write(new IntWritable(closestClusterId), new Cluster(closestClusterId,1,point));
        }
    }

    public static class KeyValueToPointClusters extends Mapper<LongWritable, Text, IntWritable, Cluster>
    {
        private IntWritable clusterId = new IntWritable(0);
        private Cluster pointCluster = new Cluster();
        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            String[] tuple = line.toString().split("\t");
            clusterId.set(Integer.parseInt(tuple[0]));
            pointCluster.setByLine(tuple[1]);
            context.write(clusterId, pointCluster);
        }
    }

    public static class PointClustersToRegionClusters extends Reducer<IntWritable, Cluster, IntWritable, Cluster> {
        Point center = new Point();
        Cluster regionCluster = new Cluster();

        @Override
        protected void reduce(IntWritable clusterId, Iterable<Cluster> pointClusters, Context context) throws IOException, InterruptedException {
            int pointNum = 0;
            double xSum = 0;
            double ySum = 0;
            for (Cluster pointCluster: pointClusters) {
                pointNum += 1;
                xSum += pointCluster.getCenter().getX();
                ySum += pointCluster.getCenter().getY();
            }
            center.setX(xSum / pointNum);
            center.setY(ySum / pointNum);
            regionCluster.setClusterId(clusterId.get());
            regionCluster.setPointNum(pointNum);
            regionCluster.setCenter(center);
            context.write(clusterId, regionCluster);
        }
    }

    public static class RegionClustersToGlobalClusters extends Reducer<IntWritable, Cluster, Cluster, NullWritable>
    {
        Point center = new Point();
        Cluster globalCluster = new Cluster();

        @Override
        protected void reduce(IntWritable clusterId, Iterable<Cluster> regionClusters, Context context) throws IOException, InterruptedException {
            int pointNum = 0;
            double xSum = 0;
            double ySum = 0;
            for (Cluster regionCluster: regionClusters) {
                pointNum += regionCluster.getPointNum();
                center = regionCluster.getCenter();
                xSum += regionCluster.getPointNum() * center.getX();
                ySum += regionCluster.getPointNum() * center.getY();
            }
            center.setX(xSum / pointNum);
            center.setY(ySum / pointNum);
            globalCluster.setClusterId(clusterId.get());
            globalCluster.setPointNum(pointNum);
            globalCluster.setCenter(center);
            context.write(globalCluster, NullWritable.get());
        }
    }
}