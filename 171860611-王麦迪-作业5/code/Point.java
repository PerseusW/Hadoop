import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class Point implements WritableComparable<Point>
{
    private int x;
    private int y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(String line) {
        String[] tuple = line.split(",");
        x = Integer.parseInt(tuple[0]);
        y = Integer.parseInt(tuple[1]);
    }

    public void setByLine(String line) {
        String[] tuple = line.split(",");
        x = Integer.parseInt(tuple[0]);
        y = Integer.parseInt(tuple[1]);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return String.valueOf(x) + "," + String.valueOf(y);
    }

    public double distanceFrom(Point point) {
        int sum = 0;
        sum += (x - point.getX()) * (x - point.getX());
        sum += (y - point.getY()) * (y - point.getY());
        return Math.sqrt(sum);
    }

    public void readFields(DataInput in) throws IOException {
        x = in.readInt();
        y = in.readInt();
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
    }

    public int compareTo(Point point) {
        if (x > point.getX()) {
            return 1;
        }
        else if (x < point.getX()) {
            return -1;
        }
        else if (y > point.getY()) {
            return 1;
        }
        else if (y < point.getY()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}