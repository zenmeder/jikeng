package Hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LinktoHbase {

	private static Configuration conf = null;

	static {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "namenode");
	}

	public static Configuration getConf() {
		return conf;
	}

	/**
	 * ?????????
	 * 
	 * @throws IOException
	 */
	public static void addFamily(String tablename, String family) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);

		HTableDescriptor tableDesc = new HTableDescriptor(tablename);
		tableDesc.addFamily(new HColumnDescriptor(family));

		admin.modifyTable(tablename, tableDesc);

		admin.close();
		System.out.println("family?????????");

	}

	/**
	 * ?????????
	 * 
	 * @throws IOException
	 */
	public static void createTable(String tablename, String[] cfs) throws IOException {
		System.out.println("??????" + tablename);
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tablename)) {
			System.out.println("??????????");
			// deleteTable(tablename);
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tablename);
			for (int i = 0; i < cfs.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("?????????");
		}
		admin.close();
	}

	/**
	 * ????????
	 * 
	 * @param tableName
	 */
	public static void insertData(String tableName, String key, String family, String[] cls, String[] values) {
		System.out.println("start insert data ......");
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(key.getBytes());// ???PUT??????????????NEW???PUT????????????,??????????ROWKEY?????rowkey?put?????????????
			for (int i = 0; i < cls.length; i++) {
				put.add(family.getBytes(), cls[i].getBytes(), values[i].getBytes());// ?????????????
			}

			table.put(put);

			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("end insert data ......");
	}

	/**
	 * ????????
	 * 
	 * @param tablename
	 * @throws IOException
	 */
	public static void deleteTable(String tablename) throws IOException {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			admin.disableTable(tablename);
			admin.deleteTable(tablename);
			admin.close();
			System.out.println("??????????");
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ?????????
	 */
	public static void getOneRecord(String tableName, String rowKey) throws IOException {

		HTable table = new HTable(conf, tableName);
		Get get = new Get(Bytes.toBytes(rowKey));
		Result result = table.get(get);
		// ??????
		for (KeyValue rowKV : result.raw()) {
			System.out.print("Row Name: " + new String(rowKV.getRow()) + " ");
			System.out.print("Timestamp: " + rowKV.getTimestamp() + " ");
			System.out.print("column Family: " + new String(rowKV.getFamily()) + " ");
			System.out.print("Row Name:  " + new String(rowKV.getQualifier()) + " ");
			System.out.println("Value: " + new String(rowKV.getValue()) + " ");
		}

		table.close();
	}

	public static String getByKey(String tableName, String rowKey) throws IOException {
		String resultStr = null;
		HTable table = new HTable(conf, tableName);
		Get get = new Get(Bytes.toBytes(rowKey));
		Result result = table.get(get);
		// ??????
		KeyValue[] rows = result.raw();
		if (rows.length > 0) {
			resultStr = new String(rows[0].getValue());
		}
		table.close();
		return resultStr;
	}

	public static void deleteAll(String tableName) throws IOException {
		HTable table = null;
		try {
			table = new HTable(conf, tableName);
			ResultScanner rs = table.getScanner(new Scan());
			for (Result r : rs) {
				deleteRow(tableName, r.getRow());
			}
			System.out.println("delete success!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null)
				table.close();
		}
	}

	/**
	 * ???????????
	 */
	public static void QueryAll(String tableName, int n) throws IOException {
		// HTablePool pool = new HTablePool(conf, 1000);
		HTable table = null;
		try {
			table = new HTable(conf, tableName);
			ResultScanner rs = table.getScanner(new Scan());
			for (Result r : rs) {
				if (n-- < 0)
					break;
//				System.out.println("????rowkey:" + new String(r.getRow()));
				for (KeyValue rowKV : r.raw()) {
					System.out.print("Row Name: " + new String(rowKV.getRow()) + " ");
					System.out.print("Timestamp: " + rowKV.getTimestamp() + " ");
					System.out.print("column Family: " + new String(rowKV.getFamily()) + " ");
					System.out.print("Row Name:  " + new String(rowKV.getQualifier()) + " ");
					System.out.println("Value: " + new String(rowKV.getValue()) + " ");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null)
				table.close();
		}
	}

	public static int getCount(String tableName) throws IOException {
		// HTablePool pool = new HTablePool(conf, 1000);
		int n = 0;
		try {
			HTable table = new HTable(conf, tableName);

			ResultScanner rs = table.getScanner(new Scan());

			for (Result r : rs) {
				n++;
			}

			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return n;
	}

	public static void deleteRow(String tablename, byte[] rowkey) {
		try {
			HTable table = new HTable(conf, tablename);
			List list = new ArrayList();
			Delete d1 = new Delete(rowkey);
			list.add(d1);

			table.delete(list);
			// System.out.println("???????!");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void test1(String[] args) {
		try {
			String tablename = "test_nhl";
			String[] cfs = { "pap", "addr" };
			// createTable(tablename, cfs);
			String[] cls = { "2013??1??1??", "2013??1??2??" };
			String[] values = { "10.254", "25.69" };
			insertData(tablename, "12345", "pap", cls, values);

			insertData(tablename, "15845", "pap", cls, values);

			insertOneRecord(tablename, "15845", "addr", "addr", "?????213??");

			// QueryAll ("test_nhl");
			getOneRecord("test_nhl", "15845");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// QueryAll("addr_13");

	}

	private static void insertOneRecord(String tableName, String row, String family, String column, String value)
			throws IOException {
		System.out.println("start insert data ......");
		HTable table = new HTable(conf, tableName);
		Put put = new Put(Bytes.toBytes(row));
		// ?????????????????
		put.add(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
		table.put(put);

		table.close();
		System.out.println("end insert data ......");
	}

	// ????????? ?? ??????
	public static JSONArray SelectDataonTime(String time, String tableName) throws IOException {
		HTable table = null;
		// ?????????JsonTable
		JSONArray jsonTable = new JSONArray();
		try {
			table = new HTable(conf, tableName);
			Scan myScanner = new Scan();
			byte[] startBytes = time.getBytes("UTF8");
			myScanner.setStartRow(startBytes);
			ResultScanner rs = table.getScanner(myScanner);
			for (Result r : rs) {
				String temp = new String(r.getRow());
				if (!stringHandle.compareTime(temp, time))
					break;
//				System.out.println("????rowkey:" + temp);
				for (KeyValue rowKV : r.raw()) {
					// ?????????????????

					String mTime = new String(rowKV.getRow()) + ";";
					String mDepth = new String(rowKV.getQualifier()) + ";";
					String mValue = new String(rowKV.getValue());
					String mData = mTime + mDepth + mValue;
					DatatoJson.InsertJsonTable(jsonTable, mData);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null)
				table.close();
		}

		return jsonTable;
	}

	// ????????? ?? ??????
	public static JSONArray SelectDatabyTime(String startTime, String endTime, String tableName) throws IOException {
//		System.out.println("endTime:" + endTime);
//		System.out.println("startTime: "+startTime);
//		System.out.println("tableName:"+tableName);
		HTable table = null;
		JSONArray jsonTable = new JSONArray();
		try {
			table = new HTable(conf, tableName);
			Scan myScanner = new Scan();
			byte[] startBytes = startTime.getBytes("UTF8");
			byte[] endBytes = endTime.getBytes("UTF8");
			myScanner.setStartRow(startBytes);
			myScanner.setStopRow(endBytes);
			ResultScanner rs = table.getScanner(myScanner);
			for (Result r : rs) {
				for (KeyValue rowKV : r.raw()) {
					System.out.println(rowKV);
					String mTime = new String(rowKV.getRow()) + ";";
					String mDepth = new String(rowKV.getQualifier()) + ";";
					String mValue = new String(rowKV.getValue());
					String mData = mTime + mDepth + mValue;
					DatatoJson.InsertJsonTable(jsonTable, mData);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null)
				table.close();
		}

		return jsonTable;
	}

	// ???????????????
	public static JSONArray SelectMyCurrentData(String tableName) throws IOException {
		HTable table = null;
		// ?????????JsonTable
		JSONArray jsonTable = new JSONArray();
		// ??????????????????????????
		String TempTime = "0000-00-00 00:00:00";
		try {
			table = new HTable(conf, tableName);
			Scan myScanner = new Scan();
			// ??????
			myScanner.setReversed(true);
			ResultScanner rs = table.getScanner(myScanner);
			for (Result r : rs) {
//				System.out.println("???rowkey:" + new String(r.getRow()));
				for (KeyValue rowKV : r.raw()) {
					String mTime = new String(rowKV.getRow()) + ";";
					// ????????????????????
					if (stringHandle.timeCompare(TempTime, mTime))
						return jsonTable;

					String mDepth = new String(rowKV.getQualifier()) + ";";
					String mValue = new String(rowKV.getValue());
					String mData = mTime + mDepth + mValue;
					//System.out.println(mData);
					DatatoJson.InsertJsonTable(jsonTable, mData);
					TempTime = mTime;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null)
				table.close();
		}
		return jsonTable;
	}
	
	//????????????????????????????????????????
	public static void statistics(double d[][],String tableName, String startdate,String enddate) throws IOException{
		//??????????????????sensorid-depthid???????????
		double [][]data=new double[6][100];
		for(int i=0;i<6;i++)
			for(int j=0;j<100;j++)
				data[i][j]=0.0;
		//int n = 1000;
		HTable table = null;
		// ?????????JsonTable
		JSONArray jsonTable = new JSONArray();
		try {
			table = new HTable(conf, tableName);
			Scan myScanner = new Scan();
			byte[] startBytes = (startdate+" 00:00:00").getBytes("UTF8");
			byte[] endBytes = (enddate+" 23:59:50").getBytes("UTF8");
			myScanner.setStartRow(startBytes);
			myScanner.setStopRow(endBytes);
			ResultScanner rs = table.getScanner(myScanner);
			for (Result r : rs) {		
				for (KeyValue rowKV : r.raw()) {
					String mTime = new String(rowKV.getRow()) + ";";
					String mDepth = new String(rowKV.getQualifier()) + ";";
					String mValue = new String(rowKV.getValue());
					String mData = mTime + mDepth + mValue;
					DatatoJson.InsertJsonTable(jsonTable, mData);	
				}
			}
//			System.out.println("jsonTable: "+jsonTable);
			for(int i=0;i<jsonTable.size();i++){
				JSONObject jo = jsonTable.getJSONObject(i);
				int depthid = Integer.parseInt(jo.getString("depthID").toString());
				int sensorid_tmp=Integer.parseInt(jo.getString("sensorID").toString());
//				System.out.println("startdate is :"+startdate+" sensorid is: "+sensorid_tmp+" depthid is :"+depthid+" value is :"+Double.parseDouble(jo.getString("value").toString()));
				int sensorid = sensorid_tmp;
//				int sensorid=0;
//				switch(sensorid_tmp){
//				case 6:
//					sensorid = 0;
//					break;
//				case 5:
//					sensorid = 1;
//					break;
//				case 4:
//					sensorid = 2;
//					break;
//				case 3:
//					sensorid = 3;
//					break;
//				case 2:
//					sensorid = 4;
//					break;
//				case 1:
//					sensorid = 5;
//					break;
//				}
				double value=Double.parseDouble(jo.getString("value").toString());
				if(0<=depthid && depthid <100){
					data[sensorid][depthid]+=value;
				}
			}
			for(int i=0;i<6;i++)
				for(int j=0;j<100;j++) {
					d[i][j] = data[i][j];
//					System.out.println("data is " + data[i][j] + " d is " + d[i][j]);
				}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null)
				table.close();
		}
	}
	// ??end????????????????????????????????????????????????????????????????????????????????????????????????????????????
	
	//???????????????String?????????
	public static String statistics_text(String tableName, String startdate,String enddate){
		double[][] data = new double[6][100];
		try {
			statistics(data, tableName , startdate , enddate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String out="";
		for(int i=0;i<6;i++){
			for(int j=0;j<100;j++)
				if(j%5 == 0)
					out+=data[i][j]+",";
			out+=";";
		}
//		System.out.println("out is :"+ out);
		return out;
	}
	
	public static void main(String[] args) {

		try {
			String tableName = "band4";
			// String[] cfs = {"info","l"};
			// String tableName = "shipmap_time_prefix";
			// String[] cfs = {"v"};
			// QueryAll(tableName,100);
			// System.out.println(SelectMyCurrentData(tableName));
			// System.out.println(SelectDatabyTime("2013-10-06 00:00:00","2013-10-06 23:59:59","band2"));
			// SelectDatabyTime("2013-10-06 00:00:00","2013-10-06 23:59:59","band2");
			// System.out.println(getCount(tableName));
			// System.out.println(new Date().toString());
			// getOneRecord(tableName, "01Y016458");
			// createTable(tableName, cfs);
			// deleteTable(tableName);
			// HBaseAdmin admin = new HBaseAdmin(conf);
			// deleteAll(ShipDao.TABLE_NAME);
//			System.out.println("helloworld");
//            HBaseAdmin admin = new HBaseAdmin(conf);
//            if (admin.tableExists(tableName)) {
//                System.out.println("??????????");
//                QueryAll(tableName, 100);
//                System.out.println("?????chadao??");
//            }
//            System.out.println("great");

			System.out.println(SelectMyCurrentData(tableName));
			//System.out.println(statistics_text("band2", "2013-10-05", "2013-10-07"));
			//System.out.println(SelectDataonTime("2013-10-05 16:30:00", tableName));			
			//?????????????????????data????
			/*
			double [][]data=new double[6][16];
			statistics(data,tableName,"2013-10-05","2013-10-10");
			
			for(int i=0;i<6;i++)
				for(int j=0;j<16;j++)
					System.out.println(data[i][j]);								
			*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("wrong");

			e.printStackTrace();
		}

	}
}
