"use strict"

// var SandcastleGUI = document.createElement("DIV");
// SandcastleGUI.setAttribute("id", "toolbar");
// document.body.appendChild(SandcastleGUI);
var SandcastleGUI = $("<div id = 'toolbar'></div>")
.css({
    "padding-left": "3px"
    });//.setAttribute("id", "toolbar");
SandcastleGUI.appendTo($("body"));
var backToJK = $("<button id = 'backToJK'>回到基坑</button>").addClass("cesium-button")
.css({
    "margin-left": "10px",
    "width": "145px",
    "background-color": "rgba(0, 0, 0, 0.3)"
    }).appendTo(SandcastleGUI);
backToJK.on("click", function(){
    $("#font-id").css({"color": "black"});
	viewer.camera.flyTo({
		destination : new Cesium.Cartesian3.fromDegrees(121.614127541442571,
				31.122189347505586, 30.0),
		orientation : {
			direction : new Cesium.Cartesian3(-0.4439097425797499,
					-0.8827963025381418, 0.15367117058102756),
			up : new Cesium.Cartesian3(-0.6577530259363744, 0.437483168692811,
					0.6131634643241887)
		}
	});
});
$("<br />").appendTo(SandcastleGUI);

var realtime = $("<button id = 'real-time'>实时数据</button>").addClass("cesium-button")
.css({
    "margin-left": "10px",
    "width": "145px",
    "background-color": "rgba(0, 0, 0, 0.3)"
    }).appendTo(SandcastleGUI);
realtime.on("click", function(){
    switchBetweenAutoAndAccumulate = 1;
    myDate = "now";
	// 每隔5000ms向服务器请求一次当前CurrentData，并将数据写入mydata（jsonArray）变量中
	nowFunc = window.setInterval("sendDateBack('now')", 5000);
	// View data repeatedly per 5001 ms
	DrawControl = window.setInterval("showCurrentData()", 5001);
	useHistory = 0;
});
$("<br />").appendTo(SandcastleGUI);

var historyBox = $("<div id = 'history-box'><div>").appendTo(SandcastleGUI);
historyBox.css({
    "border-radius": "6px",
    "border": "3px solid",
    "width": "151px",
    "padding-left": "3px",
    "padding-right": "3px"
});

//date picker
//when selected date is changed
function log_c(){
	myDate = DatePicker.val();
	console.log(myDate);

	window.clearInterval(DrawControl);
	window.clearInterval(nowFunc);
	sendDateBack(myDate);
	useHistory = 1;
}
var DatePicker_div = $("<div id='datepicker_div'></div>").css({
	"top" : "40px",
	"left" : "150px",
	"position" : "absolute"
});
//2013-10-05 is default
//var DatePicker = $("<input type='date' id='datepicker' value='2013-10-05' min='2013-10-05' max='2013-10-10' onchange='log_c()'/>");
var DatePicker = $("<input type='date' id='datepicker' value='2015-02-28' onchange='log_c()'/>");
DatePicker.appendTo($("#history-box"));
// DatePicker_div.appendTo($("body"));


