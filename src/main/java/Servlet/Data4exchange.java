//这个类专门用于不同servlet之间的信息交换
package Servlet;

import net.sf.json.JSONArray;

//单例模式
public class Data4exchange {
    private Data4exchange() {}  
    private static Data4exchange instance=null;  
    //静态工厂方法   
    public static Data4exchange getInstance() {  
         if (instance == null) {    
             instance = new Data4exchange();  
         }    
        return instance;  
    }  
    
    //保存currentData(当前最新的数据)
    private JSONArray currentData=new JSONArray();
    public void setCurrentData(JSONArray ja){
    	currentData=ja;
    	return;
    }
    public JSONArray getCurrentData(){
    	return currentData;
    }
    
}
