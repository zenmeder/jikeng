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
    description = "table to do";
    // console.log(i, data.longitude,data.latitude, data.height);
    viewer.entities.add({
        name: '监测孔:' + (i + 1),
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
var modelName = new Array();
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
                        createSensor(data[i], i);
                    }
                },
                error: function () {
                    console.log('Error!');
                }
            });
            // for(i=0;i<modelName.length;i++){
            // console.log("modelName is "+modelName[i]);
            //    $.ajax({
            //        type : "post",
            //        url : "/jikeng/GetAllSensors",
            //        cache : false,
            //        dataType : "jsonp", // 跨域请求需要使用jsonp
            //        data : "modelName="+modelName[i],
            //        jsonp : "jsonpCallback",
            //        jsonpCallback : "jsonpCallback",
            //        async : false,
            //        success : function(data) {
            //            console.log("got all sensors!");
            //            console.log(data);
            // 			for(i = 0;i<data.length;i++){
            //                createSensor(data[i], i);
            // 			}
            //        },
            //        error : function() {
            //            console.log('Error!');
            //        }
            //    });
            // }
        },
        error: function () {
            console.log('Error!');
        }
    });
}

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

