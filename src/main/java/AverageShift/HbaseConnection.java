package AverageShift;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseConnection {
    private static HBaseAdmin admin = null;
    // 定义配置对象HBaseConfiguration
    private static Configuration configuration;
    public HbaseConnection() throws Exception {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","10.171.6.153");  //hbase 服务地址
        configuration.set("hbase.zookeeper.property.clientPort","2181"); //端口号
        admin = new HBaseAdmin(configuration);
    }
    // Hbase获取所有的表信息
    public static List getAllTables() {
        List<String> tables = null;
        if (admin != null) {
            try {
                HTableDescriptor[] allTable = admin.listTables();
                System.out.println("right");
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
    public static void main(String[] args) throws Exception {
        HbaseConnection hbaseTest = new HbaseConnection();
        HbaseConnection.getAllTables();
    }
}