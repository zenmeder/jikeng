package AverageShift;

import Hbase.HBaseUtils;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.json.JSONObject;
import org.apache.log4j.Logger;
import java.util.Map;

/**
 * Created by hadoop on 18-4-2.
 */
public class MyKafkaBolt implements IBasicBolt {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String tableName = "ds4";
    private static final String[] familes = {"depth"};
    private static  Logger logger = Logger.getLogger(MyKafkaBolt.class);
    public static void main(String[] args){
        String kafkaMsg = "{\"date\":\"2014-06-19 12:00:00\",\"depth\":53.5,\"shift\":\"3.61\",\"sensorId\":220}";
        JSONObject jo = new JSONObject(kafkaMsg);
        String sensorId = jo.get("sensorId").toString();
        String date = jo.get("date").toString();
        String depth = jo.get("depth").toString();
        String shift = jo.has("shift")?jo.get("shift").toString():"";
//        System.out.println(sensorId+"\n"+date+"\n"+depth+"\n"+shift);
        for(int i = 0; i<100;i++){
            sensorId = Integer.toString(Integer.parseInt(sensorId) + 1);

       try{
          if(!HBaseUtils.doesTableExists(tableName)){
              System.out.println("Table is not existed");
               HBaseUtils.creatTable(tableName, familes);
          }
           String rowkey = date+"|" + sensorId;
           System.out.println(rowkey);
           HBaseUtils.updateTable(tableName, rowkey,"depth",depth, shift);
           Thread.sleep(5000);
       }catch (Exception e){
           e.printStackTrace();
       }
        System.err.println("sensorId is "+sensorId+", date is "+date+", depth is "+depth);
        }
    }
    public void cleanup() {
//        System.err.println("kafkaMsg: "+1);

        // TODO Auto-generated method stub
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        // TODO Auto-generated method stub
        logger.info("Enter in execute");
        String kafkaMsg = input.getString(0);
        System.err.println("kafkaMsg: "+kafkaMsg);
//        System.err.println("kafkaMsgType is: " + kafkaMsg.getClass());
        JSONObject jo = new JSONObject(kafkaMsg);
        String sensorId = jo.get("sensorId").toString();
        String date = jo.get("date").toString();
        String depth = jo.get("depth").toString();
        String shift = jo.has("shift")?jo.get("shift").toString():"";
        try{
            if(!HBaseUtils.doesTableExists(tableName)){
                System.out.println("create new table: "+tableName);
                HBaseUtils.creatTable(tableName, familes);
            }
            String rowkey = date+"|" + sensorId;
            System.err.println("rowkey is: "+rowkey);
            HBaseUtils.updateTable(tableName, rowkey,"depth",depth, shift);
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.err.println("sensorId is "+sensorId+", date is "+date+", depth is "+depth);
    }

    public void prepare(Map stormConf, TopologyContext context) {
        // TODO Auto-generated method stub
//        System.err.println("kafkaMsg: "+2);
        logger.info("Enter in prepare");
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
        logger.info("Enter in declare");
//        System.err.println("kafkaMsg: "+3);

    }


    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub

        return null;
    }

}