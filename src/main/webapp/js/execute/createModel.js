var entity;
var ellipsoid = viewer.scene.globe.ellipsoid;
// 通过id找到模型
function findWithId(mid) {
    var myEntitiesArray = new Array();
    myEntitiesArray = viewer.entities.values;
    for (var i = 0; i < myEntitiesArray.length; i++) {
        if (myEntitiesArray[i].id == mid)
            return myEntitiesArray[i];
    }
}
function SetPointColor(pointID, color) {
    var currentPoint = findWithId(pointID);
    // console.log("currentPoint", currentPoint);
    if(typeof currentPoint === 'undefined'){

        // console.log("currentPoint is undefined");
    }else{
        currentPoint.wall.material = Cesium.Color.fromAlpha(color, 0.5);
    }
}
// 将模型翻转180度
function createModel(id, show, Longitude, Latitude, url, height, scale, name,
                     description) {
    var position = Cesium.Cartesian3.fromDegrees(Longitude, Latitude, height);
    var heading = Cesium.Math.toRadians(77.5);
    var pitch = Cesium.Math.toRadians(0);
    var roll = Cesium.Math.toRadians(0);
    var orientation = Cesium.Transforms.headingPitchRollQuaternion(position,
        heading, pitch, roll);

    entity = viewer.entities.add({
        id: id,
        name: name,
        description: description,
        position: position,
        orientation: orientation,
        show: show,
        model: {
            uri: url,
            scale: scale
        }
    });
}
/**
 * 将传感器孔画在对应的位置
 * @param data
 * @param i
 */
function createSensor(data, i) {
    // description = "table to do";
    // console.log(i, data.modelName);
    // console.log();
    // date = DatePicker.val();
    description =
				'<iframe class = "linechart" name = "' + 'CP' + i
        + '" src="newLineChart.html?date=2015-02-28'+'&modelName='+ data.modelName+'&sensorId='+i
        + '" scrolling="no" height="500" width = "1200" sandbox="allow-top-navigation \
					allow-same-origin allow-scripts allow-popups allow-forms" frameborder="0" ></iframe>';
    viewer.entities.add({
        name: '监测孔:' + i,
        id: 'Sensor' + data.modelName + '-' + i,
        position: Cesium.Cartesian3.fromDegrees(parseFloat(data.longitude), parseFloat(data.latitude), parseFloat(data.height)),
        ellipsoid: {
            radii: new Cesium.Cartesian3(0.8, 0.8, 0.8),
            outline: true,
            outlineColor: Cesium.Color.GREY,
            outlineWidth: 2,
            material: Cesium.Color.fromAlpha(Cesium.Color.YELLOW, 0.8)
        },
        description: description
    });
}
function createHoles(data){
    // console.log(data);
    viewer.entities
        .add({
            name: '监测孔:' + data.sensorId + '-传感器:' + data.serialNum,
            id : data.modelName+' CP' + data.sensorId + '-' + data.serialNum,
            wall : {
                // 与i相乘的参数是左右经纬度差值/6
                positions : Cesium.Cartesian3
                    .fromDegreesArray([data.east, data.south, data.west, data.north]),
                maximumHeights : data.maxHeight.split(","),
                minimumHeights : data.minHeight.split(","),
                material : Cesium.Color.fromAlpha(Cesium.Color.GREEN,
                    0.5)
            }
        });
}
var modelName = new Array();
var holesData = null;
/**
 * 根据用户名先调用GetAllModels获得该用户名所对应的所有model的modelName，
 * 再通过这些modelName调用GetAllSensors获得所有modelName对应的所有sensors并展示出来
 * @param userName
 */
