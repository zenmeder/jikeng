package AverageShift;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;


import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by hadoop on 18-4-2.
 */
public class DataProducer {
    private static String topic = "demo";
    private static void readFromExcel(String path, Integer sheetNum, Producer<String, String> producer) throws Exception{
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hwb = new HSSFWorkbook(is);
        for(;sheetNum<=hwb.getNumberOfSheets();sheetNum++){
            HSSFSheet hs = hwb.getSheetAt(sheetNum);
            String sheetName = hs.getSheetName();
            System.out.println("sheetName is "+sheetName);
            for(int rowNum = 1; rowNum<= hs.getLastRowNum(); rowNum++){
                HSSFRow  hr = hs.getRow(rowNum);
                Date date = hr.getCell(1).getDateCellValue();
                String pattern="yyyy-MM-dd hh:mm:ss";
                String strDate= date==null ? " " : new SimpleDateFormat(pattern).format(date);
                System.out.println("Date is "+strDate);
                for(int cellNum = 2; cellNum<=hr.getLastCellNum(); cellNum++){
                    if(hr.getCell(cellNum) != null){
                        JSONObject jo = new JSONObject();
                        jo.put("sensorId", sheetNum);
                        jo.put("date", strDate);
                        jo.put("depth",0.5*(cellNum-2));
                        jo.put("shift",hr.getCell(cellNum));
                        String value = jo.toString();
                        ProducerRecord<String, String> msg = new ProducerRecord<String, String>(topic, value);
                        producer.send(msg);
                    }
                }
                Thread.sleep(5000);
            }
            Thread.sleep(5000);
        }
        producer.close();
        System.out.println("Finish Produce");
    }
    public static void main(String[] args){
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer= new KafkaProducer<String, String>(props);
        try {
            readFromExcel("/Users/zhaoyu/Desktop/jikeng/src/main/webapp/dataSample.xls", 1, producer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
