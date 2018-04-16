package Hbase;

/**
 * Created by zhaoyu on 17-11-28.
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
/**
 * Created by xuxp on 2017/7/14.
 */
public class testLinkHbase {
    public static void main(String[] args) {
        System.out.println("connect begin");
        Configuration configuration = HBaseConfiguration.create();
        System.out.println("connect ok");
        try {
            HTable table = new HTable(configuration, "band4");
//            Get get = new Get("r4");
//            Put put = new Put(Bytes.toBytes("hello12"));
//            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("cf"), Bytes.toBytes("vale"));
//            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}