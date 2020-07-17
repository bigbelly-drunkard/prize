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
        url: "/boss/account/accountFlowDetailList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'ID', width: 40, align: 'center'},
            {field: 'recordNo', title: 'recordNo', width: 120, align: 'center'},
            {field: 'accountFlowNo', title: 'accountFlowNo', width: 120, align: 'center'},
            {field: 'orderNo', title: 'orderNo', width: 120, align: 'center'},
            {field: 'userNo', title: 'userNo', width: 120, align: 'center'},
            {field: 'recordType', title: 'recordType', width: 120, align: 'center'},
            {field: 'recordTypeStr', title: 'operTypeStr', width: 120, align: 'center'},
            {field: 'flowUserType', title: 'flowUserType', width: 120, align: 'center'},
            {field: 'flowUserTypeStr', title: 'flowUserTypeStr', width: 120, align: 'center'},
            {field: 'operType', title: 'operType', width: 120, align: 'center'},
            {field: 'operTypeStr', title: 'operTypeStr', width: 120, align: 'center'},
            {field: 'compensateType', title: 'compensateType', width: 120, align: 'center'},
            {field: 'compensateTypeStr', title: 'compensateTypeStr', width: 120, align: 'center'},
            {field: 'amount', title: 'amount', width: 120, align: 'center'},
            {field: 'recordStatus', title: 'recordStatus', width: 120, align: 'center'},
            {field: 'recordStatusStr', title: 'recordStatusStr', width: 120, align: 'center'},
            {field: 'remark', title: 'remark', width: 120, align: 'center'},
            {field: 'createdDate', title: 'Create Date', width: 240, align: 'center',
                formatter: function (value) {
                    var newDate = new Date();
                    newDate.setTime(Date.parse(value) );
                    return newDate.toLocaleString();
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


    $(".queryBtn").click(function () {
        var qparam = {};
        $("#searchForm").serializeArray().map(function (x) {
            qparam[x.name] = x.value == "" ? null : x.value;
        });
        $('#MGrid').datagrid('reload', {
            condition: JSON.stringify(qparam)
        });
    });
    $(".resetBtn").click(function () {
        $("#searchForm").form('clear');
        $("input[name='recordType']").val("");
        $("input[name='flowUserType']").val("");
        $("input[name='recordStatus']").val("");
    });

});


