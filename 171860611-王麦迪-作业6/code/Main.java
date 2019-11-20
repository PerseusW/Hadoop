import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Result;

import org.apache.log4j.Logger;

public class Main
{
    private static final Logger logger = Logger.getLogger(Main.class);
    private static final String myTableName = "students";

    private static void createTable(Admin admin) throws IOException {
        logger.info("Creating table...");
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf(myTableName));
        table.addFamily(new HColumnDescriptor("Description").setCompressionType(Algorithm.NONE));
        table.addFamily(new HColumnDescriptor("Courses").setCompressionType(Algorithm.NONE));
        table.addFamily(new HColumnDescriptor("Home").setCompressionType(Algorithm.NONE));
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
        logger.info("Table created");
    }

    private static void listTables(Admin admin) throws IOException {
        logger.info("shell> list");
        TableName[] tableNames = admin.listTableNames();
        for (int i = 0; i < tableNames.length; i++) {
            logger.info(tableNames[i].getNameAsString());
        }
        logger.info("End of list");
    }

    private static void insertData(Connection connection) throws IOException {
        Table table = connection.getTable(TableName.valueOf(myTableName));
        Put person1 = new Put(Bytes.toBytes("001"));
        person1.addColumn(Bytes.toBytes("Description"), Bytes.toBytes("Name"), Bytes.toBytes("Li Lei"));
        person1.addColumn(Bytes.toBytes("Description"), Bytes.toBytes("Height"), Bytes.toBytes("176"));
        person1.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Chinese"), Bytes.toBytes("80"));
        person1.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Math"), Bytes.toBytes("90"));
        person1.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Physics"), Bytes.toBytes("95"));
        person1.addColumn(Bytes.toBytes("Home"), Bytes.toBytes("Province"), Bytes.toBytes("Zhejiang"));
        Put person2 = new Put(Bytes.toBytes("002"));
        person2.addColumn(Bytes.toBytes("Description"), Bytes.toBytes("Name"), Bytes.toBytes("Han Meimei"));
        person2.addColumn(Bytes.toBytes("Description"), Bytes.toBytes("Height"), Bytes.toBytes("183"));
        person2.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Chinese"), Bytes.toBytes("88"));
        person2.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Math"), Bytes.toBytes("77"));
        person2.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Physics"), Bytes.toBytes("66"));
        person2.addColumn(Bytes.toBytes("Home"), Bytes.toBytes("Province"), Bytes.toBytes("Beijing"));
        Put person3 = new Put(Bytes.toBytes("003"));
        person3.addColumn(Bytes.toBytes("Description"), Bytes.toBytes("Name"), Bytes.toBytes("Xiao Ming"));
        person3.addColumn(Bytes.toBytes("Description"), Bytes.toBytes("Height"), Bytes.toBytes("162"));
        person3.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Chinese"), Bytes.toBytes("90"));
        person3.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Math"), Bytes.toBytes("90"));
        person3.addColumn(Bytes.toBytes("Courses"), Bytes.toBytes("Physics"), Bytes.toBytes("90"));
        person3.addColumn(Bytes.toBytes("Home"), Bytes.toBytes("Province"), Bytes.toBytes("Shanghai"));
        logger.info("Inserting data...");
        table.put(person1);
        table.put(person2);
        table.put(person3);
        logger.info("Data inserted");
        table.close();
    }

    private static void scanData(Connection connection) throws IOException {
        logger.info("shell> scan 'students'");
        Table table = connection.getTable(TableName.valueOf(myTableName));
        ResultScanner scanner = table.getScanner(new Scan());
        for (Result result = scanner.next(); result != null; result = scanner.next()) {
            logger.info(result);
        }
        logger.info("End of table");
        scanner.close();
        table.close();
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();
        createTable(admin);
        listTables(admin);
        insertData(connection);
        scanData(connection);
        admin.close();
        connection.close();
    }
}