package AverageShift;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by hadoop on 18-4-2.
 */
public class DataConsumer {
    private static KafkaConsumer<String, String> consumer;
    private static String topic = "test";
    public DataConsumer(String zookeeper, String groupId, String topic){
        Properties props = new Properties();
        //定义连接zookeeper信息
        props.put("zookeeper.connect", zookeeper);
        //定义Consumer所有的groupID
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        props.put("bootstrap.servers","localhost:9092");
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(props);
        this.topic = topic;
    }
    public void testConsumer(){
        consumer.subscribe(Arrays.asList(this.topic));
        while (true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record :records){
                String value = record.value();
                JSONObject jo = new JSONObject(value);
                String date = jo.get("date").toString();
                String depth = jo.get("depth").toString();
                String shift = jo.has("shift")?jo.get("shift").toString():"";
                System.out.println("Date is "+date+", depth is "+depth+", shift is "+shift);
            }
        }
    }
    public static void main(String[] args){
        DataConsumer dataConsumer = new DataConsumer("localhost:2181","testgroup", topic);
        dataConsumer.testConsumer();
    }
}
