/**
 * Created by Cat on 2016/5/31.
 */
var renderer;
var containerWidth;
var containerHeight;

var container;
function initThree() {
    container = document.getElementById('canvas-frame');
    containerWidth = container.clientWidth;
    containerHeight = container.clientHeight;

    renderer = new THREE.WebGLRenderer({
        antialias: true,//antialias:true/false是否开启反锯齿
        precision: "highp",//precision:highp/mediump/lowp着色精度选择
        alpha: true,//alpha:true/false是否可以设置背景色透明
        premultipliedAlpha: false,//?
        stencil: false,//?
        preserveDrawingBuffer: true,//preserveDrawingBuffer:true/false是否保存绘图缓冲
        maxLights: 1//maxLights:最大灯光数
    });

    renderer.setSize(containerWidth, containerHeight);
    renderer.setClearColor(0xFFFFFF, 1.0);
    container.appendChild(renderer.domElement);
    //当改变窗口大小时重新绘制
    window.addEventListener('resize', onWindowResize, false);
    //当鼠标移动到小球上时显示具体信息
    window.addEventListener('mousemove', onMouseMove, false);
}

var camera;
var controls;
function initCamera() {
    camera = new THREE.PerspectiveCamera(45, containerWidth / containerHeight, 1, 10000);
    camera.position.x = 995;
    camera.position.y = 749;
    camera.position.z = 2728;
    camera.up.x = 0;
    camera.up.y = 10;
    camera.up.z = 0;
    camera.lookAt({
        x: axisLength / 2.0,
        y: 0,
        z: axisLength / 2.0
    });
    //相机控制（THREE.js插件-轨迹球控制器）
    controls = new THREE.TrackballControls(camera);
    controls.zoomSpeed = 0.5;
    controls.rotateSpeed = 3.0;
    controls.target = new THREE.Vector3(axisLength / 2.0, 0.0, axisLength / 2.0);
}

var scene;
function initScene() {
    scene = new THREE.Scene();
}

//初始化光照
var light;
function initLight() {
    var ambient_light = new THREE.AmbientLight(0xFFFFFF, 0.3);
    scene.add(ambient_light);

    var direct_light = new THREE.DirectionalLight(0xFFFFFF, 1.0);
    scene.add(direct_light);
}

//初始化坐标轴，并根据数据生成坐标点
function initObject() {
    initAxis();
    initAxisPoint();
    initPoint(usefulData);
}

//渲染函数
function render() {
    controls.update();
    requestAnimationFrame(render);
    renderer.render(scene, camera);
}

//监听器（窗口大小发生改变）的响应函数
function onWindowResize() {
    containerWidth = container.clientWidth;
    containerHeight = container.clientHeight;

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);
    controls.handleResize();
    render();
}

//监听器（鼠标移动）的响应函数（显示小球信息）
function onMouseMove(e) {

    //var mouseVector = new THREE.Vector3();
    //mouseVector.x = 2 * (e.clientX / containerWidth) - 1;
    //mouseVector.y = 1 - 2 * ( e.clientY / containerHeight );

    var vector = new THREE.Vector3();
    var raycaster = new THREE.Raycaster();
    var dir = new THREE.Vector3();

    if (camera instanceof THREE.OrthographicCamera) {
        vector.set(( event.clientX / window.innerWidth ) * 2 - 1, -( event.clientY / window.innerHeight ) * 2 + 1, -1); // z = - 1 important!
        vector.unproject(camera);
        dir.set(0, 0, -1).transformDirection(camera.matrixWorld);
        raycaster.set(vector, dir);
    } else if (camera instanceof THREE.PerspectiveCamera) {
        vector.set(( event.clientX / window.innerWidth ) * 2 - 1, -( event.clientY / window.innerHeight ) * 2 + 1, 0.5); // z = 0.5 important!
        vector.unproject(camera);
        raycaster.set(camera.position, vector.sub(camera.position).normalize());
    }

    //所有被picking-ray穿过的物体
    var intersects = raycaster.intersectObjects(points.children);

    //如果没有触碰到物体，则销毁div
    if(intersects.length == 0 && $("#follower")){
        //console.log("removed");
        $("#follower").remove();
    }
    //对被pick到的物体进行处理
    if (intersects.length > 0) {
         //有时候会pick到好几个物体，这时只处理第一个
        //获取被pick的object的id，并且根据这个id找到相应的数据
        var pid = intersects[0].object.userData.id;
        var p_i = parseInt(pid.substr(1,1));
        var p_j = parseInt(pid.substr(3));
        //console.log("p_i:",p_i,",","p_j:",p_j);
        //console.log(usefulData[p_i][p_j]);

        //在鼠标位置生成div，显示被pick的object的value
        $(function () {
            $(document.body)
                .append($('<div id="follower" style="width:50px;height:20px;border:1px solid red;position:absolute"></div>'))
                //其实这里的.mousemove是多次一举，因为本身就已经写在MouseMove响应函数里面，但是保留这种写法是为了记住可以这样写。
                .mousemove(function (e) {
                    $("#follower").text(usefulData[p_i][p_j]).css({top: e.pageY + 10, left: e.pageX + 10});
                })
        })
        /*
        for (var i = 0; i < intersects.length; i++) {
            var obj = intersects[i].object;
            obj.material.color.setRGB(0.0, 0.0, 0.0);
        }
        */
    }
}

function threeStart() {
    initThree();
    initCamera();
    initScene();
    initButtons();
    initlogo();
    initLight();
    initObject();
    render();
}

