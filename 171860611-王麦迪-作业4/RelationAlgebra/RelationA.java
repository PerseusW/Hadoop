import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class RelationA implements WritableComparable<RelationA>{
	private int id;
	private String name;
	private int age;
	private int weight;

	public int getId() {
		return id;
	}
	
	public RelationA(){}
	
	public RelationA(String line){
		String[] value = line.split(",");
		id = Integer.parseInt(value[0]);
		name = value[1];
		age = Integer.parseInt(value[2]);
		weight = Integer.parseInt(value[3]);
	}
	
	public boolean isCondition(int col, String value){
		if(col == 0 && Integer.parseInt(value) == this.id)
			return true;
		else if(col == 1 && name.equals(value))
			return true;
		else if(col ==2 && Integer.parseInt(value) == this.age)
			return true;
		else if(col ==3 && Double.parseDouble(value) == this.weight)
			return true;
		else
			return false;
	}
	
	@Override
	public String toString(){
		return id + "\t" + name + "\t" + age + "\t" + weight;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(id);
		out.writeUTF(name);
		out.writeInt(age);
		out.writeInt(weight);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		id = in.readInt();
		name = in.readUTF();
		age = in.readInt();
		weight = in.readInt();
	}

	@Override
	public int compareTo(RelationA o) {
		if(id < o.getId())
			return -1;
		else
			return 1;
	}
}
