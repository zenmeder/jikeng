/**
 * Created by Cat on 2016/7/4.
 */

//左侧按钮（调整视图）
function initButtons() {
    var m_btns = $("<div id = 'toolbar'></div>");
    $(m_btns).css({
        "position": "absolute",
        "top": "150px",
        "left": "10px"
    });
    m_btns.appendTo($("body"));
    var cam_down = $("<button>俯视</button>").appendTo(m_btns).css({width: 60, height: 30});
    $("<br />").appendTo(m_btns);
    $("<br />").appendTo(m_btns);
    var cam_up = $("<button>仰视</button>").appendTo(m_btns).css({width: 60, height: 30});
    $("<br />").appendTo(m_btns);
    $("<br />").appendTo(m_btns);
    var cam_front = $("<button>正面</button>").appendTo(m_btns).css({width: 60, height: 30});
    $("<br />").appendTo(m_btns);
    $("<br />").appendTo(m_btns);
    var cam_back = $("<button>背面</button>").appendTo(m_btns).css({width: 60, height: 30});
    $("<br />").appendTo(m_btns);
    $("<br />").appendTo(m_btns);
    var cam_left = $("<button>左侧</button>").appendTo(m_btns).css({width: 60, height: 30});
    $("<br />").appendTo(m_btns);
    $("<br />").appendTo(m_btns);
    var cam_right = $("<button>右侧</button>").appendTo(m_btns).css({width: 60, height: 30});


    cam_front.on("click", function () {
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
        //console.log("正面");
        renderer.clear();
        renderer.render(scene, camera);
        requestAnimationFrame(render);
    });

    cam_back.on("click", function () {
        camera.position.x = 529;
        camera.position.y = 1079;
        camera.position.z = -1645;
        camera.up.x = 0.41;
        camera.up.y = 9.88;
        camera.up.z = 1.49;
        camera.lookAt({
            x: axisLength / 2.0,
            y: 0,
            z: axisLength / 2.0
        });
        //console.log("正面");
        renderer.clear();
        renderer.render(scene, camera);
        requestAnimationFrame(render);
    });

    cam_left.on("click", function () {
        camera.position.x = -1185;
        camera.position.y = 440;
        camera.position.z = 551;
        camera.up.x = 0;
        camera.up.y = 10;
        camera.up.z = 0;
        camera.lookAt({
            x: axisLength / 2.0,
            y: 0,
            z: axisLength / 2.0
        });
        //console.log("侧面");
        renderer.clear();
        renderer.render(scene, camera);
        requestAnimationFrame(render);
    });

    cam_right.on("click", function () {
        camera.position.x = 2232;
        camera.position.y = 114;
        camera.position.z = 350;
        camera.up.x = 1.87;
        camera.up.y = 9.82;
        camera.up.z = -0.26;
        camera.lookAt({
            x: axisLength / 2.0,
            y: 0,
            z: axisLength / 2.0
        });
        //console.log("侧面");
        renderer.clear();
        renderer.render(scene, camera);
        requestAnimationFrame(render);
    });

    cam_up.on("click", function () {
        camera.position.x = 473.0;
        camera.position.y = -1269.0;
        camera.position.z = -1539.0;
        camera.up.x = 0.01;
        camera.up.y = 6.42;
        camera.up.z = -7.67;
        camera.lookAt({
            x: axisLength / 2.0,
            y: 0,
            z: axisLength / 2.0
        });
        //console.log("俯视");
        renderer.clear();
        renderer.render(scene, camera);
        requestAnimationFrame(render);
    });

    cam_down.on("click", function () {
        camera.position.x = axisLength / 2.0;
        camera.position.y = axisLength * 2.0;
        camera.position.z = axisLength / 2.0;
        camera.up.x = 0.0;
        camera.up.y = 0.0;
        camera.up.z = -1.0;
        camera.lookAt({
            x: axisLength / 2.0,
            y: 0,
            z: axisLength / 2.0
        });
        //console.log("俯视");
        renderer.clear();
        renderer.render(scene, camera);
        requestAnimationFrame(render);
    });
}