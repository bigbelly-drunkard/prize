var msgList = [], currMsgDate = '';
$(function () {
    queryDetail();
    setTimeout(function () {
        $("#ul li:first").animate({height:0},1000);//让第一个li 1秒内高度设置为0
    //    删除第一个元素
        msgList.shift();
        if (msgList.length<3) {
            queryMsg();
        }
    }, index*1000);//一秒出现一个li
});
function queryDetail() {
    call_slient('/customer/qc', {}, function (res) {
        $("#score").html(res.score.toFixed(0));
        $("#gold").html(res.gold.toFixed(0));
        var headUrl = res.headUrl ? res.headUrl : '../static/images/defaultHead.jpg';
        $("#headUrl").attr('src', headUrl);
        $("#nickName").html(nickName);
        $(".personNoLogin").hide();
    });
}
function queryMsg() {
    call_slient('i/qml', {}, function (res) {
        var list = res;
        var nextIndex = 0;
        res.forEach(function (p1, p2, p3) {
            if (p1.date > currMsgDate) {
                nextIndex = p2;
            }
            nextIndex = list.length;
        })
        var slice = list.slice(nextIndex);
        msgList.push(slice);
    })
}