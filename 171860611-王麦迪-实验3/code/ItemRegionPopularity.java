import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class ItemRegionPopularity implements WritableComparable<ItemRegionPopularity>
{
    private String province;
    private int itemID;
    private int popularity;

    public ItemRegionPopularity() {
        province = "-1";
        itemID = -1;
        popularity = -1;
    }

    public ItemRegionPopularity(String line) {
        String[] tuple = line.split(",");
        province = tuple[0];
        itemID = Integer.parseInt(tuple[1]);
        popularity = Integer.parseInt(tuple[2]);
    }

    public void setByLine(String line) {
        String[] tuple = line.split(",");
        province = tuple[0];
        itemID = Integer.parseInt(tuple[1]);
        popularity = Integer.parseInt(tuple[2]);
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getProvince() {
        return province;
    }

    public int getItemID() {
        return itemID;
    }

    public int getPopularity() {
        return popularity;
    }

    public String toString() {
        return province + "," + String.valueOf(itemID) + "," + String.valueOf(popularity);
    }

    public void readFields(DataInput in) throws IOException {
        province = in.readUTF();
        itemID = in.readInt();
        popularity = in.readInt();
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(province);
        out.writeInt(itemID);
        out.writeInt(popularity);
    }

    public int compareTo(ItemRegionPopularity other) {
        String otherProvince = other.getProvince();
        int otherPopularity = other.getPopularity();
        int otherItemID = other.getItemID();
        if (province.compareTo(otherProvince) == 0) {
            if (popularity == otherPopularity) {
                return itemID - otherItemID;
            }
            else {
                return otherPopularity - popularity;
            }
        }
        else {
            return province.compareTo(otherProvince);
        }
    }
}