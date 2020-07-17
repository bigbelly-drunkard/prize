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
    $('#wxTokenMGrid').datagrid({
        url: "/boss/wx/wxToken/queryList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'ID', width: 40, align: 'center'},
            {field: 'accessToken', title: 'accessToken', width: 40, align: 'center'},
            {field: 'expiresIn', title: 'expiresIn', width: 120, align: 'center'},
            {field: 'jsapiTicket', title: 'jsapiTicket', width: 120, align: 'center'},
            {field: 'ticketExpiresIn', title: 'ticketExpiresIn', width: 120, align: 'center'},
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
                }}
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
});


