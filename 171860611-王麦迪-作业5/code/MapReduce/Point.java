import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class Point implements WritableComparable<Point>
{
    private double x;
    private double y;

    public Point() {
        x = 0.0;
        y = 0.0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(String line) {
        String[] tuple = line.split(",");
        x = Double.parseDouble(tuple[0]);
        y = Double.parseDouble(tuple[1]);
    }

    public void setByLine(String line) {
        String[] tuple = line.split(",");
        x = Double.parseDouble(tuple[0]);
        y = Double.parseDouble(tuple[1]);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString() {
        return String.valueOf(x) + "," + String.valueOf(y);
    }

    public double distanceFrom(Point point) {
        double sum = 0.0;
        sum += (x - point.getX()) * (x - point.getX());
        sum += (y - point.getY()) * (y - point.getY());
        return Math.sqrt(sum);
    }

    public void readFields(DataInput in) throws IOException {
        x = in.readDouble();
        y = in.readDouble();
    }

    public void write(DataOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
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