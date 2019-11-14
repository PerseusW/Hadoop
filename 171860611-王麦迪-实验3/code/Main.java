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
    private static String anyActionMiddlePath = "/AnyAction/AggregatedResults";
    private static String anyActionFinalPath = "/AnyAction/FilteredResults";
    private static String buyActionMiddlePath = "/BuyAction/AggregatedResults";
    private static String buyActionFinalPath = "/BuyAction/FilteredResults";

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: Main <Input path> <Output path>");
            System.exit(-1);
        }

        Configuration configuration = new Configuration();
        configuration.set("inputPath", args[0]);
        configuration.set("outputPath", args[1]);

        Main.calculatePopularity(configuration);
        Main.filterPopularity(configuration);
        Main.calculateBought(configuration);
        Main.filterBought(configuration);
    }

    private static void calculatePopularity(Configuration configuration) throws Exception {
        Job calculatePopularityJob = Job.getInstance(configuration, "Calculate Popularity");
        calculatePopularityJob.setJarByClass(PopularityStatistician.class);
        calculatePopularityJob.setMapperClass(PopularityStatistician.AnyActionStatistician.class);
        calculatePopularityJob.setCombinerClass(PopularityStatistician.ActionCombiner.class);
        calculatePopularityJob.setMapOutputKeyClass(ItemRegionPopularity.class);
        calculatePopularityJob.setMapOutputValueClass(IntWritable.class);
        calculatePopularityJob.setReducerClass(PopularityStatistician.ActionCombiner.class);
        calculatePopularityJob.setOutputKeyClass(ItemRegionPopularity.class);
        calculatePopularityJob.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(calculatePopularityJob, new Path(configuration.get("inputPath")));
        FileOutputFormat.setOutputPath(calculatePopularityJob, new Path(configuration.get("outputPath") + anyActionMiddlePath));
        calculatePopularityJob.waitForCompletion(true);
    }

    private static void filterPopularity(Configuration configuration) throws Exception {
        Job filterPopularityJob = Job.getInstance(configuration, "Filter Popularity");
        filterPopularityJob.setJarByClass(PopularityStatistician.class);
        filterPopularityJob.setMapperClass(PopularityStatistician.SortPreparation.class);
        filterPopularityJob.setMapOutputKeyClass(ItemRegionPopularity.class);
        filterPopularityJob.setMapOutputValueClass(NullWritable.class);
        filterPopularityJob.setReducerClass(PopularityStatistician.TopTenFilter.class);
        filterPopularityJob.setOutputKeyClass(ItemRegionPopularity.class);
        filterPopularityJob.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(filterPopularityJob, new Path(configuration.get("outputPath") + anyActionMiddlePath));
        FileOutputFormat.setOutputPath(filterPopularityJob, new Path(configuration.get("outputPath") + anyActionFinalPath));
        filterPopularityJob.waitForCompletion(true);
    }

    public static void calculateBought(Configuration configuration) throws Exception {
        Job calculateBoughtJob = Job.getInstance(configuration, "Calculate Bought");
        calculateBoughtJob.setJarByClass(PopularityStatistician.class);
        calculateBoughtJob.setMapperClass(PopularityStatistician.BuyActionStatistician.class);
        calculateBoughtJob.setCombinerClass(PopularityStatistician.ActionCombiner.class);
        calculateBoughtJob.setMapOutputKeyClass(ItemRegionPopularity.class);
        calculateBoughtJob.setMapOutputValueClass(IntWritable.class);
        calculateBoughtJob.setReducerClass(PopularityStatistician.ActionCombiner.class);
        calculateBoughtJob.setOutputKeyClass(ItemRegionPopularity.class);
        calculateBoughtJob.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(calculateBoughtJob, new Path(configuration.get("inputPath")));
        FileOutputFormat.setOutputPath(calculateBoughtJob, new Path(configuration.get("outputPath") + buyActionMiddlePath));
        calculateBoughtJob.waitForCompletion(true);
    }

    private static void filterBought(Configuration configuration) throws Exception {
        Job filterBoughtJob = Job.getInstance(configuration, "Filter Bought");
        filterBoughtJob.setJarByClass(PopularityStatistician.class);
        filterBoughtJob.setMapperClass(PopularityStatistician.SortPreparation.class);
        filterBoughtJob.setMapOutputKeyClass(ItemRegionPopularity.class);
        filterBoughtJob.setMapOutputValueClass(NullWritable.class);
        filterBoughtJob.setReducerClass(PopularityStatistician.TopTenFilter.class);
        filterBoughtJob.setOutputKeyClass(ItemRegionPopularity.class);
        filterBoughtJob.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(filterBoughtJob, new Path(configuration.get("outputPath") + buyActionMiddlePath));
        FileOutputFormat.setOutputPath(filterBoughtJob, new Path(configuration.get("outputPath") + buyActionFinalPath));
        filterBoughtJob.waitForCompletion(true);
    }
}