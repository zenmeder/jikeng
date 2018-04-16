package Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.bcel.internal.generic.Select;

import net.sf.json.JSONArray;
import Hbase.LinktoHbase;

/**
 * Servlet implementation class Warning
 */
@WebServlet("/Warning")
public class Warning extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//警报阈值
	private static final double WARNING_THRE = 50;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Warning() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// init
		response.setContentType("text/plain; charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 返回信息
		PrintWriter out = response.getWriter();
		// 用于获取数据
		JSONArray data = new JSONArray();
		//data = Data4exchange.getInstance().getCurrentData();
		data=LinktoHbase.SelectMyCurrentData("band3");
		// 遍历data并检查是否有危险信息
		for (int i = 0; i < data.size(); i++) {
			//如果有任意一条大于阈值WARNING_THRE的value就会发出警报
			if (Double.parseDouble(data.getJSONObject(i).getString("value"))> WARNING_THRE) {
				System.out.println("WARNING!!!");
				out.println("WARNING!!!");	
				break;
			}			
		}
		out.flush();
		out.close();
	}

}
