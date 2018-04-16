/**
 * Created by Cat on 2016/5/31.
 */

"use strict";

//坐标轴长度
var axisLength = 1000.0;
//坐标点的半径
var pointRadius = axisLength / 100.0;
//坐标点的高度变化幅度
var pointYscale = 100.0;
//绘制坐标轴
function initAxis() {
    var geometryLine = new THREE.Geometry();
    var materialLine = new THREE.LineBasicMaterial({
        color: 0x0000ff
    });
    geometryLine.vertices.push(
        new THREE.Vector3(0, 0, axisLength),
        new THREE.Vector3(0, 0, 0),
        new THREE.Vector3(axisLength, 0, 0)
    );
    var line = new THREE.Line(geometryLine, materialLine);
    scene.add(line);
}

//绘制坐标轴上的点
function initAxisPoint() {
    var geoPoint = new THREE.SphereGeometry(pointRadius, 32, 32);
    var matPoint = new THREE.MeshBasicMaterial({color: 0x000000});
    //绘制x轴上的点
    var sphereX = new Array();
    for (var i = 0; i < 6; i++) {
        sphereX[i] = new THREE.Mesh(geoPoint, matPoint);
        sphereX[i].position.set(axisLength * (i + 1.0) / 6.0, 0, 0);
        scene.add(sphereX[i]);
    }

    //绘制z轴上的点
    var sphereZ = new Array();
    for (var i = 0; i < 15; i++) {
        sphereZ[i] = new THREE.Mesh(geoPoint, matPoint);
        sphereZ[i].position.set(0.0, 0.0, axisLength * (i + 1.0) / 15.0);
        scene.add(sphereZ[i]);
    }

}

//绘制场景中的文字和标识
function initlogo(){
    var cube = new THREE.Mesh(new THREE.CubeGeometry(1000,100,100), new THREE.MeshBasicMaterial({color: 0x00ffff}));
    cube.position.set(axisLength/2.0, 0,axisLength + 100.0);
    scene.add(cube);

    var triangle =  new THREE.Mesh(new THREE.TetrahedronGeometry(70),new THREE.MeshBasicMaterial({color: 0xff00ff}));
    triangle.position.set(axisLength/2.0, 0, 0 - 60.0);
    scene.add(triangle);
}

//绘制坐标点
/*
var mydata = [[1000.0, 2000.0, 3000.0, 12000.0, 0.0, 0.0, 10.0, 5000.0, 9000.0, 0.0, 50.0, 0.0, 0.0, 0.0, 0.0],
    [1000.0, 20000.0, 3000.0, 1200.0, 0.0, 0.0, 10.0, 5000.0, 9000.0, 0.0, 50.0, 0.0, 0.0, 0.0, 0.0],
    [1000.0, 2000.0, 3000.0, 1200.0, 0.0, 50.0, 0.0, 10.0, 5000.0, 9000.0, 0.0, 0.0, 0.0, 0.0, 0.0],
    [1000.0, 2000.0, 3000.0, 9000.0, 12000.0, 0.0, 0.0, 10.0, 5000.0, 0.0, 50.0, 0.0, 0.0, 0.0, 0.0],
    [1000.0, 20000.0, 3000.0, 12000.0, 0.0, 0.0, 10.0, 5000.0, 9000.0, 0.0, 50.0, 0.0, 0.0, 0.0, 0.0],
    [1000.0, 2000.0, 1200.0, 0.0, 0.0, 10.0, 50000.0, 3000.0, 9000.0, 0.0, 50.0, 0.0, 0.0, 0.0, 0.0]];
    */

//根据坐标点的值选择颜色
function myColor(value) {
    if (value < 5000.0) return 0x2CDD22;
    else if (value >= 5000.0 && value < 10000.0) return 0x86FA05;
    else if (value >= 10000.0 && value < 15000.0) return 0xFFFF00;
    else if (value >= 15000.0 && value < 20000.0) return 0xFF8040;
    else return 0xFF0000;
}

var points;
function initPoint(data) {
    //console.log(mydata);
    var geoPoint = new THREE.SphereGeometry(pointRadius, 32, 32);
    var matPoint = new THREE.MeshBasicMaterial({color: 0xffffff});

    if (data == null)return;
    //数组第一维长度
    var len1 = 6;
    //数组第二维长度
    var len2 = 15;

    //初始化points
    points = new THREE.Object3D();
    scene.add( points );

    for (var i = 0; i < len1; i++)
        for (var j = 0; j < len2; j++) {
            //matPoint = new THREE.MeshBasicMaterial({color: myColor(data[i][j])});
            matPoint = new THREE.MeshLambertMaterial({color: myColor(data[i][j])});
            var point = new THREE.Mesh(geoPoint, matPoint);
            point.position.set(axisLength * (i + 1.0) / 6.0, data[i][j] / 10000.0 * pointYscale, axisLength * (j + 1.0) / 15.0);
            //为每个point的object设定id
            point.userData.id="p"+i+"-"+j;
            points.add(point);
        }
}