//var historyDateSelection = $("<select></select>").attr("id", "HistoryDateSelection").addClass("cesium-button")
//.css({
//    "width": "145px",
//    "background-color": "rgba(0, 0, 0, 0.3)"
//    }).appendTo(historyBox);
////var option1 = $("<option selected = 'selected'>实时数据</option>").appendTo(historyDateSelection);
//var option1 = $("<option>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2013-10-05</option>").appendTo(historyDateSelection);
//var option2 = $("<option>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2013-10-06</option>").appendTo(historyDateSelection);
//var option3 = $("<option>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2013-10-07</option>").appendTo(historyDateSelection);
//var option4 = $("<option>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2013-10-08</option>").appendTo(historyDateSelection);
//var option5 = $("<option>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2013-10-09</option>").appendTo(historyDateSelection);
//var option6 = $("<option>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2013-10-10</option>").appendTo(historyDateSelection);
//var option0 = $("<option selected = 'selected'>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspNONE</option>").appendTo(historyDateSelection);
//$("#HistoryDateSelection").change(function(){
//	handleSelectChange();
//});
//function handleSelectChange()
//{
//	var selectedText = $.trim($("#HistoryDateSelection option:selected").text());
//	console.log("selected:", selectedText);
//
//	if (!selectedText)
//	{
//		return;
//	}
//	else
//	{
//		if ("NONE" == selectedText)
//		{
//			window.clearInterval(nowFunc);
//			useHistory = 1;
//		}
//		else
//		{
//            /*
//			if ("实时数据" == selectedText)
//			{
//				myDate = "now";
//				// 每隔5000ms向服务器请求一次当前CurrentData，并将数据写入mydata（jsonArray）变量中
//				nowFunc = window.setInterval("sendDateBack('now')", 5000);
//				// View data repeatedly per 5001 ms
//				DrawControl = window.setInterval("showCurrentData()", 5001);
//				useHistory = 0;
//			}
//			else
//			{
//            */
//                switchBetweenAutoAndAccumulate = 2;
//				// 保存当前日期
//				myDate = selectedText;
//				// 停止请求CurrentData
//				window.clearInterval(DrawControl);
//				window.clearInterval(nowFunc);
//				// 请求selectedText当天的历史数据
//				sendDateBack(selectedText);
//				// 使用历史数据
//				useHistory = 1;
//			//}
//		}
//	}
//}

$("<br />").appendTo(historyBox);



// "BacktoHome" Button
/*Sandcastle.addToolbarButton('回到基坑', function() {
	viewer.camera.flyTo({
		destination : new Cesium.Cartesian3.fromDegrees(121.614127541442571,
				31.122189347505586, 30.0),
		orientation : {
			direction : new Cesium.Cartesian3(-0.4439097425797499,
					-0.8827963025381418, 0.15367117058102756),
			up : new Cesium.Cartesian3(-0.6577530259363744, 0.437483168692811,
					0.6131634643241887)
		}
	});

});*/
/*
// "Select Date" ToolbarMenu
Sandcastle.addToolbarMenu([ {
	text : 'NONE',
	onselect : function() {
		window.clearInterval(nowFunc);
		useHistory = 1;
	}
}, {
	text : '2013-10-05',
	onselect : function() {
		// 保存当前日期
		myDate = "2013-10-05";
		// 停止请求CurrentData
		window.clearInterval(nowFunc);
		// 请求2013-10-05当天的历史数据
		sendDateBack('2013-10-05');
		// 使用历史数据
		useHistory = 1;
	}
}, {
	text : '2013-10-06',
	onselect : function() {
		myDate = "2013-10-06";
		window.clearInterval(nowFunc);
		sendDateBack('2013-10-06');
		useHistory = 1;
	}
}, {
	text : '2013-10-07',
	onselect : function() {
		myDate = "2013-10-07";
		window.clearInterval(DrawControl);
		window.clearInterval(nowFunc);
		sendDateBack('2013-10-07');
		useHistory = 1;
	}
}, {
	text : '2013-10-08',
	onselect : function() {
		myDate = "2013-10-08";
		window.clearInterval(DrawControl);
		window.clearInterval(nowFunc);
		sendDateBack('2013-10-08');
		useHistory = 1;
	}
}, {
	text : '2013-10-09',
	onselect : function() {
		myDate = "2013-10-09";
		window.clearInterval(DrawControl);
		window.clearInterval(nowFunc);
		sendDateBack('2013-10-09');
		useHistory = 1;
	}
}, {
	text : '2013-10-10',
	onselect : function() {
		myDate = "2013-10-10";
		window.clearInterval(DrawControl);
		window.clearInterval(nowFunc);
		sendDateBack('2013-10-10');
		useHistory = 1;
	}
}, {
	text : '实时数据',
	onselect : function() {
		myDate = "now";
		// 每隔5000ms向服务器请求一次当前CurrentData，并将数据写入mydata（jsonArray）变量中
		nowFunc = window.setInterval("sendDateBack('now')", 5000);
		// View data repeatedly per 5001 ms
		DrawControl = window.setInterval("showCurrentData()", 5001);
		useHistory = 0;
	}
} ], 'toolbar');
*/
// ~Toolbar for time selection
function HistoryDataUpdate() {
	var selectedTime = calcTimebyNum(SelectTimeToolbar.value);
	showHistoryData(myDate + selectedTime);
	SelectedTimeDisplay.value = myDate + selectedTime;
}


