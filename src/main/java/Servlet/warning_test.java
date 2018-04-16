//单纯为了测试WRINING信息的检测，测试完毕后可以删除此java文件
package Servlet;

import java.io.IOException;

import Hbase.LinktoHbase;
import net.sf.json.JSONArray;

public class warning_test {
	public static void main(String []args){
		// 用于获取数据
		JSONArray data = new JSONArray();
		try {
			data=LinktoHbase.SelectMyCurrentData("band3");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 遍历data并检查是否有危险信息
		for (int i = 0; i < data.size(); i++) {
			//System.out.println(Double.parseDouble(data.getJSONObject(i).getString("value")));
			//如果有任意一条大于50的value
			if (Double.parseDouble(data.getJSONObject(i).getString("value"))> 50) {
				System.out.println("WARNING!!!");
				break;
			}			
		}
	}
}
