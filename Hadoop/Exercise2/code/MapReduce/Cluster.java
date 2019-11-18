import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Cluster implements WritableComparable<Cluster>
{
    private int clusterId;
    private int pointNum;
    private Point center;

    public Cluster() {
        clusterId = -1;
        pointNum = 1;
        center = new Point();
    }

    public Cluster(int id, int num, Point point) {
        clusterId = id;
        pointNum = num;
        center = point;
    }

    public Cluster(String line) {
        String[] tuple = line.split(",");
        clusterId = Integer.parseInt(tuple[0]);
        pointNum = Integer.parseInt(tuple[1]);
        center = new Point(Double.parseDouble(tuple[2]), Double.parseDouble(tuple[3]));
    }

    public void setByLine(String line) {
        String[] tuple = line.split(",");
        clusterId = Integer.parseInt(tuple[0]);
        pointNum = Integer.parseInt(tuple[1]);
        center = new Point(Double.parseDouble(tuple[2]), Double.parseDouble(tuple[3]));
    }

    public void setClusterId(int id) {
        clusterId = id;
    }

    public void setPointNum(int num) {
        pointNum = num;
    }

    public void setCenter(Point point) {
        center = point;
    }

    public int getClusterId() {
        return clusterId;
    }

    public int getPointNum() {
        return pointNum;
    }

    public Point getCenter() {
        return center;
    }

    public String toString() {
        String string = String.valueOf(clusterId) + "," + String.valueOf(pointNum) + "," + center.toString();
        return string;
    }

    public void readFields(DataInput in) throws IOException {
        clusterId = in.readInt();
        pointNum = in.readInt();
        center.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(clusterId);
        out.writeInt(pointNum);
        center.write(out);
    }

    public int compareTo(Cluster other) {
        if (clusterId > other.getClusterId()) {
            return 1;
        }
        else if (clusterId < other.getClusterId()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}