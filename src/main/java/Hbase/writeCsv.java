package Hbase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class writeCsv {
	static String file_name = "/home/hadoop/system/jikeng/src/main/webapp/dataSample.xls";

	// sensor���֣�6����
	static String sensorName[] = { "VI. six;;;;;", "V. five;;;;;", "IV. four;;;;;", "III. three;;;;;", "II. two;;;;;", "I. one;;;;;"
			 };

	// dID2Year(��depthIDת��ΪYear�����ɺϸ��csv�ļ�)
	public static int dID2Year(int depthID) {
		return depthID + 1937;
	}

	// д�ļ�
	public static void output(double [][]data,String filename) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename)));			
			try {
				writer.write("name;year;union_budget;resp_budgets;local_budgets;total_budget" + "\n");	
				for (int i = 0; i < 6; i++) {
					writer.write(sensorName[i] + "\n");
					for (int j = 0; j < 15; j++) {
						writer.write(";" + dID2Year(j) + ";-;-;-;" + (int) data[i][j] + ","
								+ String.format("%.1f", data[i][j] - (int) data[i][j]) + "\n");

					}
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
	}

	public static void main(String[] args) {
		double[][] data = new double[6][16];
		try {
			String tableName = "band2";

			// ָ��ʱ���ڼ��ͳ�����ݱ�д��data����
			LinktoHbase.statistics(data, tableName, "2013-10-05", "2013-10-09");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output(data,file_name);	

	}
}
