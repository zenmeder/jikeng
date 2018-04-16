package Hbase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.Export;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * Created by zhaoyu on 29/03/2018.
 */
public class HbaseToFile {
    public static void main(String[]args) throws Exception{
        Configuration conf = HBaseConfiguration.create();
//        String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
//        if(otherArgs.length < 2) {
//            usage("Wrong number of arguments: " + otherArgs.length);
//            System.exit(-1);
//        }
        String[] s = {"band4","/user/zhaoyu/hbase_backup"};

        Job job = Export.createSubmittableJob(conf, s);
        System.exit(job.waitForCompletion(true)?0:1);
//        Configuration conf = new Configuration();
//        try {
//            Export.createSubmittableJob(conf, s);
//        }catch (Exception e){
//            System.out.println("ao");
//        }
    }
}
