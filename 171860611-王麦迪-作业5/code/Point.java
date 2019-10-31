import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class Point implements Writable
{
    int x;
    int y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(String line) {
        String[] tuple = line.split(",");
        x = Integer.parseInt(tuple[0]);
        y = Integer.parseInt(tuple[1]);
    }

    public String toString() {
        return String.valueOf(x) + "," + String.valueOf(y);
    }

    public void readFields(DataInput in) throws IOException {
        x = in.readInt();
        y = in.readInt();
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
    }
}