package AverageShift;

import Hbase.HBaseUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * Created by hadoop on 18-4-3.
 */
public class TestLinkHbase {
    public static void main(String[] args){
        String[] families = {"depth"};
        Configuration configuration = HBaseConfiguration.create();
        try {
            HBaseUtils.creatTable("band1", families);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("aaa");
    }
}
