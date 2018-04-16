package Hbase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class stringHandle {
	
	// ��ȡ��ǰ�������������������ʱ�䡪����������������������������������������������������������
	// �ж�time1�Ƿ�����time2���ǣ�true����false
	public static boolean timeCompare(String time1, String time2) {
		int year1 = Integer.parseInt(time1.substring(0, 4));
		int month1 = Integer.parseInt(time1.substring(5, 7));
		int day1 = Integer.parseInt(time1.substring(8, 10));
		int hour1 = Integer.parseInt(time1.substring(11, 13));
		int minute1 = Integer.parseInt(time1.substring(14, 16));
		int year2 = Integer.parseInt(time2.substring(0, 4));
		int month2 = Integer.parseInt(time2.substring(5, 7));
		int day2 = Integer.parseInt(time2.substring(8, 10));
		int hour2 = Integer.parseInt(time2.substring(11, 13));
		int minute2 = Integer.parseInt(time2.substring(14, 16));

		// �ǳ����ĵ��Ǹ�Ч�����ٱ����������ƴ��ȴ�С�ķ�ʽ��Ч�����߼��ж�
		if (year1 > year2)
			return true;
		else if (year1 < year2)
			return false;

		else if (month1 > month2)
			return true;
		else if (month1 < month2)
			return false;

		else if (day1 > day2)
			return true;
		else if (day1 < day2)
			return false;

		else if (hour1 > hour2)
			return true;
		else if (hour1 < hour2)
			return false;

		else if (minute1 > minute2)
			return true;
		else if (minute2 < minute2)
			return false;
		else
			return false;
	}
	
	//�Ƚ�����ʱ���Ƿ����(rowkey�Ǵ�hbase��������rowkey����ʽ��2013-10-05 16:30:00|pz3�� time�Ǳ�׼ʱ�䣬��ʽ��2013-10-05 16:30:00)
	public static boolean compareTime(String rowkey,String time){
		Pattern pattern = Pattern.compile("^"+time+".*");
		Matcher matcher = pattern.matcher(rowkey);
		boolean b= matcher.matches();
		return b;	
	}
	
	public static void main(String[] args){
		System.out.println(compareTime("2013-10-05 16:30:00|pz3","2013-10-05 16:30:00"));
	}

}
