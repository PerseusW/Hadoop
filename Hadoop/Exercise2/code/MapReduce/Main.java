import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;

public class Main
{

    private static void generateInitialCluster(Configuration configuration) throws Exception {
        Job generateInitialClusterJob = Job.getInstance(configuration, "Generate Initial Cluster");
        generateInitialClusterJob.setJarByClass(Converter.class);
        generateInitialClusterJob.setMapperClass(Converter.PointsToCountedPoints.class);
        generateInitialClusterJob.setMapOutputKeyClass(DescIntWritable.class);
        generateInitialClusterJob.setMapOutputValueClass(Point.class);
        generateInitialClusterJob.setReducerClass(Converter.CountedPointsToInitialClusters.class);
        generateInitialClusterJob.setOutputKeyClass(Cluster.class);
        generateInitialClusterJob.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(generateInitialClusterJob, new Path(configuration.get("inputPath")));
        FileOutputFormat.setOutputPath(generateInitialClusterJob, new Path(configuration.get("outputPath") + "/0-C"));
        generateInitialClusterJob.waitForCompletion(true);
    }

    private static void IterateCluster(Configuration configuration) throws Exception {
        int iterationNum = configuration.getInt("iterationNum", 0);
        for (int i = 0; i < iterationNum; i++) {
            configuration.set("clusterPath", configuration.get("outputPath") + "/" + String.valueOf(i) + "-C");
            Job categorizePointsJob = Job.getInstance(configuration, "Categorize Points");
            categorizePointsJob.setJarByClass(Converter.class);
            categorizePointsJob.setMapperClass(Converter.PointsToPointClusters.class);
            categorizePointsJob.setMapOutputKeyClass(IntWritable.class);
            categorizePointsJob.setMapOutputValueClass(Cluster.class);
            FileInputFormat.addInputPath(categorizePointsJob, new Path(configuration.get("inputPath")));
            FileOutputFormat.setOutputPath(categorizePointsJob, new Path(configuration.get("outputPath") + "/" + String.valueOf(i + 1) + "-P"));
            categorizePointsJob.waitForCompletion(true);

            Job mergePointsJob = Job.getInstance(configuration,"Merge Points");
            mergePointsJob.setJarByClass(Converter.class);
            mergePointsJob.setMapperClass(Converter.KeyValueToPointClusters.class);
            mergePointsJob.setMapOutputKeyClass(IntWritable.class);
            mergePointsJob.setMapOutputValueClass(Cluster.class);
            mergePointsJob.setCombinerClass(Converter.PointClustersToRegionClusters.class);
            mergePointsJob.setReducerClass(Converter.RegionClustersToGlobalClusters.class);
            mergePointsJob.setOutputKeyClass(Cluster.class);
            mergePointsJob.setOutputValueClass(NullWritable.class);
            FileInputFormat.addInputPath(mergePointsJob, new Path(configuration.get("outputPath") + "/" + String.valueOf(i + 1) + "-P"));
            FileOutputFormat.setOutputPath(mergePointsJob, new Path(configuration.get("outputPath") + "/" + String.valueOf(i + 1) + "-C"));
            mergePointsJob.waitForCompletion(true);
        }

    }

    public static void main (String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Usage: Main <Input path> <Output path> <Number of clusters> <Number of iterations>");
            System.exit(-1);
        }
        
        Configuration configuration = new Configuration();
        configuration.set("inputPath", args[0]);
        configuration.set("outputPath", args[1]);
        configuration.setInt("clusterNum", Integer.parseInt(args[2]));
        configuration.setInt("iterationNum", Integer.parseInt(args[3]));

        Main.generateInitialCluster(configuration);
        Main.IterateCluster(configuration);
    }
}