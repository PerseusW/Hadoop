import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Term implements WritableComparable<Term>
{
	private String term;
	private int count;
	private String document;
	
	public Term() {}
	
	@Override
	public String toString() {
		return term + "," + count + "@" + document;
	}

	@Override
	public void write(DataOutput outputStream) throws IOException {
		outputStream.writeUTF(term);
		outputStream.writeInt(count);
		outputStream.writeUTF(document);
	}

	@Override
	public void readFields(DataInput inputStream) throws IOException {
		term = inputStream.readUTF();
		count = inputStream.readInt();
		document = inputStream.readUTF();
	}

	@Override
	public int compareTo(Term other) {
		int termCompare = term.compareTo(other.getTerm());
		int documentCompare = document.compareTo(other.getDocument());
		if (termCompare != 0) {
			return termCompare;
		}
		else if (count != other.getCount()) {
			return other.getCount() - count;
		}
		else {
			return documentCompare;
		}
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getTerm() {
		return term;
	}

	public int getCount() {
		return count;
	}

	public String getDocument() {
		return document;
	}

}
