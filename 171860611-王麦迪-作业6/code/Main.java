import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;

public class Main
{
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf("students"));
        table.addFamily(new HColumnDescriptor("Description").setCompressionType(Algorithm.NONE));
        table.addFamily(new HColumnDescriptor("Courses").setCompressionType(Algorithm.NONE));
        table.addFamily(new HColumnDescriptor("Home").setCompressionType(Algorithm.NONE));
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
        System.out.println("Table Created");
        HTableDescriptor[] tableDescriptor = admin.listTables();
        for (int i = 0; i < tableDescriptor.length; i++) {
            System.out.println(tableDescriptor[i].getNameAsString());
        }
        HTable hTable = new HTable(configuration, "students");
        Put person1 = new Put(Bytes.toBytes("001"));
        person1.add(Bytes.toBytes("Description"), Bytes.toBytes("Name"), Bytes.toBytes("Li Lei"));
        person1.add(Bytes.toBytes("Description"), Bytes.toBytes("Height"), Bytes.toBytes("176"));
        person1.add(Bytes.toBytes("Courses"), Bytes.toBytes("Chinese"), Bytes.toBytes("80"));
        person1.add(Bytes.toBytes("Courses"), Bytes.toBytes("Math"), Bytes.toBytes("90"));
        person1.add(Bytes.toBytes("Courses"), Bytes.toBytes("Physics"), Bytes.toBytes("95"));
        person1.add(Bytes.toBytes("Home"), Bytes.toBytes("Province"), Bytes.toBytes("Zhejiang"));
        Put person2 = new Put(Bytes.toBytes("002"));
        person2.add(Bytes.toBytes("Description"), Bytes.toBytes("Name"), Bytes.toBytes("Han Meimei"));
        person2.add(Bytes.toBytes("Description"), Bytes.toBytes("Height"), Bytes.toBytes("183"));
        person2.add(Bytes.toBytes("Courses"), Bytes.toBytes("Chinese"), Bytes.toBytes("88"));
        person2.add(Bytes.toBytes("Courses"), Bytes.toBytes("Math"), Bytes.toBytes("77"));
        person2.add(Bytes.toBytes("Courses"), Bytes.toBytes("Physics"), Bytes.toBytes("66"));
        person2.add(Bytes.toBytes("Home"), Bytes.toBytes("Province"), Bytes.toBytes("Beijing"));
        Put person3 = new Put(Bytes.toBytes("003"));
        person3.add(Bytes.toBytes("Description"), Bytes.toBytes("Name"), Bytes.toBytes("Xiao Ming"));
        person3.add(Bytes.toBytes("Description"), Bytes.toBytes("Height"), Bytes.toBytes("162"));
        person3.add(Bytes.toBytes("Courses"), Bytes.toBytes("Chinese"), Bytes.toBytes("90"));
        person3.add(Bytes.toBytes("Courses"), Bytes.toBytes("Math"), Bytes.toBytes("90"));
        person3.add(Bytes.toBytes("Courses"), Bytes.toBytes("Physics"), Bytes.toBytes("90"));
        person3.add(Bytes.toBytes("Home"), Bytes.toBytes("Province"), Bytes.toBytes("Shanghai"));

        hTable.put(person1);
        hTable.put(person2);
        hTable.put(person3);
        System.out.println("Data Inserted");
        connection.close();
    }
}