//给定ID（"CP sensorid - depthID"）去找到对应的value（当前选定的历史时间下的value）
function value_HTC(id) {
    var selectedTime = calcTimebyNum(SelectTimeToolbar.value);
    var time = myDate + selectedTime;
    var index = 0;
    var value = 0;
    var flag = false;
    while (myData[index] != null) {
        //如果已经找到目标
        if (flag)break;
        // 找到指定时间的数据
        if (myData[index].time != time) {
            index++;
            continue;
        }
        var srcDepthID = Number(id.substr(4)) + 1;
        if (myData[index].depthID != srcDepthID) {
            index++;
            continue;
        }

        var srcSensorID = Number(id.substr(2, 1));
        /*
        switch (srcSensorID) {
            case 0:
                srcSensorID = 2;
                break;
            case 1:
                srcSensorID = 3;
                break;
            case 2:
                srcSensorID = 4;
                break;
            case 3:
                srcSensorID = 5;
                break;
            case 4:
                srcSensorID = 9;
                break;
            case 5:
                srcSensorID = 10;
                break;
        }
        */
        if (myData[index].sensorID != srcSensorID) {
            index++;
            continue;
        }
        flag = true;
        value = myData[index].value;
    }
    console.log(time + "——" + value);
    return value;
}
//window.setInterval("value_THC('CP3-13')", 5000);

// <form>
var SelectTimeForm = document.createElement("FORM");
SelectTimeForm.setAttribute("oninput", "HistoryDataUpdate()");
SelectTimeForm.setAttribute("style", "padding-left:3px");
// <input>
var SelectTimeToolbar = document.createElement("INPUT");
SelectTimeToolbar.setAttribute("name", "SelectTimeToolbar");
SelectTimeToolbar.setAttribute("type", "range");
SelectTimeToolbar.setAttribute("style", "width:145px");
SelectTimeToolbar.setAttribute("max", 143);
SelectTimeToolbar.setAttribute("min", 0);
SelectTimeToolbar.setAttribute("step", 1);
var brbr = document.createElement("br");
// <output>
var SelectedTimeDisplay = document.createElement("OUTPUT");
SelectedTimeDisplay.setAttribute("name", "SelectedTimeDisplay");
SelectedTimeDisplay.setAttribute("style", "width:145px");
// append
SelectTimeForm.appendChild(SelectTimeToolbar);
SelectTimeForm.appendChild(brbr);
SelectTimeForm.appendChild(SelectedTimeDisplay);
// document.body.appendChild(SelectTimeForm);
// SandcastleGUI.appendChild($(SelectTimeForm));
//$(SelectTimeForm).appendTo(SandcastleGUI);
$(SelectTimeForm).appendTo(historyBox);

// ~ON-OFF to Viewing History data
var ON_OFFtoScroll = window.setInterval("", 1000);
// Avoid repetition of "setInterval"
var flag160325 = false;
// 3: Accumulate, 2: Auto, 1: Real-Time
var switchBetweenAutoAndAccumulate = 1;
window.clearInterval(ON_OFFtoScroll);
// Auto viewing speed
var speed16328 = 500;
var autoScroll = $("<button>重播</button>").addClass("cesium-button")
.css({
    "width": "145px",
    "background-color": "rgba(0, 0, 0, 0.3)"
    }).appendTo(historyBox);
