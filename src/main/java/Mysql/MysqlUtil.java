package Mysql;

import java.util.Map;

/**
 * Created by zhaoyu on 16/04/2018.
 */
public class MysqlUtil {

    public static void  main(String[] args){
        LinkToMysql ltm = new LinkToMysql();
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
//        ltm.insertModel("jikeng1", "31.12263393139467", "121.6149828457345", "18","models/jikeng/jikengup1.gltf", "0.3", "jikengken" );

    }
}
