//�����ר�����ڲ�ͬservlet֮�����Ϣ����
package Servlet;

import net.sf.json.JSONArray;

//����ģʽ
public class Data4exchange {
    private Data4exchange() {}  
    private static Data4exchange instance=null;  
    //��̬��������   
    public static Data4exchange getInstance() {  
         if (instance == null) {    
             instance = new Data4exchange();  
         }    
        return instance;  
    }  
    
    //����currentData(��ǰ���µ�����)
    private JSONArray currentData=new JSONArray();
    public void setCurrentData(JSONArray ja){
    	currentData=ja;
    	return;
    }
    public JSONArray getCurrentData(){
    	return currentData;
    }
    
}
