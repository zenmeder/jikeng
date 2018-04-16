/**
 * 
 */
"use strict"

// day+1
function dayPP(date) {
	var day = date[8] + date[9];
	var iDay = parseInt(day) + 1;
	var sDate = '';
	if (iDay < 10)
		sDate = date.substring(0, 8) + '0' + String(iDay);
	else
		sDate = date.substring(0, 8) + String(iDay);
	//console.log(sDate);
	return sDate;
}