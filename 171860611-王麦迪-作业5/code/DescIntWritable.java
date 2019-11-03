import org.apache.hadoop.io.IntWritable;

public class DescIntWritable extends IntWritable
    {
        @Override
        public int compareTo(IntWritable integer) {
            return -super.compareTo(integer);
        }
    }