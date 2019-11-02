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
    public static void main (String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Usage: Main <Input path> <Output path> <Number of clusters> <Number of iterations>");
            System.exit(-1);
        }
        
        Configuration configuration = new Configuration();
        configuration.setInt("clusterNum",Integer.parseInt(args[2]));

        ClusterGenerator clusterGenerator = new ClusterGenerator(configuration, args[0]);
        Path clusterPath = clusterGenerator.generateInitialCluster();
        configuration.set("clusterPath", clusterPath);
    }
}