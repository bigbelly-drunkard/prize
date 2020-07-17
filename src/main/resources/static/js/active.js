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
var teamNoShare = '';
var teamNoMy = '';
var shareUserNo = '';
var myUserNo = '';
var openId4Mini = '';
function init() {
//    初始化渲染
//    获取url的teamNo 参数
    teamNoShare = getQueryVariable('teamNoShare');
    shareUserNo = getQueryVariable('shareUserNo');
    openId4Mini = getQueryVariable('openId4Mini');
    if (!openId4Mini) {
        console.log("没有登录 没有openId4Mini")
    }
    $.ajax({
        type: 'POST',
        url: '/active/q',
        dataType: 'json',

        headers: {openId: openId4Mini},
        data: {teamNo: teamNoShare},
        success: function (result) {
            if (!result.success) {
                if (result.respCode == 'PRO3300017') {
                    //活动已过期  不可以创建队伍 不可以领奖
                    $("#createTeam").hide();
                    $("#failMsg").html('活动已过期');
                    return;
                }
                location.href = '/error/500';
            } else {
                var activeBean = result.data.activeBean;
                var activeTeam = result.data.activeTeam;
                var orderNos = result.data.orderNos;
                var activePrices = result.data.activePrices;
                myUserNo = result.data.userNo;
                //    是否存在队伍
                if (!activeTeam) {
                    // $("#createTeam").show();
                    show("#createTeam");
                } else {
                    // $(".ruleBlank").hide()
                    $("#myTeam .title").html(activeTeam.teamName);
                    teamNoMy = activeTeam.teamNo;
                    var ul = activeTeam.userList;
                    var s = '';
                    if (ul && ul.length >= 0) {
                        for (obj in ul) {
                            var headurl = '/images/defaultHead.jpg';
                            if (ul[obj].headUrl) {
                                headurl = ul[obj].headUrl;
                            }
                            var nickName = '室友';
                            if (ul[obj].nickName) {
                                nickName = ul[obj].nickName;
                            }
                            s += ' <div class="item">'
                                + ' <img src="' + headurl + '"/>'
                                + ' <span class="score">+' + ul[obj].integral + '分</span>'
                                + '<span class="nickName">' + nickName + '</span>'
                                + '</div>'
                        }

                    }
                    $("#myTeam .userList").html(s);
                    // $("#myTeam").show();
                    show("#myTeam");

                    //    队伍积分渲染
                    var integralTotal = activeTeam.integralTotal;
                    var number = 3 * (integralTotal - 3) + 15;
                    $("#fen").attr('width', number + '%').html(integralTotal + '分');
                    show("#jifen");
                    if (teamNoShare) {
                        //    切换分享、加入队伍按钮
                        $("#share").hide();
                        show("#shareJoin");
                    }
                }

                //    活动是否已到开奖时间 显示中奖信息 隐藏创建队伍
                var time = new Date().getTime();
                if (activeBean.oneTime < time) {
                    $("#createTeam").hide();

                    if (!activePrices || activePrices.length === 0) {
                        // 未中奖
                        $("#failMsg").html('未中奖或已领取过奖品');
                        show("#failMsg")
                    } else {
                        var activePrice = activePrices[0];
                        //    显示中奖内容
                        show("#priceButton");

                        if ('ONE' === activePrice.type) {
                            show("#price_88");
                        } else if ('TWO' === activePrice.type) {
                            show("#price_888");
                        } else {

                        }
                    }
                    // $("#priceInfo").show();
                    show("#priceInfo");
                }

                //    是否存在订单
                if (orderNos && orderNos.length > 0) {
                    var ss = '    <span class="title">我的积分订单</span>'
                        + '<div class="orderT">'
                        + '  <div class="orderNoTitle">订单号</div>'
                        + '<div>积分</div>'
                        + '</div>';
                    for (obj1 in orderNos) {
                        ss += ' <div class="orderT" onclick="toOrder4Mini(\'' + orderNos[obj1] + '\')">'
                            + '  <div class="orderNo">' + orderNos[obj1] + '</div>'
                            + '  <div>+1分</div>'
                            + '  </div>';

                    }
                    $("#orderNos").html(ss);
                    show("#orderNos");
                }


            }
        }
    });
}
function createTeamSubmit() {
    var val = $('.teamInput').val();
    if (!val) {
        return;
    }
    $.ajax({
        type: 'POST',
        url: '/active/c',
        headers: {openId: openId4Mini},
        dataType: 'json',
        data: {teamName: val},
        success: function (result) {
            if (result.success) {
                location.href = location.href;
            } else {
                switch (result.respCode) {
                    case 'PRO3300018':
                        showError('您已有队伍，不可创建新队伍啦');
                        break;
                    case 'PRO3300017':
                        showError('活动已结束，不可创建新队伍啦');
                        break;
                    default:
                        location.href = '/error/500';
                        break;
                }
                return;
            }
        }
    });
}
function share() {

//跳到小程序分享页面
    var url = window.location.href + '&teamNoShare=' + teamNoMy + '&shareUserNo=' + myUserNo;
    var s = encodeURIComponent(url);
    var postData = {
        title: '寝室组队活动开始了，快快进来看一下你的寝室状况吧',
        page: '/pages/web/web?url=' + s
    };

    if (smallPro) {
        wx.miniProgram.navigateTo({url: '/pages/shareTwo/shareTwo?param=' + encodeURIComponent(JSON.stringify(postData))})
    } else {
        alert("请在小程序打开。微信搜索【快递代拿】小程序")
    }


}
function toOrder4Mini(orderNo) {
    if (smallPro) {
        wx.miniProgram.navigateTo({url: '/pages/orderDetail/orderDetail?pubOrderNo=' + orderNo})
    } else {
        alert("请在小程序打开。微信搜索【快递代拿】小程序")
    }
}
function toPubOrder() {
    if (smallPro) {
        wx.miniProgram.navigateTo({url: '/pages/index/index'})
    } else {
        alert("请在小程序打开。微信搜索【快递代拿】小程序")
    }
}
function confirmPrice() {
    $.ajax({
        type: 'POST',
        url: '/active/g',
        dataType: 'json',
        //mock todo
        headers: {openId: openId4Mini},
        data: {},
        success: function (result) {
            if (result.success) {
                location.href = location.href;
            } else {
                switch (result.respCode) {
                    case 'PRO3300017':
                        showError('活动已结束');
                        break;
                    default:
                        location.href = '/error/500';
                        break;
                }
                return;
            }
        }
    });
}
function joinTeam() {
    $.ajax({
        type: 'POST',
        url: '/active/j',
        dataType: 'json',
        //mock todo
        headers: {openId: openId4Mini},
        data: {teamNo: teamNoShare, shareUserNo: shareUserNo},
        success: function (result) {
            if (result.success) {
                location.href = location.href;
            } else {
                switch (result.respCode) {
                    case 'PRO3300016':
                        showError('队伍人员已满，无法加入');
                        break;
                    case 'PRO3300018':
                        showError('您已加入过其他队伍，不可加入此队伍啦');
                        break;
                    default:
                        location.href = '/error/500';
                        break;
                }
                return;
            }
        }
    });
}
function show(_seleted) {
    $(_seleted).removeClass('hide');

}
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}
function showError(content) {
    content = content ? content : '系统错误，请稍后再试';
    $("#error .content").html(content);
    show(".mask");
    show("#error");
}
function hideError() {
    $(".mask").hide();
    $("#error").hide();
}