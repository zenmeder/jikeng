/**
 * Created by Zenmeder on 2018/4/18.
 */
package Mysql;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.Format;
import java.util.Arrays;
import java.util.List;

public class testLink {
    public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://10.171.6.198:1433;databaseName=StackOverflow";
            Connection con = DriverManager.getConnection(url,"sa","hadoop");
            System.out.println("数据库连接成功");
            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from dbo.Posts");
//            Integer lineNum = 1;
//            Integer fileNum = 1;
//            String toWrite = "";
//            while (rs.next()){
//                if(lineNum % 100000 == 0){
//                    System.out.println("读取到了第"+lineNum+"行");
//                }
//                List head = Arrays.asList("Id","AcceptedAnswerId","AnswerCount","OwnerUserId","ParentId","PostTypeId","FavoriteCount","Score","ViewCount");
//                String s = "<head>";
//                for(int i =0; i<head.size();i++){
//                    s += rs.getString(head.get(i).toString())+",";
//                }
//                s += "</head>\n";
//                s += "<tags>"+rs.getString("Tags")+"</tags>\n";
//                s += "<body>" + rs.getString("Body")+"</body>\n";
//                toWrite += s;
//                if(lineNum % 1000000 == 0){
//                    FileOutputStream outstr = new FileOutputStream(new File("C:\\Users\\Zenmeder\\Desktop\\post"+fileNum+".txt"));
//                    BufferedOutputStream buff = new BufferedOutputStream(outstr);
//                    System.out.println("正在生成第"+fileNum+"个文件");
//                    buff.write(toWrite.getBytes());
//                    System.out.println("已生成第"+fileNum+"个文件");
//                    System.out.println("正在生成第"+fileNum+1+"个文件");
//                    fileNum += 1;
//                    toWrite = "";
//
//                }
//                lineNum += 1;
//            }

//            int i =  1;
//            int j = 19;
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = dbf.newDocumentBuilder();
//            Document dom = builder.newDocument();
//            Element root = dom.createElement("posts");
//            dom.appendChild(root);
//
//            while (rs.next()){
//                if (i%100000 == 0){
//                    System.out.println("已读取到了第"+i+"行");
//                }
//                if (i<=18000000){
//                    i++;
//                    continue;
//                }
////                System.out.println(rs.getString("Id"));
//
////                System.out.println("正在处理第"+i+"行");
//                Element element = dom.createElement("post");
//                element.setAttribute("Id", rs.getString("Id"));
//                element.setAttribute("AcceptedAnswerId", rs.getString("AcceptedAnswerId"));
//                element.setAttribute("AnswerCount", rs.getString("AnswerCount"));
//                element.setAttribute("OwnerUserId", rs.getString("OwnerUserId"));
//                element.setAttribute("ParentId", rs.getString("ParentId"));
//                element.setAttribute("PostTypeId", rs.getString("PostTypeId"));
//                element.setAttribute("FavoriteCount", rs.getString("FavoriteCount"));
//                element.setAttribute("Score", rs.getString("Score"));
//                element.setAttribute("ViewCount", rs.getString("ViewCount"));
//                element.setAttribute("tags",rs.getString("Tags") );
//                element.setAttribute("body", rs.getString("Body"));
//                root.appendChild(element);
//                if(i%1000000 == 0){
//                    TransformerFactory factory = TransformerFactory.newInstance();
//                    Transformer transformer = factory.newTransformer();
//                    transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
//                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//                    transformer.transform(new DOMSource(dom), new StreamResult("C:\\Users\\Zenmeder\\Desktop\\posts"+j+".xml"));
//                    System.out.println("正在生成第"+j+"个文件");
//                    j++;
//                    dbf = DocumentBuilderFactory.newInstance();
//                    builder = dbf.newDocumentBuilder();
//                    dom = builder.newDocument();
//                    root = dom.createElement("posts");
//                    dom.appendChild(root);
//                }
//                i++;
//            }
            con.close();
        }
        catch(Exception e) {
            System.out.println("数据库连接失败\n" + e.toString());
        }
    }
}
