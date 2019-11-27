import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class PopularityStatistician
{
    public static class AnyActionStatistician extends Mapper<LongWritable, Text, ItemRegionPopularity, IntWritable>
    {
        private UserLog userLog = new UserLog();
        private ItemRegionPopularity itemRegionPopularity = new ItemRegionPopularity();
        private IntWritable one = new IntWritable(1);

        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            userLog.setByLine(line.toString());
            itemRegionPopularity.setProvince(userLog.getProvince());
            itemRegionPopularity.setItemID(userLog.getItemID());
            if (userLog.getAction() != -1) {
                context.write(itemRegionPopularity, one);
            }
        }
    }

    public static class BuyActionStatistician extends Mapper<LongWritable, Text, ItemRegionPopularity, IntWritable>
    {
        private UserLog userLog = new UserLog();
        private ItemRegionPopularity itemRegionPopularity = new ItemRegionPopularity();
        private IntWritable one = new IntWritable(1);

        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            userLog.setByLine(line.toString());
            itemRegionPopularity.setProvince(userLog.getProvince());
            itemRegionPopularity.setItemID(userLog.getItemID());
            if (userLog.getAction() == 2) {
                context.write(itemRegionPopularity, one);
            }
        }
    }

    public static class ActionCombiner extends Reducer<ItemRegionPopularity, IntWritable, ItemRegionPopularity, IntWritable>
    {
        private IntWritable actionCount = new IntWritable(-1);
        @Override
        protected void reduce(ItemRegionPopularity itemRegionPopularity, Iterable<IntWritable> localCount, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable singleCount: localCount) {
                count += singleCount.get();
            }
            actionCount.set(count);
            context.write(itemRegionPopularity, actionCount);
        }
    }

    public static class SortPreparation extends Mapper<LongWritable, Text, ItemRegionPopularity, NullWritable>
    {
        private ItemRegionPopularity itemRegionPopularity = new ItemRegionPopularity();

        @Override
        protected void map(LongWritable offSet, Text line, Context context) throws IOException, InterruptedException {
            String[] tuple = line.toString().split("\t");
            itemRegionPopularity.setByLine(tuple[0]);
            itemRegionPopularity.setPopularity(Integer.parseInt(tuple[1]));
            context.write(itemRegionPopularity, NullWritable.get());
        }
    }

    public static class TopTenFilter extends Reducer<ItemRegionPopularity, NullWritable, ItemRegionPopularity, NullWritable>
    {
        private String current = "";
        private int topTenCount = 0;
        @Override
        protected void reduce(ItemRegionPopularity tuple, Iterable<NullWritable> nothing, Context context) throws IOException, InterruptedException {
            if (topTenCount < 10) {
                topTenCount++;
                current = tuple.getProvince();
                context.write(tuple, NullWritable.get());
            }
            else if (current.compareTo(tuple.getProvince()) != 0) {
                topTenCount = 1;
                current = tuple.getProvince();
                context.write(tuple, NullWritable.get());
            }
        }
    }
}