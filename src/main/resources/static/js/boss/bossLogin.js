/**
 *
 * Created by chendatao on 2018/1/19.
 */
$(function () {
    //渲染错误显示
    if ($("#err").val()==1||$("#err").val()=='1') {
        $(".pss").find('p').html('登录名或密码错误');
        $(".pss").find('p').show();
    }else{
        $(".pss").find('p').hide();
    }
$("#login").click(function () {
        if ($(this).hasClass('grayBtn1')) {
            return;
        }
        login();
    });


    /**
     * 回车事件
     * @param e
     */
    document.onkeydown = function(e){
        var ev = document.all ? window.event : e;
        if(ev.keyCode==13) {
            $("#login").click();
        }
    }
});

/**
 * 检查登录名并控制是否显示删除内容按钮
 */
function checkU() {
    var acc = $("#username").val();
    if (acc) {
        $("#login").removeClass('grayBtn1').addClass('sub_button');
    }else {
        $("#login").addClass('grayBtn1').removeClass('sub_button');
    }

}

/**
 * 检查登录密码
 */
function checkPw() {
    var pss = $("#userpwd").val();
    if (pss) {
        $("#login").removeClass('grayBtn1').addClass('sub_button');
    }else {
        $("#login").addClass('grayBtn1').removeClass('sub_button');
    }

}

/**
 * 登录
 */
function login() {
    var u = $("#username").val();
    var p = $("#userpwd").val();
    $("#login").addClass('grayBtn1');
    //故意增加加密的难度 start
    var c = 'da'+asedt();
    var param = {"name":u,"pwd":p};
    var param = Encrypt(JSON.stringify(param), c);
    //故意增加加密的难度 end
    $.ajax({
        url: 'login',
        type:'post',
        dataType:'json',
        data:{reqStr:param},
        success:function (data) {
            if (data.success) {
                var url = getRequestParam('returnUrl');
                url = url ? url : '/';
                location.href = decodeURIComponent(url);
            }else {
                if (data.respCode=='PRO3300003') {
                    location.href = 'toBossLogin?returnUrl='+encodeURIComponent(location.href);
                    return;
                }
                $(".pss").find('p').html('登录名或密码错误');
                $(".pss").find('p').show();
            }
        },
        error:function (data) {
            location.href ='/error/500';

        }
    });
}
