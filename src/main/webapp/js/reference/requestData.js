/**
 * 
 */
var url = "/jikeng/SelectCurrentData";
var myData; // 用于保存返回的数据（jsonArray格式）
var useHistory = 1; // 是否使用历史数据
var myDate; // 保存当前选择的日期

//请求指定日期的数据并传回前端
function sendDateBack(date) {
	// var date='2015-5-15';
	iData = "date=" + date;
	$.ajax({
		type : "post",
		url : url,
		cache : false,
		/*
		 * dataType:"text", data:iData, success:function(){
		 * console.log("Success!"); },
		 */
		dataType : "jsonp", // 跨域请求需要使用jsonp
		data : iData,
		jsonp : "jsonpCallback",
		jsonpCallback : "jsonpCallback",
		success : function(data) {
			console.log(data); // 请求成功前台给出提示
			var totalNum = data.length;
			myData = data;
		},
		error : function() {
			console.log('Error!');
		}
	});
}

//请求到指定日期的累计数据并传回前端
function calTHC(){
	if(myDate==null)
	{		
		return;
	}
	calTHC_t(myDate);
}
function calTHC_t(date){
	//前缀是"calTHC:"
	console.log("MyDate is null");
	iData= "date="+"calTHC:"+date;
	$.ajax({
		type:"post",
		url:url,
		cache:false,
		dataType:"text",
		data:iData,
		success:function(data){
			// console.log(data);
			var lines = data.split(";");
			var usefulData = {};
			var sensorID = 0;
			lines.forEach(function(item)
				{
					usefulData[sensorID] = [];
					var valueArray = item.split(",");
					var depth = 0;
					valueArray.forEach(function(value)
						{
							var object = {};
							object['depth'] = depth + 1;
							object['summary'] = parseInt(value.split(".")[0]);
							usefulData[sensorID].push(object);
							++depth;
						});
					++sensorID;
				});
			console.log("usefulData:", usefulData);
			window.usefulData = usefulData;
			// show();
		},
		error: function(){
			console.log('THCError!');
		}
	
	});
}