autoScroll.on("click", function(e){
	switchBetweenAutoAndAccumulate = 2;
					if (flag160325 == false) {
						ON_OFFtoScroll = window
								.setInterval(
										"if(SelectTimeToolbar.value<143) {SelectTimeToolbar.value++;HistoryDataUpdate();}"
												+ "else {myDate = dayPP(myDate); sendDateBack('2013-10-06'); SelectTimeToolbar.value=0; HistoryDataUpdate();}",
										speed16328);
						flag160325 = true;
						console.log(speed16328);
					} else {
						// console.log("jsljflwj!!!!");					
					}
});
$("<br />").appendTo(historyBox);
/*Sandcastle.addToolbarButton(
				'自动滚动',
				function() {
					switchBetweenAutoAndAccumulate = 2;
					if (flag160325 == false) {
						ON_OFFtoScroll = window
								.setInterval(
										"if(SelectTimeToolbar.value<143) {SelectTimeToolbar.value++;HistoryDataUpdate();}"
												+ "else {myDate = dayPP(myDate); sendDateBack('2013-10-06'); SelectTimeToolbar.value=0; HistoryDataUpdate();}",
										speed16328);
						flag160325 = true;
						console.log(speed16328);
					} else {
						// console.log("jsljflwj!!!!");					
					}
				});
*/
var pause = $("<button>暂停</button>").addClass("cesium-button")
.css({
    "width": "145px",
    "background-color": "rgba(0, 0, 0, 0.3)"
    }).appendTo(historyBox);
pause.on("click", function(e){
	window.clearInterval(ON_OFFtoScroll);
	flag160325 = false;
	speed16328 = 500;
});
$("<br />").appendTo(historyBox);
/*Sandcastle.addToolbarButton('暂停', function() {
	window.clearInterval(ON_OFFtoScroll);
	flag160325 = false;
	speed16328 = 500;
});
*/
function showSummary(myDate)
{
	if ($("#summary-panel"))
	{
		$("#summary-panel").remove();
	}
	var summaryPanel = $("<div><button id = 'close'>close</button></div>").attr("id", "summary-panel");
	summaryPanel.css({
		"width": "50%",
		"height": "50%",
		"top": "10%",
		"left": "10%",
		"position": "absolute",
		"background-color": "white"
	});
	summaryPanel.appendTo($("body"));
	$("#close").on('click', function(event) {
		// event.preventDefault();
		/* Act on the event */
		$("#summary-panel").remove();
	});
	var iframeContent = $('<iframe src = "http://211.152.38.103:8080/JK_feixun/accumulation/index.html?date='
									 + myDate + '" scrolling="no" height="100%" width = "100%"></iframe>');
	iframeContent.appendTo(summaryPanel);
}


var accumulate = $("<button>生成累积图</button>").addClass("cesium-button")
.css({
    "width": "145px",
    "background-color": "rgba(0, 0, 0, 0.3)"
    }).appendTo(historyBox);
accumulate.on("click", function(e){
	sendDateBack('cum'+myDate);
	switchBetweenAutoAndAccumulate = 3;
	setTimeout(calTHC, 2000);
	// setTimeout(showSummary, 1000, myDate);
	setTimeout('window.open("http://211.152.38.103:8080/JK_feixun/accumulation/index.html?date=" + myDate,"","width=800,height=600")', 1000);	
});
/*Sandcastle.addToolbarButton('累积', function(){
	sendDateBack('cum'+myDate);
	switchBetweenAutoAndAccumulate = 3;
	setTimeout(calTHC, 2000);
	// setTimeout(showSummary, 1000, myDate);
	setTimeout('window.open("http://211.152.38.103:8080/JK_feixun/accumulation/index.html?date=" + myDate,"","width=800,height=600")', 1000);	
});
*/
/*......
var m_buttons=$(".cesium-button");
console.log("m_buttons",m_buttons);
//....
$(m_buttons[9]).css({
        "top":"29px",
            "left":"82px",
                "position":"absolute"
});

//....
$(m_buttons[10]).css({
        "top":"95px",
            "left":"0px",
                "position":"absolute"
});
//..
$(m_buttons[11]).css({
        "top":"95px",
            "left":"82px",
                "position":"absolute"
});
//..
$(m_buttons[12]).css({
        "top":"128px",
            "left":"0px",
                "position":"absolute"
});

//.....
var m_scrollBar=$("input");
//console.log(m_scrollBar);
$(m_scrollBar[1]).css({
        "top":"60px",
            "left":"0px",
                "position":"absolute"
});
//.........
var m_sBarDisplay=$("output");
//console.log(m_sBarDisplay);
m_sBarDisplay.css({
        "top":"60px",
            "left":"140px",
                "position":"absolute"
});
*/
