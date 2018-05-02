package AverageShift;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseConnection {
    private static HBaseAdmin admin = null;
    // 定义配置对象HBaseConfiguration
    private static Configuration configuration;
    public HbaseConnection() throws Exception {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","127.0.0.1");  //hbase 服务地址
        configuration.set("hbase.zookeeper.property.clientPort","2181"); //端口号
        admin = new HBaseAdmin(configuration);
    }
    public static void createTable(String tableName, String[] cols) throws IOException {

        TableName tn = TableName.valueOf(tableName);

        if (admin.tableExists(tn)) {
            System.out.println("table is exists!");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tn);
            for (String col : cols) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
        }
    }
    // Hbase获取所有的表信息
    public static List getAllTables() throws IOException{
        List<String> tables = null;
        if (admin != null) {
            try {
                HTableDescriptor[] allTable = admin.listTables();
                if (allTable.length > 0)
                    tables = new ArrayList<String>();
                for (HTableDescriptor hTableDescriptor : allTable) {
                    tables.add(hTableDescriptor.getNameAsString());
                    System.out.println(hTableDescriptor.getNameAsString());
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tables;
    }
    public static void insertRow(String tableName, String rowkey, String colFamily, String col, String val)
            throws IOException {
        HTable table = new HTable(configuration, tableName);
        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(colFamily), Bytes.toBytes(col), Bytes.toBytes(val));
        table.put(put);
        table.close();
    }

    public static void getData(String tableName, String rowkey, String colFamily) throws IOException {
        HTable table = new HTable(configuration, tableName);
        Get get = new Get(Bytes.toBytes(rowkey));
        // 获取指定列族数据
        // get.addFamily(Bytes.toBytes(colFamily));
        // 获取指定列数据
        // get.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col));
        Result result = table.get(get);
        table.close();
        for(Cell cell: result.rawCells()){
            System. out .println(
                    "Rowkey : " +Bytes. toString (result.getRow())+
                            "   FamiliyQuilifier : " +Bytes. toString (CellUtil. cloneQualifier (cell))+
                            "   Value : " +Bytes. toString (CellUtil. cloneValue (cell))
            );
        }
    }

    public static void deleteTable(String tableName) throws IOException {
        TableName tn = TableName.valueOf(tableName);
        if (admin.tableExists(tn)) {
            admin.disableTable(tn);
            admin.deleteTable(tn);
        }
    }
    public static void main(String[] args) throws Exception {
        HbaseConnection hbaseTest = new HbaseConnection();
    }
}