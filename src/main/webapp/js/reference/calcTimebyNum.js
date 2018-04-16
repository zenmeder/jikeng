/**
 * 
 */
"use strict"
//通过0~143的num返回一个 " xx:xx:00"格式的时间
function calcTimebyNum(num) {
	var hour = parseInt(num / 6);
	var min_10 = parseInt(num % 6);
	//字符串格式的hour和min
	var s_hour, s_min;
	
	if (hour < 10)
		s_hour = "0" + String(hour);
	else
		s_hour = String(hour);
	
	s_min=String(min_10)+"0";
	
	var time=" "+s_hour+":"+s_min+":00";
	return time;
}