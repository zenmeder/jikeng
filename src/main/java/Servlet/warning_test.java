//����Ϊ�˲���WRINING��Ϣ�ļ�⣬������Ϻ����ɾ����java�ļ�
package Servlet;

import java.io.IOException;

import Hbase.LinktoHbase;
import net.sf.json.JSONArray;

public class warning_test {
	public static void main(String []args){
		// ���ڻ�ȡ����
		JSONArray data = new JSONArray();
		try {
			data=LinktoHbase.SelectMyCurrentData("band3");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ����data������Ƿ���Σ����Ϣ
		for (int i = 0; i < data.size(); i++) {
			//System.out.println(Double.parseDouble(data.getJSONObject(i).getString("value")));
			//���������һ������50��value
			if (Double.parseDouble(data.getJSONObject(i).getString("value"))> 50) {
				System.out.println("WARNING!!!");
				break;
			}			
		}
	}
}
