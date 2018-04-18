package Mysql;

//import org.json.JSONArray;
//import org.json.JSONObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyu on 16/04/2018.
 */
public class MysqlUtil {

    public static void  main(String[] args){
        LinkToMysql ltm = new LinkToMysql();
        Map<Integer, JSONObject> m = ltm.getSensorPosition("jikeng1");
        for(Map.Entry<Integer, JSONObject> entry: m.entrySet()){
//            System.out.println(entry.getValue().get("height"));
        }
//        JSONArray r = ltm.getModels();
//        System.out.println(r);
//        for (int i = 0; i<r.size();i++){
//            JSONObject jo = r.getJSONObject(i);
//            System.out.println(jo.get("modelUrl"));
//        }
//        for(JSONObject s: r){
//            System.out.println(s);
//        }
//        for(int i = 0; i< r. i++){
//
//        }
//        ltm.getAll();
//        ltm.getAll("select * from t1");
//        ltm.insertSensorPosition("jikeng1",1,"121.614983531255533,31.122592361610856");
//        for(int i = 1; i<=6;i++){
//            double longitude = 121.614983531255533+ i * 0.000012782699573203141 + 0.0000063913497866;
//            double latitude = 31.122592361610856 - i * 0.00005276838845524878 - 0.0000263841942276;
//            double height = 18.0;
//            ltm.insertSensorPositionByLLH("jikeng1", i, String.valueOf(latitude), String.valueOf(longitude), String.valueOf(height));
//        }
//        Map<Integer, String> m = ltm.getSensorPosition("jikeng1");
//        for(Map.Entry<Integer, String> entry: m.entrySet()){
//            System.out.println("sensorId is "+ entry.getKey()+", value is "+entry.getValue());
//        }
//        ltm.insertModel("jikeng1", "31.12263393139467", "121.6149828457345", "18","", "0.3", "jikengken" );

    }
}
