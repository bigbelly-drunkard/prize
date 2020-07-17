/**
 * Created by chendatao on 2018/3/9.
 */
var url = '';
$(function () {
    var iparam = {};
    $("#searchForm").serializeArray().map(function (x) {
        iparam[x.name] = x.value == "" ? null : x.value;
    });
    // console.log("iparam="+JSON.stringify(iparam));
    $('#checkMGrid').datagrid({
        url: "/boss/order/queryList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'ID', width: 40, align: 'center'},
            {field: 'acceptOrderNo', title: 'acceptOrderNo', width: 40, align: 'center'},
            {field: 'orderUserNo', title: 'orderUserNo', width: 120, align: 'center'},
            {field: 'publishOrderNo', title: 'publishOrderNo', width: 120, align: 'center'},
            {field: 'checkFailReason', title: 'checkFailReason', width: 120, align: 'center'},
            {field: 'overdueDays', title: 'overdueDays', width: 120, align: 'center'},
            {field: 'orderStatusStr', title: 'orderStatusStr', width: 120, align: 'center'},
            {field: 'finishRemark', title: 'finishRemark', width: 120, align: 'center'},
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
            {field: 'version', title: 'version', width: 120, align: 'center'},
            {field: 'fileUrl', title: 'fileUrl', width: 120, align: 'center'},
            {
                field: 'operation', title: 'Operation', width: 100, align: 'center',
                formatter: function (value, row, index) {
                    var    op = "<a herf='javacript:;' onclick='showDetail(" + '"' + row.acceptOrderNo + '"' + ")' style='color: blue;cursor:pointer;'>详情</a>&nbsp";
                    return op;
                }
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


    $(".queryBtn").click(function () {
        var qparam = {};
        $("#searchForm").serializeArray().map(function (x) {
            qparam[x.name] = x.value == "" ? null : x.value;
        });
        $('#checkMGrid').datagrid('reload', {
            condition: JSON.stringify(qparam)
        });
    });
    $(".resetBtn").click(function () {
        $("#searchForm").form('clear');
        $("input[name='acceptOrderNo']").val("");
        $("input[name='orderUserNo']").val("");
        $("input[name='orderStatus']").val("");
    });

});

function showDetail(acceptOrderNo) {
    window.open('/boss/order/showDetail?acceptOrderNo=' + acceptOrderNo);
}