function getModels(userName) {
    $.ajax({
        type: "post",
        url: "/jikeng/GetAllModels",
        cache: false,
        dataType: "jsonp", // 跨域请求需要使用jsonp
        data: "",
        jsonp: "jsonpCallback",
        jsonpCallback: "jsonpCallback",
        async: false,
        success: function (data) {
            console.log("got all models!");
            console.log(data);
            for (i = 0; i < data.length; i++) {
                // console.log(10+i, true, parseFloat(data[i].longitude),parseFloat(data[i].latitude), data[i].modelUrl, parseFloat(data[i].height), parseFloat(data[i].scale), data[i].modelName, data[i].description);
                modelName.push(data[i].modelName);
                createModel(10 + i, true, parseFloat(data[i].longitude), parseFloat(data[i].latitude), data[i].modelUrl, parseFloat(data[i].height), parseFloat(data[i].scale), data[i].modelName, data[i].description);
            }
            jsonModelName = JSON.stringify(modelName);
            console.log(jsonModelName);
            $.ajax({
                type: "post",
                url: "/jikeng/GetAllSensors",
                cache: false,
                dataType: "jsonp", // 跨域请求需要使用jsonp
                data: "jsonModelName=" + jsonModelName,
                jsonp: "jsonpCallback",
                jsonpCallback: "jsonpCallback",
                async: false,
                success: function (data) {
                    console.log("got all sensors!");
                    console.log(data);
                    for (i = 0; i < data.length; i++) {
                        createSensor(data[i], data[i].sensorId);
                    }
                },
                error: function () {
                    console.log('Error!');
                }
            });
            $.ajax({
                type: "post",
                url: "/jikeng/GetAllHoles",
                cache: false,
                dataType: "jsonp", // 跨域请求需要使用jsonp
                data: "jsonModelName=" + jsonModelName,
                jsonp: "jsonpCallback",
                jsonpCallback: "jsonpCallback",
                async: false,
                success: function (data) {
                    console.log("got all holes!");
                    console.log(data);
                    holesData = data;
                    for (i = 0; i < data.length; i++) {
                        createHoles(data[i]);
                    }
                },
                error: function () {
                    console.log('Error!');
                }
            });
        },
        error: function () {
            console.log('Error!');
        }
    });
}
// function showCurrentData() {
//     console.log(holesData);
//     for(i=0;i<holesData.length;i++){
//         cid = holesData[i].modelName+' CP' + holesData[i].sensorId + '-' + holesData[i].serialNum;
//         console.log(cid);
//     }
// }
function getAllShowHolesData() {
    console.log(JSON.stringify(modelName));
    var currentDate = sessionStorage.getItem('currentDate');
    if(currentDate==null){
        currentDate = "2014-03-27";
    }
    $.ajax({
        type: "post",
        url: "/jikeng/GetAllShowHolesData",
        cache: false,
        dataType: "jsonp", // 跨域请求需要使用jsonp
        data: "jsonModelName=" + JSON.stringify(modelName)+"&date="+currentDate,
        jsonp: "jsonpCallback",
        jsonpCallback: "jsonpCallback",
        async: false,
        success: function (data) {
            console.log("got all showHolesData!");
            console.log(data);
            for(i = 0;i<data.length;i++){
                myID = data[i].modelName+' CP' + data[i].sensorId + '-' + data[i].serialNum;
                myValue = data[i].value;
                // 根据测得的数据改变每个点的颜色（60是最大值，防溢出处理）
                if (myValue >= 50)
                    SetPointColor(myID, new Cesium.Color(1.0, 0.0, 0.0, 0.5));
                else if(myValue >= 40)
                    SetPointColor(myID, new Cesium.Color(1.0 , 1.0, 0.0, 0.5));
                else if(myValue >= 20)
                    SetPointColor(myID, new Cesium.Color(0.5 , 1.0, 0.0, 0.5));
                else
                    SetPointColor(myID, new Cesium.Color(0.0 , 1.0, 0.0, 0.5));
            }
        },
        error: function () {
            console.log('Error!');
        }
    });

}
// function getCurrentData() {
//     iData = "date="+sessionStorage.getItem("currentDate")+"&modelName="+JSON.stringify(modelName)+"&modelNums=plural";
//     $.ajax({
//         type: "post",
//         url: "/jikeng/GetCurrentData",
//         cache: false,
//         dataType: "jsonp", // 跨域请求需要使用jsonp
//         data: iData,
//         jsonp: "jsonpCallback",
//         jsonpCallback: "jsonpCallback",
//         asnyc: false,
//         success: function (data) {
//             console.log(iData);
//             console.log(data);
//         },
//         error: function () {
//             console.log(iData + 'Error!');
//         }
//     });
// }
// var drawHoles = window.setInterval("showCurrentData()",4000);
var getdata = window.setInterval("getAllShowHolesData()",4000);
// createModel(12345, true, 12.6149828457345, 31.12263393139467, "models/123_out/123.gltf", 10,0.3,"lll","dsfjkl");
getModels("jikeng1");
// createModel(
// 		12,
// 		true,
// 		121.6149828457345,
// 		31.12263393139467,
// 		'models/jikeng/jikengup1.gltf',
// 		18,
// 		0.3,
// 		'基坑',
// 		'<p><h1>Details</h1><a href="http://localhost:8180/?page=Project&poid=2621441&tab=3dview" target="_blank">查看建筑3D模型</a> &nbsp&nbsp &nbsp&nbsp &nbsp&nbsp<a href="http://localhost:8082/bimviews/?page=AddProject" target="_blank">上传建筑3D模型</a> &nbsp&nbsp &nbsp&nbsp <a href="http://localhost:8080/d3_test/" target="_blank">累积模型</a> &nbsp&nbsp &nbsp&nbsp <div showModel>显示模型</div><div style="height:350px"><img src="models/jikeng/jikeng.png"style="height:305px"><div id = "details"></div><h3> </h3> <p> </div>');// <img
// src="images/jikeng.png"style="height:305px">

