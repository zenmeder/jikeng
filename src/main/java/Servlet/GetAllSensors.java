package Servlet;

import Mysql.LinkToMysql;
import net.sf.json.JSONArray;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zhaoyu on 17/04/2018.
 */

/**
 * Servlet implementation class SelectCurrentData
 */
@WebServlet("/GetAllSensors")
public class GetAllSensors extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllSensors() {
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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // doGet(request, response);
        String jsonModelName = request.getParameter("jsonModelName");
         System.err.println("jsonModelName:"+jsonModelName);
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String jsonpCallback = request.getParameter("jsonpCallback");
        PrintWriter out = response.getWriter();
        System.err.println(jsonModelName);
        JSONArray ja = JSONArray.fromObject(jsonModelName);
        JSONArray jsonpOut = new JSONArray();
        LinkToMysql ltm = new LinkToMysql();
        for(int i = 0; i<ja.size();i++){
            System.err.println("modelName is :"+ja.get(i).toString());
            JSONArray jay = ltm.getSensorPosition(ja.get(i).toString());
            jsonpOut.addAll(jay);
            System.out.println("Sensors are: "+jsonpOut);
        }
        out.println(jsonpCallback + "(" + jsonpOut + ")");
        out.flush();
        out.close();
//    }
    }
}
