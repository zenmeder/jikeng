package Hbase;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class DatatoJson {
	static void InsertJsonTable(JSONArray jsonTable,String data, String modelName) {
		String mdata = data;
//		System.out.println("mdata is "+mdata);
		JSONObject jsonObject=new JSONObject();
		String time = mdata.substring(0, mdata.indexOf("|"));
		mdata = mdata.substring(mdata.indexOf("|")+1);
		String sensorID = mdata.substring(0, mdata.indexOf(";"));

		mdata = mdata.substring(mdata.indexOf(";")+1);
		String depthID = mdata.substring(0,mdata.indexOf(";"));
		mdata = mdata.substring(mdata.indexOf(";")+1);
		String value =mdata;	
		
		jsonObject.put("time", time);		
		int sensorid=Integer.parseInt(sensorID);
//		switch(sensorid){
//			case 1: sensorid = 5;break;
//			case 2: sensorid = 4;break;
//			case 3: sensorid = 3;break;
//			case 4: sensorid = 2;break;
//			case 5: sensorid = 1;break;
//			case 6:sensorid = 0;break;
//		}
		jsonObject.put("sensorId", sensorid);
		jsonObject.put("depth", Integer.parseInt(depthID));
		jsonObject.put("value", Double.parseDouble(value));
		jsonObject.put("modelName", modelName);
		jsonTable.add(jsonObject);		
	}

	public static void main(String[] args) {
//		JSONArray jsonTable = new JSONArray();
//		InsertJsonTable(jsonTable,"2013-10-05 14:40:00|PZ0002;11;45.83");
//		InsertJsonTable(jsonTable,"2013-10-05 14:40:00|Pz0010;11;5.16");
//		InsertJsonTable(jsonTable,"2014-10-08 15:30:00|pz5;11;3.45");
//		System.out.println("jsonObject��" + jsonTable);
	}
}


