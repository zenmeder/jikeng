/**
 * 
 */
"use strict"
Array.prototype.in_array = function(e)
{
	for(i=0;i<this.length;i++)
	{
		if(this[i] == e)
			return true;
	}
	return false;
}



window.entityID = [];
// 一面墙上传感器的数量
var JiKengSensors = 6;
// 每个传感器测量的深度值个数
var JiKengDepth = 15;
for (var i = 0; i < JiKengSensors; i++)
	for (var j = 0; j < JiKengDepth; j++) {
		window.entityID.push('CP' + i + '-' + j);
		var description = '';
		if (j == 0)
		{
			description = '<style>.cesium-infoBox-description {text-align:center;}</style>\
				<iframe date = "jik" name = "' + 'CP' + i + '-' + j 
					+ '" src="linechart.html?_v=' + new Date().getTime() 
					+ '" scrolling="no" height="260" width = "100%" sandbox="allow-top-navigation \
					allow-same-origin allow-scripts allow-popups allow-forms" frameborder="0" ></iframe>';
			
			//最上面一排传感器绘制标识
			viewer.entities.add({
				name: '监测孔:'+ (i + 1),
				id:'CP'+ i,
			    position : Cesium.Cartesian3.fromDegrees(121.614983531255533+ i * 0.000012782699573203141 + 0.0000063913497866, 
			    		31.122592361610856 - i * 0.00005276838845524878 - 0.0000263841942276, 18.0),
			    ellipsoid : {
			        radii : new Cesium.Cartesian3(0.8, 0.8, 0.8),
			        outline : true,

			        outlineColor : Cesium.Color.GREY,
			        outlineWidth : 2,
			        material : Cesium.Color.fromAlpha(Cesium.Color.YELLOW,0.8)
			    },
			    description: description
			});
		}
		viewer.entities
				.add({
					name: '监测孔:' + i + '-传感器:' + j,
					id : 'CP' + i + '-' + j,
					wall : {
						// 与i相乘的参数是左右经纬度差值/6
						positions : Cesium.Cartesian3
								.fromDegreesArray([
										121.614983531255533 + i * 0.000012782699573203141,
										31.122592361610856 - i * 0.00005276838845524878,
										121.614983531255533 + (i + 1) * 0.000012782699573203141,
										31.122592361610856 - (i + 1) * 0.00005276838845524878 ]),
						maximumHeights : [ 18.0 - j * 1.2, 18.0 - j * 1.2 ],
						minimumHeights : [ 18.0 - (j + 1) * 1.2,
								18.0 - (j + 1) * 1.2 ],
						material : Cesium.Color.fromAlpha(Cesium.Color.GREEN,
								0.5)		
					}
					// description: '<iframe src="linechart.html" height="260" scrolling="no" frameborder="0" seamless></iframe>'		 allow-forms			
					//description: description
					// description: '<div id = "sbsb">GHJK</div>'
				});
	}

// 设置某个监测点的color
function SetPointColor(pointID, color) {
	var currentPoint = findWithId(pointID);
	// console.log("currentPoint", currentPoint);
	if(typeof currentPoint === 'undefined'){

		// console.log("currentPoint is undefined");
	}else{
        currentPoint.wall.material = Cesium.Color.fromAlpha(color, 0.5);
	}
}

console.log("viewer:", viewer);
//var scene = viewer.scene;
var labelEntity = viewer.entities.add({
     label : {
         show : false//,
         // horizontalOrigin : Cesium.HorizontalOrigin.LEFT
     }
 });
