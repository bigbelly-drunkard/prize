$(function () {
    /**
     * Created by chendatao on 2018/3/28.
     */
    //判断是否是小程序
    if (is_weixn()&&!is_weixn_mini()) {
        var signVo = $("#signVo").val();
        var signVoObject = JSON.parse(signVo);
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: signVoObject.appId, // 必填，公众号的唯一标识
            timestamp: signVoObject.timestamp, // 必填，生成签名的时间戳
            nonceStr: signVoObject.nonceStr, // 必填，生成签名的随机串
            signature: signVoObject.signature,// 必填，签名
            // 必填，需要使用的JS接口列表 微信支付，分享朋友圈，分享给朋友，分享qq,分享微博，分享qq空间
            jsApiList: ['chooseWXPay', 'onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone']
        });


        wx.ready(function () {
            //  如果需要在分享里有动态信息就需要这个标志位了 之前发现初始化后无法动态改标题
            setInterval(function () {

                if (shareFlag) {
                    wx.onMenuShareTimeline(getShare(0));//分享到朋友圈
                    wx.onMenuShareAppMessage(getShare(1));//分享给好友
                    wx.onMenuShareQQ(getShare(2));//分享给手机QQ
                    wx.onMenuShareWeibo(getShare(3));//分享给手机QQ
                    wx.onMenuShareQZone(getShare(4));//分享给手机QQ
                    shareFlag = false;
                }
            }, 100);

            // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
        });
        wx.error(function (res) {
            console.log(res);
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        });
    }
});
//支付
function wxpay(data) {
    //判断是否是app
    var isApp = $.cookie('isApp');
    if (isApp == 'true') {
        //调用Android的方法
        window.android.setJson(data.toString());
        return;
    }

    if (data.mwebUrl != null && data.mwebUrl != '') {
        //h5支付
        Location.href = data.mwebUrl;
        return;
    }
    // test code_url
    if (data.codeUrl != null && data.codeUrl != '') {
        //h5支付
        console.log(data.codeUrl);
        Location.href = data.codeUrl;
        return;
    }
    //判断是否是小程序
    if (window.__wxjs_environment === 'miniprogram') {
        wx.miniProgram.navigateTo({url: '../pay/pay?param=' + encodeURIComponent(JSON.stringify(data))});
        return;
    }

// 发起一个微信支付请求
    wx.chooseWXPay({
        timestamp: data.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
        nonceStr: data.nonceStr, // 支付签名随机串，不长于 32 位
        'package': 'prepay_id=' + data.prepay_id, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=\*\*\*）
        signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
        paySign: data.paySign, // 支付签名
        success: function (res) {
// 支付成功后的回调函数
            if (res.errMsg == "chooseWXPay:ok") {
                if ('PREACC' == data.payType) {
                    location.href = "/showPODetail/"+data.orderNo+"/true";
                    return;
                }
                else if ('PUB' == data.payType) {
                    location.href = "/toPublishResult/" + data.outTradeNo;
                    return;
                }
                else if ('RECHARGE' == data.payType) {
                    location.href = "/toRechargeResult/" + data.outTradeNo;
                    return;
                }
            } else {
                location.href = "/error/500";

            }
        },
        //如果你按照正常的jQuery逻辑，下面如果发送错误，一定是error，那你就太天真了，当然，jssdk文档中也有提到
        fail: function (res) {
            //接口调用失败时执行的回调函数。
            console.log('fail');
            payfail(data.prepay_id);
        },
        complete: function (res) {
            //接口调用完成时执行的回调函数，无论成功或失败都会执行。
        },
        cancel: function (res) {
            //用户点击取消时的回调函数，仅部分有用户取消操作的api才会用到。
            console.log('cancel');
            location.href = "/showPODetail/"+data.orderNo;
            payfail(data.prepay_id);
        },
        trigger: function (res) {
            //监听Menu中的按钮点击时触发的方法，该方法仅支持Menu中的相关接口。
        }
    });

}
function payfail(prepay_id) {
    $.ajax({
        url: '/wx/fail',
        type: 'post',
        dataType: 'json',
        data: {prePayId: prepay_id},
        success: function (data) {
            if (data.success) {
                console.log('cancel success');
            }
            else if (data.respCode == 'PRO3300003') {
                location.href = '/toLogin?returnUrl=' + encodeURIComponent(location.href);
                return;
            }
        },
        error: function (data) {
            location.href = '/error/500';

        }
    });

}


/**
 *
 * @param x 0.朋友圈分享 1.发送给朋友 2.qq 3.微博 4.qq空间

 * @param y 0:已分享 1：取消分享
 */
function shareAjax(x, y) {
    $.ajax({
        url: '/shareRecord/insert',
        type: 'post',
        dataType: 'json',
        data: {shareType: x, shareStatus: y},
        success: function () {
            console.log('share record succ');
        },
        error: function () {
            console.log('share record error');

        }

    });

}

function getShare(x) {
    var umu = $.cookie('umu');
    var sno = encodeURIComponent($.cookie('sno'));
    var share = {
        "imgUrl": "https://m.dailianshijie.com/images/share.jpg",//分享图，默认当相对路径处理，所以使用绝对路径的的话，“http://”协议前缀必须在。
        "desc": "立即分享给好友，好友注册成功立即奖励信任分10分，且好友账号单子成功结算后，推荐人将与平台五五分手续费。",//摘要,如果分享到朋友圈的话，不显示摘要。
        "title": '分享推荐返现金',//分享卡片标题
        link: 'https://m.dailianshijie.com/showShareCommend?un=' + umu + '&sno=' + sno, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        // link: 'http://192.168.0.100:8080/shareIndex',// 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        "success": function () {//分享成功后的回调函数
            //             // 用户确认分享后执行的回调函数
            $("#shareId").trigger('click');
            shareAjax(x, 0);
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
            $("#shareCancel").trigger('click');
            shareAjax(x, 1);
        }
    };
    return share;
}