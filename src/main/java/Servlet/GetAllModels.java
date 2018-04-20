package Servlet;

import Hbase.LinktoHbase;
import Mysql.LinkToMysql;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;

/**
 * Created by zhaoyu on 17/04/2018.
 */

/**
 * Servlet implementation class SelectCurrentData
 */
@WebServlet("/GetAllModels")
public class GetAllModels extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllModels() {
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
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String jsonpCallback = request.getParameter("jsonpCallback");
        PrintWriter out = response.getWriter();

        // ???????
        final double WARNING_THRE = 50.0;
        // ??????????
        JSONArray data = new JSONArray();
        // data = Data4exchange.getInstance().getCurrentData();
        data = LinktoHbase.SelectMyCurrentData("band4");
        // ??????????
        boolean isDangerous = false;
        // ???????value??????????
        JSONObject jo_max = data.getJSONObject(0);
        //????????????
        double jo_max_val = 0.0;
        //????????????
        double jo_accumulation = 0.0;
        // ????data????????????????
        for (int i = 0; i < data.size(); i++) {
            double value = data.getJSONObject(i).getDouble("value");
            //?????????
            jo_accumulation += value;
            // ???????????????????WARNING_THRE??value?????????
            if (value > WARNING_THRE) {
                //System.out.println("WARNING!!!");
                isDangerous = true;
                //break;
            }
            //??????
            if (value > jo_max_val) {
                jo_max = data.getJSONObject(i);
                jo_max_val = jo_max.getDouble("value");
            }
        }
        String time = jo_max.getString("time");
        // ?????JSONObject
        JSONObject jo_out = new JSONObject();
        jo_out.put("time", time);
        jo_out.put("depthID", jo_max.getString("depthID"));
        jo_out.put("sensorID", jo_max.getString("sensorID"));
        jo_out.put("status", isDangerous ? "warning" : "safe");
        jo_out.put("threshold", WARNING_THRE);
        jo_out.put("value", jo_max.getDouble("value"));
        jo_out.put("average", jo_accumulation / (double) data.size());


        //?????
        //out.println(jsonpCallback + "(" + jo_out + ")");// ????jsonp???????
        out.println(jo_out);
		/*
		JSONArray jsonpOut = new JSONArray();
		// hbase??????table name
		String tableName = "band2";
		jsonpOut = LinktoHbase.SelectDatabyTime("2013-10-06 00:00:01", "2013-10-06 23:59:59", tableName);

		out.println(jsonpCallback + "(" + jsonpOut + ")");// ????jsonp???????
		// System.out.println(jsonpOut);
		*/

        out.flush();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // doGet(request, response);
        String userName = request.getParameter("userName");
        // System.out.println("date:"+date);
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String jsonpCallback = request.getParameter("jsonpCallback");
        PrintWriter out = response.getWriter();
        LinkToMysql ltm = new LinkToMysql();
        JSONArray jsonpOut = ltm.getModels();
        out.println(jsonpCallback + "(" + jsonpOut + ")");
        out.flush();
        out.close();
//    }
    }
}
