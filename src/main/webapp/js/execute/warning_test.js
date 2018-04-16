/**
 * 单纯的warning信息测试用文件，当测试完成后可以删除此js文件
 */

var url = "/jikeng/SelectCurrentData";

//参数并不是日期，date只是为了和servlet进行对接而统一的参数命名。由于系统只有一个servlet，系统根据传输过来的参数值来确定服务，这个函数的参数值就写'warning'
function warning_request_test(date) {
	iData = "date=" + date;
	$.ajax({
		type : "post",
		url : url,
		cache : false,
		dataType : "jsonp",
		data : iData,
		jsonp : "jsonpCallback",
		jsonpCallback : "jsonpCallback",
		success : function(data) {
			console.log(data); // 请求成功前台给出提示
		},
		error : function() {
			console.log('Error!');
		}
	});
}

//warning_request_test("warning");
window.setInterval("warning_request_test('warning')", 5002);
