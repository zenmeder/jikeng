package Mysql;

//import org.json.JSONArray;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.*;

/**
 * Created by zhaoyu on 16/04/2018.
 * mysql增删改查操作
 */
public class LinkToMysql {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jikeng";
    private static final String userName = "root";
    private static final String passwd = "hyx19920831";
    private static Connection conn = null;
    private static Statement stmt = null;

    public LinkToMysql() {
        try {
            Class.forName(JDBC_DRIVER);
            System.err.println("start linking to database");
            conn = DriverManager.getConnection(DB_URL, userName, passwd);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有可用的模型
     * @return JSONArray
     * todo 根据用户名来筛选展示哪些模型
     */
    public JSONArray getModels(){
        String sql = "select * from models";
        JSONArray res = new JSONArray();
        try{
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                JSONObject ja = new JSONObject();
                ja.put("modelName", rs.getString("modelName"));
                ja.put("latitude", rs.getString("latitude"));
                ja.put("longitude", rs.getString("longitude"));
                ja.put("height", rs.getString("height"));
                ja.put("modelUrl", rs.getString("modelUrl"));
                if(findColumn(rs, "scale"))
                    ja.put("scale", rs.getString("scale"));
                if(findColumn(rs, "description"))
                    ja.put("description", rs.getString("description"));
                res.add(ja);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 判断resultSet中有没有指定列
     * @param rs resultSet
     * @param columnLabels 列名
     * @return Boolean
     */
    public Boolean findColumn(ResultSet rs, String columnLabels){
        try{
            rs.findColumn(columnLabels);
        }catch (Exception e){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    /**
     * 将模型的名字，经纬度以及高度等信息存入models表中
     * @param modelName 模型名
     * @param latitude 纬度
     * @param longitude 经度
     * @param height 高度
     * @param modelUrl 模型url地址
     * @param scale 模型的尺度
     * @param description 模型描述
     */
    public void insertModel(String modelName, String latitude, String longitude, String height, String modelUrl, String scale, String description){
        List<String> args = new ArrayList<String>(Arrays.asList("modelName", "latitude", "longitude", "height"));
        List<String> values = new ArrayList<String>(Arrays.asList(modelName, latitude, longitude, height));
        if(!modelUrl.equals("")){
            args.add("modelUrl");
            values.add(modelUrl);
        }
        if(!scale.equals("")){
            args.add("scale");
            values.add(scale);
        }
        if(!description.equals("")){
            args.add("description");
            values.add(description);
        }
        String sql = "insert into models(";
        for(int i = 0; i<args.size();i++){
            sql += args.get(i);
            if(i<args.size()-1){
                sql += ",";
            }
        }
        sql += ") values (";
        for(int i = 0; i<values.size();i++){
            sql += "\""+values.get(i)+"\"";
            if(i<values.size()-1){
                sql += ",";
            }
        }
        sql += ")";
        System.out.println(sql);
        try{
            stmt.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * 通过modelName查询传感器及其位置信息
     * @param modelName 传感器名
     * @return Map<Integer, JSONObject>
     */
    public Map<Integer, JSONObject> getSensorPosition(String modelName){

        String sql = "select * from sensor where modelName="+"\""+modelName+"\"";
        Map<Integer, JSONObject> hm = new HashMap<Integer, JSONObject>();
        try{
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                JSONObject jo = new JSONObject();
                Integer sensorId = rs.getInt("sensorId");
                jo.put("latitude", rs.getString("latitude"));
                jo.put("longitude", rs.getString("longitude"));
                jo.put("height", rs.getString("height"));
                hm.put(sensorId, jo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return hm;
    }

    /**
     * 将模型名， 传感器id以及位置存入表中
     * @param modelName 模型名
     * @param sensorId 传感器id
     * @param position 位置
     */
    public void insertSensorPosition(String modelName, Integer sensorId, String position){
        try{
            String sql = "insert into sensor(modelName, sensorId, position) values ("+"\""+modelName+"\""+", "+sensorId+", ";
            sql += "\"" + position +"\")";
            stmt.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * 将模型名， 传感器id以及经纬度高度存入表中
     * @param modelName 模型名
     * @param sensorId 传感器id
     * @param latitude 纬度
     * @param longitude 经度
     * @param height 高度
     */
    public void insertSensorPositionByLLH(String modelName, Integer sensorId,String latitude, String longitude, String height){
        String sql = "insert into sensor(modelName, sensorId, latitude, longitude, height) values (";
        sql += "\""+modelName+"\",";
        sql += "\""+sensorId+"\",";
        sql += "\""+latitude+"\",";
        sql += "\""+longitude+"\",";
        sql += "\""+height+"\")";
        try{
            stmt.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
