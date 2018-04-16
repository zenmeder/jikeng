/**
 * Created by Cat on 2016/6/7.
 */

"use strict"

//检查变量的类型
function getType(o) {
    var _t;
    return ((_t = typeof(o)) == "object" ? o == null && "null" || Object.prototype.toString.call(o).slice(8, -1) : _t).toLowerCase();
}

function calTHC() {
    //原始参数
    var myDate = window.location.search;
    //容错
    if (myDate == null) return;
    //calTHC_t(myDate);
    //参数转换为字符串类型
    var myDate_str = myDate.toString();
    //最终处理的参数
    var myDate_out = myDate_str.substring(6);
    //console.log(myDate_out);
    calTHC_t(myDate_out);
}

//最终传给graph.js的数据
var usefulData = {};
var url = "../SelectCurrentData";
function calTHC_t(date) {
    //前缀是"calTHC:"
    var iData = "date=" + "calTHC:" + date;
    $.ajax({
        type: "post",
        url: url,
        cache: false,
        dataType: "text",
        data: iData,
        success: function (data) {
            // console.log(data);
            var lines = data.split(";");
            var sensorID = 0;
            lines.forEach(function (item) {
                usefulData[sensorID] = [];
                var valueArray = item.split(",");
                valueArray.forEach(function (value) {
                    var object;
                    object = parseInt(value.split(".")[0]);
                    usefulData[sensorID].push(object);
                });
                ++sensorID;
            });
            console.log("usefulData:", usefulData);
            threeStart();
        },
        error: function () {
            console.log('THCError!');
        }

    });
}



