var smallPro = false;
$(function () {
//判断当前页面是否在小程序环境中
    wx.miniProgram.getEnv(function (res) {
        console.log(res.miniprogram) // true
        if (res.miniprogram) {
            smallPro = true;
        } else {
            smallPro = false;
        }

    });
    init();
});
function init() {
//    初始化渲染
    var activeNo = $("#activeNo").val();
    switch (activeNo) {
        case "002":
            $("#a002").removeClass('hide');
            break;
        case "003":
            $("#a003").removeClass('hide');
            break;
    }
}
