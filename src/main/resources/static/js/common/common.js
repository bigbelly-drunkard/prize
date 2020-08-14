// var deviceWidth = parseInt(window.screen.width);  //获取当前设备的屏幕宽度
//     var deviceWidth = parseInt($(window).width());  //获取当前设备的屏幕宽度
// alert("deviceWidth"+deviceWidth)
// var deviceScale = deviceWidth / (414*414/360);  //得到当前设备屏幕与640之间的比例，之后我们就可以将网页宽度固定为640px
// document.write('<meta name="viewport" content="width=414,initial-scale=' + deviceScale + ', minimum-scale = ' + deviceScale + ', maximum-scale = ' + deviceScale + '">');
document.write('<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">');
// window.onload = function () {
//     (function () {
        var html = document.documentElement;
        var windowWidth = html.clientWidth;
        // var oldBodyFontSizeStr = $("body").css('font-size').split('px')[0];
        // var oldBodyFontSize = parseInt(oldBodyFontSizeStr);
        // $("body").css('font-size',(windowWidth / 414 * oldBodyFontSize) + 'px');
        html.style.fontSize = (windowWidth / 414 * 16) + 'px';
//     })();
// }
window.scroll(0, 0);
//用户名正则，4到16位（字母或数字或下划线或减号）
var uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
//密码正则，6到16位(字母或数字或下划线)（
var pwPattern = /^\w{6,16}$/;

function checkUserName(name) {
    return uPattern.test(name);
}
function checkPassword(name) {
    return pwPattern.test(name);
}
function checkNum(num) {
    var numPattern = /^\d+(\.\d{1,2})?$/;
    return numPattern.test(num) && num > 0;
}
function checkNumInt(num) {
    var numPattern = /^[0-9]+$/;
    return numPattern.test(num);
}
function asedt() {
    return "taochendataoch";

}
function Encrypt(word, c) {
    var key = CryptoJS.enc.Utf8.parse(c);

    var srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
    return encrypted.toString();
}
function Decrypt(word, c) {
    var key = CryptoJS.enc.Utf8.parse(c);

    var decrypt = CryptoJS.AES.decrypt(word, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}

if (window.console) {
    //is open
    console.log('what are you doing?');
}
;

/**
 * 切换密码显示格式
 * @param oof
 */
function switchPwd(oof) {
    var pwd = $(oof);
    if (pwd.hasClass('icon-unlook')) {
        pwd.siblings('input').attr('type', 'text');
        pwd.addClass('icon-look').removeClass('icon-unlook');
    } else {
        pwd.siblings('input').attr('type', 'password');
        pwd.addClass('icon-unlook').removeClass('icon-look');
    }

}

/**************************************时间格式化处理************************************/
function dateFtt(fmt, date) { //author: meizz
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
/**
 * 倒计时
 * @param x 需要html的this
 * @param y 时间
 */
function timeOut(x, y) {
    setInterval(function () {

        $(x).html(calcDHMS(y));
    }, 1000);
}
/**
 * 计算时间差及文案描述
 * @param y 时间
 * @returns {string}
 */
function calcDHMS(y) {
    var leftTime = (y - new Date()); //计算剩余的毫秒数
    var days = parseInt(leftTime / 1000 / 60 / 60 / 24, 10); //计算剩余的天数
    var hours = parseInt(leftTime / 1000 / 60 / 60 % 24, 10); //计算剩余的小时
    var minutes = parseInt(leftTime / 1000 / 60 % 60, 10);//计算剩余的分钟
    var seconds = parseInt(leftTime / 1000 % 60, 10);//计算剩余的秒数
    days = checkTime(days);
    hours = checkTime(hours);
    minutes = checkTime(minutes);
    seconds = checkTime(seconds);
    var s = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
    return s;
}
/**
 * CST时间转换成GMT
 * 后台java返回的是CST时间
 * 前端js 默认是GMT时间 GMT比CST在东八区多14个小时
 * @param strDate
 * @returns {Date}
 */
function dateToGMT(strDate) {
    var dateStr = strDate.split(" ");
    var strGMT = dateStr[0] + " " + dateStr[1] + " " + dateStr[2] + " " + dateStr[5] + " " + dateStr[3] + " GMT+0800";
    var date = new Date(Date.parse(strGMT));
    return date;
}
/**
 * 补零
 * @param i
 * @returns {*}
 */
function checkTime(i) { //将0-9的数字前面加上0，例1变为01
    if (i < 10) {
        i = "0" + i;
    }
    return i;
}


/**
 * 显示查看密码弹窗
 */
function queryPwd() {
    $("#mask").show();
    $("#alert1").show();
    $("#p").focus();
}
/**
 * 取消弹窗1
 */
function cancel() {
    $("#mask").hide();
    $("#alert1").hide();

}

/**
 * 获取请求路径的参数
 * @param param
 * @returns {*}
 */
function getRequestParam(param) {
    var requestString = location.search;
    var reg = new RegExp("(?:\\?|&)" + param + "=(.*?)(?:&|$)");
    if (reg.test(requestString)) {
        return decodeURIComponent(RegExp.$1);
    } else {
        return '';
    }
}
/**
 * 判断是否在微信公众号中打开
 * @returns {boolean}
 */
function is_weixn() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger") {
        return true;
    } else {
        return false;
    }
}
/**
 * 判断是否在微信小程序打开
 * @returns {boolean}
 */
function is_weixn_mini() {
    if ($.cookie('isMini')) {
    // if (window.__wxjs_environment === 'miniprogram') {
        return true;
    } else {
        return false;
    }
}