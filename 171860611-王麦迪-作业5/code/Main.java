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
        Job initialJob = Job.getInstance(configuration, "Generate Initial Clusters");
        initialJob.setJarByClass(ClusterGenerator.class);

        initialJob.setMapperClass(ClusterGenerator.generateMapper.class);
        initialJob.setMapOutputKeyClass(DescIntWritable.class);
        initialJob.setMapOutputValueClass(Point.class);

        initialJob.setReducerClass(ClusterGenerator.generateReducer.class);
        initialJob.setOutputKeyClass(Cluster.class);
        initialJob.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(initialJob, new Path(configuration.get("inputPath")));
        FileOutputFormat.setOutputPath(initialJob, new Path(configuration.get("outputPath") + "/0-ClusterInfo"));
        
        initialJob.waitForCompletion(true);
    }

    private static void IterateCluster(Configuration configuration) throws Exception {
        int iterationNum = configuration.getInt("iterationNum", 0);
        for (int i = 0; i < iterationNum; i++) {
            configuration.set("clusterPath", configuration.get("outputPath") + "/" + String.valueOf(i) + "-ClusterInfo");
        
            Job clusterJob = Job.getInstance(configuration,"Interation");
            clusterJob.setJarByClass(ClusterIterator.class);

            clusterJob.setMapperClass(ClusterIterator.ClusterMapper.class);
            clusterJob.setMapOutputKeyClass(IntWritable.class);
            clusterJob.setMapOutputValueClass(Cluster.class);
            clusterJob.setCombinerClass(ClusterIterator.PointCombiner.class);

            clusterJob.setReducerClass(ClusterIterator.ClusterCombiner.class);
            clusterJob.setOutputKeyClass(Cluster.class);
            clusterJob.setOutputValueClass(NullWritable.class);
        
            FileInputFormat.addInputPath(clusterJob, new Path(configuration.get("inputPath")));
            FileOutputFormat.setOutputPath(clusterJob, new Path(configuration.get("outputPath") + "/" + String.valueOf(i + 1) + "-ClusterInfo"));

            clusterJob.waitForCompletion(true);
        }
        for (int i = 0; i < iterationNum; i++) {
            configuration.set("clusterPath", configuration.get("outputPath") + "/" + String.valueOf(i) + "-ClusterInfo");
        
            Job clusterJob = Job.getInstance(configuration,"Interation");
            clusterJob.setJarByClass(ClusterIterator.class);

            clusterJob.setMapperClass(ClusterIterator.ClusterMapper.class);
            clusterJob.setMapOutputKeyClass(IntWritable.class);
            clusterJob.setMapOutputValueClass(Cluster.class);
        
            FileInputFormat.addInputPath(clusterJob, new Path(configuration.get("inputPath")));
            FileOutputFormat.setOutputPath(clusterJob, new Path(configuration.get("outputPath") + "/" + String.valueOf(i + 1) + "-ClusterPoints"));

            clusterJob.waitForCompletion(true);
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