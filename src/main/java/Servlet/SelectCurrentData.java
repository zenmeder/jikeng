package Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Hbase.LinktoHbase;
import Hbase.writeCsv;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SelectCurrentData
 */
//@WebServlet("SelectCurrentData")
public class SelectCurrentData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SelectCurrentData() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
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
		JSONObject jo_max=data.getJSONObject(0);
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
			if(value >jo_max_val){
				jo_max = data.getJSONObject(i);
				jo_max_val=jo_max.getDouble("value");
			}		
		}
		String time = jo_max.getString("time");
		// ?????JSONObject
		JSONObject jo_out = new JSONObject();
		jo_out.put("time", time);
		jo_out.put("depthID",jo_max.getString("depthID"));
		jo_out.put("sensorID",jo_max.getString("sensorID"));
		jo_out.put("status", isDangerous?"warning":"safe");	
		jo_out.put("threshold", WARNING_THRE);
		jo_out.put("value", jo_max.getDouble("value"));	
		jo_out.put("average", jo_accumulation/(double)data.size());
			

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
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		String date = request.getParameter("date");
		// System.out.println("date:"+date);
		response.setContentType("text/plain; charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String jsonpCallback = request.getParameter("jsonpCallback");
		PrintWriter out = response.getWriter();
		JSONArray jsonpOut = new JSONArray();
		// ?????????????
		if (date.equals("now")) {
			String tableName = "band4";
			jsonpOut = LinktoHbase.SelectMyCurrentData(tableName);
			// ????????????????????
			Data4exchange.getInstance().setCurrentData(jsonpOut);
			out.println(jsonpCallback + "(" + jsonpOut + ")");// ????jsonp???????

		}
		// ??????????????
		else if (date.substring(0, 3).equals("cum")) {
			double[][] data = new double[6][16];
			LinktoHbase.statistics(data, "band2", "2013-10-05", date.substring(3));
			writeCsv.output(data, "/home/zhaoyu/IdeaProjects/jikeng/src/main/webapp/1937-1951.csv");
		}
		// ????????????
		else if (date.equals("warning")) {
			// ???????
			final double WARNING_THRE = 50.0;
			// ??????????
			JSONArray data = new JSONArray();
			// data = Data4exchange.getInstance().getCurrentData();
			data = LinktoHbase.SelectMyCurrentData("band4");

			// ??????????
			boolean isDangerous = false;
			// ????data????????????????
			for (int i = 0; i < data.size(); i++) {
				// ???????????????????WARNING_THRE??value?????????
				if (Double.parseDouble(data.getJSONObject(i).getString("value")) > WARNING_THRE) {
					//System.out.println("WARNING!!!");
					isDangerous = true;
					break;
				}
			}

			// ???????????
			JSONObject jo_0 = data.getJSONObject(0);
			String time = jo_0.getString("time");
			// ?????JSONObject
			JSONObject jo_out = new JSONObject();
			jo_out.put("time", time);
			jo_out.put("status", isDangerous?"warning":"safe");			

			//?????
			out.println(jsonpCallback + "(" + jo_out + ")");// ????jsonp???????
			//System.out.println(jo_out);
		}
		// ???????????????????(THC)
		else if (date.substring(0, 7).equals("calTHC:")) {
//			String d = date.substring(7,-1);
//			String output = LinktoHbase.statistics_text("band4", "2015-03-01", date.substring(7));
			String output = LinktoHbase.statistics_text("band4", date.substring(7), date.substring(7));
			out.println(output);
		}
		// ??????????????
		else {
			String startTime = date + " 00:00:01";
			String endTime = date + " 23:59:59";
			// hbase??????table name
			String tableName = "band4";
			jsonpOut = LinktoHbase.SelectDatabyTime(startTime, endTime, tableName);
			out.println(jsonpCallback + "(" + jsonpOut + ")");// ????jsonp???????
		}
		out.flush();
		out.close();
	}
}
