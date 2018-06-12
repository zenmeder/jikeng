package Servlet;

import Hbase.LinktoHbase;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class GetCurrentData
 */
@WebServlet("/GetCurrentData")
public class GetCurrentData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCurrentData() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String date = request.getParameter("date");
		String modelName = request.getParameter("modelName");
		String sensorId = request.getParameter("sensorId");
		String modelNums = request.getParameter("modelNums");
		response.setContentType("text/plain; charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String jsonpCallback = request.getParameter("jsonpCallback");
		PrintWriter out = response.getWriter();
		JSONArray jsonpOut = new JSONArray();
		String tableName = modelName;
		if (date.equals("now")) {
			jsonpOut = LinktoHbase.SelectMyCurrentData(tableName);
			Data4exchange.getInstance().setCurrentData(jsonpOut);
//			out.println(jsonpCallback + "(" + jsonpOut + ")");
		}
		else if(modelNums.equals("single")) {
			String startTime = date + " 00:00:01";
			String endTime = date + " 23:59:59";
			System.out.println("date is "+date);
			jsonpOut = LinktoHbase.SelectDatabyTime(startTime, endTime, tableName);
		}else{
			String startTime = date + " 00:00:01";
			String endTime = date + " 23:59:59";
			JSONArray ja = JSONArray.fromObject(tableName);
			for(int i = 0; i<ja.size();i++)
				jsonpOut.addAll(LinktoHbase.SelectDatabyTime(startTime, endTime, ja.get(i).toString()));
		}
		out.println(jsonpCallback + "(" + jsonpOut + ")");

		out.flush();
		out.close();
	}
}
