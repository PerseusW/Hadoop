import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class RelationA implements WritableComparable<RelationA>
{
	private int id;
	private String name;
	private int age;
	private int weight;
	
	public RelationA() {}
	
	public RelationA(String line) {
		String[] info = line.split(",");
		id = Integer.parseInt(info[0]);
		name = info[1];
		age = Integer.parseInt(info[2]);
		weight = Integer.parseInt(info[3]);
	}
	
	@Override
	public String toString() {
		return id + "," + name + "," + age + "," + weight;
	}

	@Override
	public void write(DataOutput outputStream) throws IOException {
		outputStream.writeInt(id);
		outputStream.writeUTF(name);
		outputStream.writeInt(age);
		outputStream.writeInt(weight);
	}

	@Override
	public void readFields(DataInput inputStream) throws IOException {
		id = inputStream.readInt();
		name = inputStream.readUTF();
		age = inputStream.readInt();
		weight = inputStream.readInt();
	}

	@Override
	public int compareTo(RelationA otherA) {
		if (id == otherA.getId()) {
			return 0;
		}
		else if (id < otherA.getId()) {
			return -1;
		}
		else {
			return 1;
		}
	}

	public int getId() {
		return id;
	}

	public boolean columnEquals(int column, String value) {
		switch (column) {
			case 0: if (id == Integer.parseInt(value)) {return true;} break;
			case 1: if (name.equals(value)) {return true;} break;
			case 2: if (age == Integer.parseInt(value)) {return true;} break;
			case 3: if (weight == Integer.parseInt(value)) {return true;} break;
		}
		return false;
	}

	public boolean columnSmallerThan(int column, String value) {
		switch (column) {
			case 0: if (id < Integer.parseInt(value)) {return true;} break;
			case 2: if (age < Integer.parseInt(value)) {return true;} break;
			case 3: if (weight < Integer.parseInt(value)) {return true;} break;
		}
		return false;
	}

	public boolean columnGreaterThan(int column, String value) {
		switch (column) {
			case 0: if (id > Integer.parseInt(value)) {return true;} break;
			case 2: if (age > Integer.parseInt(value)) {return true;} break;
			case 3: if (weight > Integer.parseInt(value)) {return true;} break;
		}
		return false;
	}

	public String getColumn(int column) {
		switch (column) {
			case 0: return String.valueOf(id);
			case 1: return name;
			case 2: return String.valueOf(age);
			case 3: return String.valueOf(weight);
		}
		return new String();
	}
}
