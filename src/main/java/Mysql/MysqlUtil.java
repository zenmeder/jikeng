package Mysql;

//import org.json.JSONArray;
//import org.json.JSONObject;

import Hbase.LinktoHbase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhaoyu on 16/04/2018.
 */
public class MysqlUtil {
    public  static JSONArray  sortJsonArray(JSONArray array){
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < array.size(); i++) {
            jsonValues.add(array.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            // You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "depth";

            public int compare(JSONObject a, JSONObject b) {
//                String valA = new String();
//                String valB = new String();
                Integer valA = 0;
                Integer valB = 0;
                try {
                    // 这里是a、b需要处理的业务，需要根据你的规则进行修改。
                    String aStr = a.getString(KEY_NAME);
                    String bStr = b.getString(KEY_NAME);
                    valA = Integer.parseInt(aStr);
                    valB = Integer.parseInt(bStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (valA == valB)
                    return 0;
                else if(valA>valB)
                    return 1;
                else
                    return -1;
            }
        });
        for (int i = 0; i < array.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        return sortedJsonArray;
    }
    public static JSONArray substract(JSONArray ja, Integer holesNum){
        Integer i = 0;
        Integer j = 0;
        JSONArray res = new JSONArray();
        while (i < ja.size() && j<holesNum){
            JSONObject jo = ja.getJSONObject(i);
            jo.put("serialNum", j);
            res.add(jo);
            i += ja.size()/holesNum;
            j += 1;
        }
        return res;
    }
    public static void  main(String[] args){
        LinkToMysql ltm = new LinkToMysql();
        String date = "2014-03-27";
        List ja = Arrays.asList("jikeng1","jikeng2","jikeng3");
        String startTime = date + " 00:00:01";
        String endTime = date + " 23:59:59";
        Map<String, JSONArray> m = new HashMap<String, JSONArray>();
        for(int i = 0; i<ja.size();i++){
            m.put(ja.get(i).toString(), ltm.getSensorPosition(ja.get(i).toString()));
        }
        Map<String, JSONArray> r = new HashMap<String, JSONArray>();
        for(Map.Entry<String, JSONArray> entry: m.entrySet()){
            for(int i = 0; i<entry.getValue().size();i++){
                String sid = ""+entry.getValue().getJSONObject(i).get("sensorId");
                String k = entry.getKey()+"-"+entry.getValue().getJSONObject(i).get("sensorId");
                try {
                    JSONArray ja1 = LinktoHbase.SelectDataByTimeAndSensorId(startTime, endTime, entry.getKey(), Integer.parseInt(sid));
                    if(r.containsKey(k)){
                        r.get(k).addAll(ja1);
                    }else{
                        r.put(k, ja1);
                    }
                    r.put(k, sortJsonArray(r.get(k)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        JSONArray res = new JSONArray();
        for(Map.Entry<String, JSONArray> entry: r.entrySet()) {
            String[] k = entry.getKey().split("-");
            entry.setValue(substract(entry.getValue(), LinkToMysql.getHolesNum(k[0],Integer.parseInt(k[1]))));
            res.addAll(entry.getValue());
        }
        System.out.println(res);
//        for(int i = 0; i<sensors.size();i++){
//            Integer holesNum = sensors.getJSONObject(i).getInt("holesNum");
//            String modelName = sensors.getJSONObject(i).getString("modelName");
//            try{
//                JSONArray holes = sortJsonArray(LinktoHbase.SelectDatabyTime(startTime, endTime, modelName));
//
////                System.out.println(sortJsonArray(holes));
////                System.out.println(holesNum);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        JSONArray ja = ltm.getModels();
//        System.out.println(ja);
//        JSONArray ja1 = new JSONArray();
//        JSONArray ja2 = new JSONArray();
//        for(int i = 0; i<ja.size();i++){
//            ja1.addAll(ltm.getSensorPosition(ja.getJSONObject(i).get("modelName").toString()));
//            ja2.addAll(ltm.getAllHoles(ja.getJSONObject(i).get("modelName").toString()));
//        }
//        System.out.println(ja1);
//        for (int i = 0; i<ja1.size();i++){
//            System.out.println(ja2.get(i));
//        }
//        JSONArray res = new JSONArray();
//        JSONObject jo = new JSONObject();
//        jo.put("models", ja.toString());
//        jo.put("sensors", ja1.toString());
//        jo.put("holes", ja2.toString());
//        System.out.println(jo);
//        res.set(0, ja.toString());
//        res.set(1, ja1.toString());
//        res.set(2, ja2.toString());
//        System.out.println(res);
//        JSONArray a = ltm.getAllHoles("jikeng2");
//        for(int i=0; i<a.size();i++){
//            System.out.println(a.get(i).toString());
//        }
        for(int i = 1; i<7;i++){
            for(int j = 0; j<15;j++){
                String east = Double.toString(121.614983531255533 + i * 0.000012782699573203141);
                String south = Double.toString(33.122592361610856 - i * 0.00005276838845524878);
                String west = Double.toString(121.614983531255533 + (i + 1) * 0.000012782699573203141);
                String north = Double.toString(33.122592361610856 - (i + 1) * 0.00005276838845524878);
                String maxHeight = Double.toString(18.0 - j * 1.2) + "," + Double.toString(18.0 - j * 1.2);
                String minHeight = Double.toString(18.0 - (j+1) * 1.2) + "," + Double.toString(18.0 - (j+1) * 1.2);
                Integer sensorId = i;
                Integer serialNum = j;
                String modelName = "jikeng3";
                ltm.insertIntoHoles(east, south, west, north, maxHeight, minHeight, modelName,sensorId, serialNum);
            }
        }
//        JSONArray ja = ltm.getSensorPosition("jikeng1");
//        JSONArray ja2 = ltm.getSensorPosition("jikeng2");
//        System.out.println(ja);
//        System.out.println(ja2);
//        System.out.println(ja.addAll(ja2));
//        System.out.println(ja);
//        String s = "[\"jikeng1\",\"jikeng2\"]";
//        JSONArray ja = JSONArray.fromObject(s);
//        System.out.println(ja);
//        for(int i = 0; i<ja.size();i++){
//            System.out.println(ja.get(i).toString());
//        }
//        System.out.println(JSONObject.toBean(s));
//        Map<Integer, JSONObject> m = ltm.getSensorPosition("jikeng1");
//        for(Map.Entry<Integer, JSONObject> entry: m.entrySet()){
//            System.out.println(entry.getValue().get("height"));
//        }
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
//            double latitude = 33.122592361610856 - i * 0.00005276838845524878 - 0.0000263841942276;
//            double height = 18.0;
//            ltm.insertSensorPositionByLLH("jikeng3", i, String.valueOf(latitude), String.valueOf(longitude), String.valueOf(height));
//        }
//        Map<Integer, String> m = ltm.getSensorPosition("jikeng1");
//        for(Map.Entry<Integer, String> entry: m.entrySet()){
//            System.out.println("sensorId is "+ entry.getKey()+", value is "+entry.getValue());
//        }
//        ltm.insertModel("jikeng3", "33.12263393139467", "121.6149828457345", "18","", "0.3", "jikengken3" );

    }
}
