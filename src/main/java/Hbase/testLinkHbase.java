package Hbase;

/**
 * Created by zhaoyu on 17-11-28.
 */
import net.sf.json.JSONArray;
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
        JSONArray jsonpOut = null;
        String date = "2015-03-02";
        String tableName = "jikeng1";
        String startTime = date + " 00:00:01";
        String endTime = date + " 23:59:59";
        try{
            jsonpOut = LinktoHbase.SelectDatabyTime(startTime, endTime, tableName);
            System.out.println(jsonpOut);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}