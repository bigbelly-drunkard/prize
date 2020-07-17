/**
 * Created by chendatao on 2018/3/9.
 */
$(function () {
    var iparam = {};
    $("#searchForm").serializeArray().map(function (x) {
        iparam[x.name] = x.value == "" ? null : x.value;
    });
    // console.log("iparam="+JSON.stringify(iparam));
    $('#MGrid').datagrid({
        url: "/boss/order/publishOrderQueryList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'ID', width: 40, align: 'center'},
            {field: 'orderNo', title: 'orderNo', width: 120, align: 'center'},
            {field: 'orderUserNo', title: 'orderUserNo', width: 120, align: 'center'},
            {field: 'gameType', title: 'gameType', width: 120, align: 'center'},
            {field: 'gameTypeStr', title: 'gameTypeStr', width: 120, align: 'center'},
            {field: 'gameDistrict', title: 'gameDistrict', width: 120, align: 'center'},
            {field: 'gameDistrictStr', title: 'gameDistrictStr', width: 120, align: 'center'},
            {field: 'hopeFinishDate', title: 'hopeFinishDate', width: 120, align: 'center'},
            {field: 'hopeFinishDay', title: 'hopeFinishDay', width: 120, align: 'center'},
            {field: 'expireDate', title: 'expireDate', width: 120, align: 'center'},
            {field: 'rewardAmount', title: 'rewardAmount', width: 120, align: 'center'},
            {field: 'creditAmount', title: 'creditAmount', width: 120, align: 'center'},
            {field: 'delayAmount', title: 'delayAmount', width: 120, align: 'center'},
            {field: 'orderDesc', title: 'orderDesc', width: 120, align: 'center'},
            {field: 'orderStatus', title: 'orderStatus', width: 120, align: 'center'},
            {field: 'orderStatusStr', title: 'orderStatusStr', width: 120, align: 'center'},
            {field: 'finishRemark', title: 'finishRemark', width: 120, align: 'center'},
            {field: 'accountNo', title: 'accountNo', width: 120, align: 'center'},
            {field: 'createdDate', title: 'Create Date', width: 240, align: 'center',
                formatter: function (value) {
                    var newDate = new Date();
                    newDate.setTime(Date.parse(value) );
                    return newDate.toLocaleString();
                }},
            {field: 'modifiedDate', title: 'modify Date', width: 240, align: 'center',
                formatter: function (value) {
                    if (value) {
                        var newDate = new Date();
                        newDate.setTime(Date.parse(value) );
                        return newDate.toLocaleString();
                    }
                    return value;
                }},
            {
                field: 'version', title: 'version', width: 120, align: 'center'
            }
        ]],
        nowrap: false,
        // 显示行号
        rownumbers: true,
        /*fit: true,*/
        fitColumns: true,
        emptyMsg: 'No data found!',
        pagination: true,
        pageSize: 20,
        pageList: [5, 10, 15, 20, 25, 30, 40, 50],
        checkOnSelect: true,
        selectOnCheck: false
    });


    $("#query").click(function () {
        var qparam = {};
        $("#searchForm").serializeArray().map(function (x) {
            qparam[x.name] = x.value == "" ? null : x.value;
        });
        $('#MGrid').datagrid('reload', {
            condition: JSON.stringify(qparam)
        });
    });
    $("#queryReal").click(function () {
        var qparam = {};
        $("#searchForm").serializeArray().map(function (x) {
            qparam[x.name] = x.value == "" ? null : x.value;
        });
        qparam['blankUserNo']='xxx';
        $('#MGrid').datagrid('reload', {
            condition: JSON.stringify(qparam)
        });
    });
    $(".resetBtn").click(function () {
        $("#searchForm").form('clear');
        $("input[name='gameType']").val("");
        $("input[name='gameDistrict']").val("");
        $("input[name='orderStatus']").val("");
    });

});



