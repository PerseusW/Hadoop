import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class UserLog implements WritableComparable<UserLog>
{
    private int userID;
    private int itemID;
    private int itemCategoryID;
    private int merchantID;
    private int brandID;
    private int month;
    private int day;
    private int action;
    private int ageRange;
    private int gender;
    private String province;

    public UserLog() {
        userID = -1;
        itemID = -1;
        itemCategoryID = -1;
        merchantID = -1;
        brandID = -1;
        month = -1;
        day = -1;
        action = -1;
        ageRange = -1;
        gender = -1;
        province = "-1";
    }

    public UserLog(String line) {
        String[] tuple = line.split(",");
        if (!tuple[0].isEmpty()) {
            userID = Integer.parseInt(tuple[0]);
        }
        if (!tuple[1].isEmpty()) {
            itemID = Integer.parseInt(tuple[1]);
        }
        if (!tuple[2].isEmpty()) {
            itemCategoryID = Integer.parseInt(tuple[2]);
        }
        if (!tuple[3].isEmpty()) {
            merchantID = Integer.parseInt(tuple[3]);
        }
        if (!tuple[4].isEmpty()) {
            brandID = Integer.parseInt(tuple[4]);
        }
        if (!tuple[5].isEmpty()) {
            month = Integer.parseInt(tuple[5]);
        }
        if (!tuple[6].isEmpty()) {
            day = Integer.parseInt(tuple[6]);
        }
        if (!tuple[7].isEmpty()) {
            action = Integer.parseInt(tuple[7]);
        }
        if (!tuple[8].isEmpty()) {
            ageRange = Integer.parseInt(tuple[8]);
        }
        if (!tuple[9].isEmpty()) {
            gender = Integer.parseInt(tuple[9]);
        }
        province = tuple[10];
    }

    public void setByLine(String line) {
        String[] tuple = line.split(",");
        if (!tuple[0].isEmpty()) {
            userID = Integer.parseInt(tuple[0]);
        }
        if (!tuple[1].isEmpty()) {
            itemID = Integer.parseInt(tuple[1]);
        }
        if (!tuple[2].isEmpty()) {
            itemCategoryID = Integer.parseInt(tuple[2]);
        }
        if (!tuple[3].isEmpty()) {
            merchantID = Integer.parseInt(tuple[3]);
        }
        if (!tuple[4].isEmpty()) {
            brandID = Integer.parseInt(tuple[4]);
        }
        if (!tuple[5].isEmpty()) {
            month = Integer.parseInt(tuple[5]);
        }
        if (!tuple[6].isEmpty()) {
            day = Integer.parseInt(tuple[6]);
        }
        if (!tuple[7].isEmpty()) {
            action = Integer.parseInt(tuple[7]);
        }
        if (!tuple[8].isEmpty()) {
            ageRange = Integer.parseInt(tuple[8]);
        }
        if (!tuple[9].isEmpty()) {
            gender = Integer.parseInt(tuple[9]);
        }
        province = tuple[10];
    }

    public int getUserID() {
        return userID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getItemCategoryID() {
        return itemCategoryID;
    }

    public int getMerchantID() {
        return merchantID;
    }

    public int getBrandID() {
        return brandID;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getAction() {
        return action;
    }

    public int getAgeRange() {
        return ageRange;
    }

    public int getGender() {
        return gender;
    }

    public String getProvince() {
        return province;
    }

    public String toString() {
        return String.valueOf(userID) + "," +
               String.valueOf(itemID) + "," +
               String.valueOf(itemCategoryID) + "," +
               String.valueOf(merchantID) + "," +
               String.valueOf(brandID) + "," +
               String.valueOf(month) + "," +
               String.valueOf(day) + "," +
               String.valueOf(action) + "," +
               String.valueOf(ageRange) + "," +
               String.valueOf(gender) + "," +
               province;
    }

    public void readFields(DataInput in) throws IOException {
        userID = in.readInt();
        itemID = in.readInt();
        itemCategoryID = in.readInt();
        merchantID = in.readInt();
        brandID = in.readInt();
        month = in.readInt();
        day = in.readInt();
        action = in.readInt();
        ageRange = in.readInt();
        gender = in.readInt();
        province = in.readUTF();
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(userID);
        out.writeInt(itemID);
        out.writeInt(itemCategoryID);
        out.writeInt(merchantID);
        out.writeInt(brandID);
        out.writeInt(month);
        out.writeInt(day);
        out.writeInt(action);
        out.writeInt(ageRange);
        out.writeInt(gender);
        out.writeUTF(province);
    }

    public int compareTo(UserLog o) {
        if (itemID > o.getItemID()) {
            return 1;
        }
        else if (itemID < o.getItemID()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}