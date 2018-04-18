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
		id : id,
		name : name,
		description : description,
		position : position,
		orientation : orientation,
		show : show,
		model : {
			uri : url,
			scale : scale
		}
	});
}
function getModels(userName) {
    $.ajax({
        type : "post",
        url : "/jikeng/GetAllModels",
        cache : false,
        dataType : "jsonp", // 跨域请求需要使用jsonp
        data : "",
        jsonp : "jsonpCallback",
        jsonpCallback : "jsonpCallback",
        success : function(data) {
        	console.log("got all models!");
            for(i=0;i<data.length;i++){
            	// console.log(10+i, true, parseFloat(data[i].longitude),parseFloat(data[i].latitude), data[i].modelUrl, parseFloat(data[i].height), parseFloat(data[i].scale), data[i].modelName, data[i].description);
            	createModel(10+i, true, parseFloat(data[i].longitude),parseFloat(data[i].latitude), data[i].modelUrl, parseFloat(data[i].height), parseFloat(data[i].scale), data[i].modelName, data[i].description);
			}
        },
        error : function() {
            console.log('Error!');
        }
    });
}
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

