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
        url: "/boss/account/accountFlowList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'id', width: 40, align: 'center'},
            {field: 'recordNo', title: 'recordNo', width: 120, align: 'center'},
            {field: 'flowUserType', title: 'flowUserType', width: 120, align: 'center'},
            {field: 'flowUserTypeStr', title: 'flowUserTypeStr', width: 120, align: 'center'},
            {field: 'recordType', title: 'recordType', width: 120, align: 'center'},
            {field: 'recordTypeStr', title: 'recordTypeStr', width: 120, align: 'center'},
            {field: 'remark', title: 'remark', width: 120, align: 'center'},
            {field: 'orderNo', title: 'orderNo', width: 120, align: 'center'},
            {field: 'totalMoney', title: 'totalMoney', width: 120, align: 'center'},
            {field: 'userNo', title: 'userNo', width: 120, align: 'center'},
            {field: 'accType', title: 'accType', width: 120, align: 'center'},
            {field: 'accTypeStr', title: 'accTypeStr', width: 120, align: 'center'},
            {field: 'recordStatus', title: 'recordStatus', width: 120, align: 'center'},
            {field: 'recordStatusStr', title: 'recordStatusStr', width: 120, align: 'center'},
            {field: 'createdDate', title: 'Create Date', width: 240, align: 'center',
                formatter: function (value) {
                    var newDate = new Date();
                    newDate.setTime(Date.parse(value) );
                    return newDate.toLocaleString();
                }},
            {field: 'modifiedDate', title: 'modified Date', width: 240, align: 'center',
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
            }, {
                field: 'operation', title: 'Operation', width: 100, align: 'center',
                formatter: function (value, row, index) {
                    if (row.recordType == '1' && row.recordStatus == '1') {
                    var op = "<a herf='javacript:;' onclick='updateCashSucc(" + '"' + row.id + '"' + ")' style='color: blue;cursor:pointer;'>已提现</a>&nbsp";
                    return op;
                    }
                    return "";
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
        $('#MGrid').datagrid('reload', {
            condition: JSON.stringify(qparam)
        });
    });
    $(".resetBtn").click(function () {
        $("#searchForm").form('clear');
    });

});
function updateCashSucc(id) {
    $.messager.confirm('继续操作', '确定执行下发操作吗?', function (data) {
        if (data) {
            $.ajax({
                url: 'updateCashSucc',
                type: 'post',
                dateType: 'json',
                data: {'id': id},
                success: function (data) {
                    if (!data.success) {
                        var errorDesc = 'system exception';
                        $.messager.show({
                            title: 'Error',
                            msg: errorDesc
                        });
                    } else {
                        $('#MGrid').datagrid('reload');    // reload the user data
                    }
                }

            });

        }

    });

}


