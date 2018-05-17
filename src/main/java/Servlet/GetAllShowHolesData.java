package Servlet;

import Mysql.LinkToMysql;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import Hbase.LinktoHbase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zhaoyu on 17/04/2018.
 */

/**
 * Servlet implementation class GetAllSensors
 */
@WebServlet("/GetAllShowHolesData")
public class GetAllShowHolesData extends HttpServlet {
    public static JSONArray sortJsonArray(JSONArray array) {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < array.size(); i++) {
            jsonValues.add(array.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "depth";

            public int compare(JSONObject a, JSONObject b) {
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
                else if (valA > valB)
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
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllShowHolesData() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    /**
     *
     * @param ja
     * @param holesNum
     * @return
     */
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
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // doGet(request, response);
        String jsonModelName = request.getParameter("jsonModelName");
        String date = request.getParameter("date");
        System.out.println(jsonModelName+date);
//         System.err.println("jsonModelName:"+jsonModelName);
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String jsonpCallback = request.getParameter("jsonpCallback");
        PrintWriter out = response.getWriter();
        JSONArray ja = JSONArray.fromObject(jsonModelName);
//        JSONArray jsonpOut = new JSONArray();
        LinkToMysql ltm = new LinkToMysql();
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
        out.println(jsonpCallback + "(" + res + ")");
        out.flush();
        out.close();
//    }
    }
}
