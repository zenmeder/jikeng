package Kafka;

/**
 * Created by hadoop on 18-4-2.
 */
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class SimpleProducer {
    private static Producer<Integer, String> producer;
    private final Properties props = new Properties();

    public SimpleProducer() {
        //定义连接的broker list
        props.put("metadata.broker.list", "localhost:9092");
        //定义序列化类 Java中对象传输之前要序列化
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        producer = new Producer<Integer, String>(new ProducerConfig(props));
    }

    public static void main(String[] args) {
        SimpleProducer sp = new SimpleProducer();
        //定义topic
        String topic = "mytopic";

        //定义要发送给topic的消息
        String messageStr = "This is a message";

        //构建消息对象
        KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic, messageStr);

        //推送消息到broker
        producer.send(data);
        producer.close();
    }
}
