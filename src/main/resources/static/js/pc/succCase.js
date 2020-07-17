/**
 * Created by chendatao on 2018/12/7.
 */
//总数据量
var totalNum = 0;
//目前显示数据量
var pre = 0;
//页码
var pageNo = 1;
var myScroll = '';
$(function () {
    if (is_mobile()) {
        $("#morePc").hide();
        $("#wrapper").addClass('wrapperH5');
        scroll();
    } else {
        $("#wrapper").removeClass('wrapperH5');
        $("#more").hide();
    }
    queryList();
    $("#wrapper").show();
    $("#morePc").click(function () {
        pageNo++;
        queryList();
        return false;
    });

});


function queryList() {
    var param = {
        // "pageNum": 12,
        "pageNo": pageNo
        // "indexNo":a?a:null,
        // "gameType": gameType,
        // "gameDistrict": area == 0 ? null : area,
        // "orderColumn": 0,
        // "ascFlag": false,
        // "orderStatus": 4
    };

    $.ajax({
        url: '/queryList',
        type: 'get',
        dataType: 'json',
        data: param,
        success: function (data) {
            if (data.success) {
                //渲染
                var listRst = data.data;
                totalNum = data.total;
                var ul = $("#scroller").children('ul');
                var content = '';
                if (listRst && listRst.length > 0) {
                    for (var obj in listRst) {
                        if (obj>=param.pageNum) {
                        //    为了保证渲染列表数是pageNum，舍弃多余pageNum的数据
                            break;
                        }
                        var src = '';
                        var alt = '';
                        if (listRst[obj].gameType == '0') {
                            src = '/images/lol.jpg';
                            alt = '英雄联盟lol_代练-代练世界';
                        } else if (listRst[obj].gameType == '1') {
                            src = '/images/wzry1.jpg';
                            alt = '王者荣耀_代练-代练世界';
                        } else if (listRst[obj].gameType == '2') {
                            src = '/images/JDQS.jpg';
                            alt = '绝地求生刺激战场_代练-代练世界';
                        }else if (listRst[obj].gameType == '3') {
                            src = '/images/dnf.jpg';
                            alt = '王者荣耀_代练-代练世界';
                        } else if (listRst[obj].gameType == '4') {
                            src = '/images/JDQS.jpg';
                            alt = '绝地求生刺激战场_代练-代练世界';
                        }
                        content = content + ' <a href="/showSuccCaseDetail/' + listRst[obj].orderNo +
                            '"title="'+listRst[obj].orderNo+'代练单详情_成功案例_代练-代练世界"> <li> <section> <h3>' + listRst[obj].gameTypeStr + '代练单</h3> <div class="c"> ' +
                            '<img src="' + src + '" alt="'+alt+'"> <div class="content"> <p> <span> ' + listRst[obj].gameTypeStr + '    ' + listRst[obj].gameDistrictStr +
                            ' </span> <span> 赏金 ' + listRst[obj].rewardAmount + '¥ </span> </p> ' +
                            '<span class = "desc" > ' + (listRst[obj].orderDesc ? listRst[obj].orderDesc : '无描述') + '' +
                            ' </span> </div> </div> </section> </li> </a> ';
                    }
                }

                if (pageNo == 1) {
                    //刷新
                    if (!content) {
                        content = '<p style="text-align: center">亲~，没有数据^_^</p>';
                    }
                    ul.html(content);
                    pre = listRst ? listRst.length : 0;
                } else {
                    //叠加
                    ul.html(ul.html() + content);
                    pre = pre + (listRst ? listRst.length : 0);
                }
                // if (listRst) {
                if (is_mobile()) {
                    myScroll.refresh();
                }
                // }else {
                //
                // }
                if (pre >= totalNum) {
                    $("#more").hide();
                    $("#morePc").hide();
                } else {
                    if (is_mobile()) {
                        $("#wrapper").addClass('wrapperH5');
                        $("#morePc").hide();
                        $("#more").show();
                    } else {
                        $("#wrapper").removeClass('wrapperH5');
                        $("#morePc").show();
                        $("#more").hide();
                    }
                }

            } else {
                // nothing
            }
        },
        error: function (data) {
// nothing
        }
    });
}
function scroll() {
     myScroll = new iScroll('wrapper', {
        onScrollMove: function () {
            // if (this.y<(this.maxScrollY)) {
            //上拉30 开始变化
            if (this.y < -30) {
                $('.pull_icon').addClass('flip');
                $('.pull_icon').removeClass('loading');
                $('.more span').text('释放加载...');
            } else {
                $('.pull_icon').removeClass('flip loading');
                $('.more span').text('上拉加载...')
            }
        },
        onScrollEnd: function () {
            if ($('.pull_icon').hasClass('flip')) {
                $('.pull_icon').addClass('loading');
                $('.more span').text('加载中...');
                pageNo++;
                if (pre < totalNum) {
                    queryList();
                }
            }

        },
        onRefresh: function () {
            $('.pull_icon').removeClass('flip loading');
            $('.more span').text('上拉加载...');
        }
    });
}