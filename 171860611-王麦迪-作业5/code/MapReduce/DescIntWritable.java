import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class DescIntWritable implements WritableComparable<DescIntWritable> {
        private int value;

        public DescIntWritable() {
            value = 0;
        }

        public DescIntWritable(int value) {
            this.value = value;
        }

        public void set(int value) {
            this.value = value;
        }

        public int get() {
            return value;
        }

        public String toString() {
            return String.valueOf(value);
        }

        public void write(DataOutput out) throws IOException {
            out.writeInt(value);
        }

        public void readFields(DataInput in) throws IOException {
            value = in.readInt();
        }

        public int compareTo(DescIntWritable o) {
            if (value > o.get()) {
                return -1;
            }
            else if (value < o.get()) {
                return 1;
            }
            else {
                return 0;
            }
        }
}