var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
handler.setInputAction(function(movement) {
	var foundPosition = false;
 var scene = viewer.scene;
 // console.log("scene:%o\nendPosition.x:%o, nendPosition.y:%o", scene, movement.endPosition.x, movement.endPosition.y);
 var pickedObject = scene.pick(movement.endPosition);
 if (pickedObject && scene.pickPositionSupported)// && pickedObject.id == Entity)
 {
 	console.log("pickedObject:%o", pickedObject.id._id);
 	var id = pickedObject.id._id;
 	if (typeof(id) != typeof("sb") && id.indexOf('CP')==-1)
 	{
 		console.log("not match");
 		if ($("#dsb"))
			{
				console.log("dsb is not null");
				$("#dsb").remove();
			}
 		return;
 	}
 	else
 	{    		
 		console.log("match");
 		var valueToShow = "没有监测数据";
			if (2 == switchBetweenAutoAndAccumulate)
			{
				// console.log("cartesian:", myData);
				if (!myData)
				{
					return;
				}

				valueToShow = "当前偏移量："  + Math.ceil(parseFloat(value_HTC(id)));
			}
			else
			{
             if (3 == switchBetweenAutoAndAccumulate)
             {
				    if (typeof(usefulData) == "undefined")
                 {
					    return;
				    }
				    // console.log("usefulData:", usefulData);
				    valueToShow = "累积偏移量：" + usefulData[parseInt(id[2])][parseInt(id[4])].summary
             }
             else
             {
                 if (1 == switchBetweenAutoAndAccumulate)
                 {
                     if (!myData)
                     {
                         return;
                     }
                     console.log("MyData:", myData);
                     console.log("id is:", id);
                     var depth = parseInt(id.split("-")[1]);
                     var sensorID = parseInt(id.split("-")[0][2]) + 1;
                     console.log("depth is "+depth+" sensorId is "+sensorID);
                     myData.forEach(function(item){
                         var d = parseInt(item.depthID);
                         if (d != depth)
                         {
                             // console.log("depth:%o != d:%o", depth, d);
                         }
                         else
                         {
                             var sid = parseInt(item.sensorID);
                             if (sid != sensorID)
                             {
                                console.log("sensorID:%o != sid:%o", sensorID, sid);
                             }
                             else
                             {
                             	console.log("valuetoshow"+typeof item.value);
                                 valueToShow = "实时偏移量：" + item.value;
                                 return;
                             }
                         }
                     });
                 }
             }
			}

 		if ($("#dsb"))
			{
				console.log("dsb is not null");
				$("#dsb").remove();
			}
 		$("<div id = 'dsb'></div>").appendTo($("body"));
 		$("#dsb").css({
				"width": "15%",
				"height": "10%",
				"top": movement.endPosition.y + 5,
				"left": movement.endPosition.x + 5,
				"background-color": "white",
				"position": "absolute",
				"padding-left": "10px"
				// "text-align":"center"
			});
 		console.log("id is "+id);
			$("<div></div>").text("测孔编号：" + (parseInt(id.split("-")[0][2]) + 1)).appendTo($("#dsb"));
			$("<div></div>").text("传感器：" + id.split("-")[1]).appendTo($("#dsb"));
			$("<div></div>").text(valueToShow).appendTo($("#dsb"));
			// $("#dsb").text("传感器：" + id.split("-")[0][2]+ "深度：" + id.split("-")[1] + "" + valueToShow);
			$("#dsb").show();
 	}
 }
 else
 {
 	if ($("#dsb"))
		{
			console.log("dsb is not null");
			$("#dsb").remove();
		}
 }
 
 if (!foundPosition) {
     // labelEntity.label.show = false;
 }
}, Cesium.ScreenSpaceEventType.MOUSE_MOVE);


// 显示实时数据(band4)
function showCurrentData() {
	// 所有数据清空（没有测到数据的点呈白色）
	for (var i = 0; i < JiKengSensors; i++)
		for (var j = 0; j < JiKengDepth; j++)
			SetPointColor('CP' + i + '-' + j, Cesium.Color.WHITE);
	var nowID = 0;
	while (myData[nowID] != null) {
		// sensorID和depthID调整
		var mSensorID = Number(myData[nowID].sensorID);
		var mDepthID = myData[nowID].depthID - 1;
		var myID = 'CP' + mSensorID + '-' + mDepthID;
		var myValue = Number(myData[nowID].value);
		// console.log(myID + "=" + myValue);
        // console.log("msensorid is "+mSensorID+" mdepthid is "+mDepthID+" mvalue is "+myValue);

        // 根据测得的数据改变每个点的颜色（60是最大值，防溢出处理）
		if (myValue >= 5)
			SetPointColor(myID, new Cesium.Color(1.0, 0.0, 0.0, 0.5));
		else if(myValue >= 4)
			SetPointColor(myID, new Cesium.Color(1.0 , 1.0, 0.0, 0.5));
		else if(myValue >= 2)
			SetPointColor(myID, new Cesium.Color(0.5 , 1.0, 0.0, 0.5));
		else 
			SetPointColor(myID, new Cesium.Color(0.0 , 1.0, 0.0, 0.5));
		nowID++;
	}
}

// 显示历史数据
function showHistoryData(time) {
	// 所有数据清空（没有测到数据的点呈白色）
	for (var i = 0; i < JiKengSensors; i++)
		for (var j = 0; j < JiKengDepth; j++)
			SetPointColor('CP' + i + '-' + j, Cesium.Color.WHITE);

	var nowID = 0;
	while (myData[nowID] != null) {

		// 找到指定时间的数据
		if (myData[nowID].time != time) {
			nowID++;
			continue;
		}
		console.log("my data length is :", myData.length);
		// sensorID和depthID调整
		var mSensorID = Number(myData[nowID].sensorID);
		var mDepthID = myData[nowID].depthID - 1;
		var myID = 'CP' + mSensorID + '-' + mDepthID;
		var myValue = Number(myData[nowID].value);
		// 根据测得的数据改变每个点的颜色（60是最大值，防溢出处理）
		if (myValue >= 50)
			SetPointColor(myID, new Cesium.Color(1.0, 0.0, 0.0, 0.5));
		else if(myValue >= 40)
			SetPointColor(myID, new Cesium.Color(1.0 , 1.0, 0.0, 0.5));
		else if(myValue >= 20)
			SetPointColor(myID, new Cesium.Color(0.5 , 1.0, 0.0, 0.5));
		else 
			SetPointColor(myID, new Cesium.Color(0.0 , 1.0, 0.0, 0.5));
		nowID++;
	}
}

// 每3100ms重新绘制一次数据
var DrawControl = window.setInterval("showCurrentData()", 3100);
// 默认对于绘制的请求是停止状态
//window.clearInterval(DrawControl);

// 每隔一定时间请求一次CurrentData
var nowFunc = window.setInterval("sendDateBack('now')", 3000);
// 默认对于CurrentData的请求是停止状态
//window.clearInterval(nowFunc);
