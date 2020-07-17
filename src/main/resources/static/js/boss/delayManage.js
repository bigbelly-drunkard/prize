/**
 * Created by chendatao on 2018/3/8.
 */
var url = '';
$(function () {
    var iparam = {};
    $("#searchForm").serializeArray().map(function (x) {
        iparam[x.name] = x.value == "" ? null : x.value;
    });
    // console.log("iparam="+JSON.stringify(iparam));
    $('#delayMGrid').datagrid({
        url: "/boss/delayM/queryList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'ID', width: 40, align: 'center'},
            {field: 'orderNo', title: 'orderNo', width: 40, align: 'center'},
            {field: 'orderUserNo', title: 'orderUserNo', width: 120, align: 'center'},
            {field: 'gameTypeStr', title: 'gameTypeStr', width: 120, align: 'center'},
            {field: 'gameDistrictStr', title: 'gameDistrictStr', width: 120, align: 'center'},
            {
                field: 'hopeFinishDate', title: 'hopeFinishDate', width: 120, align: 'center',
                formatter: function (value) {
                    var newDate = new Date();
                    newDate.setTime(Date.parse(value));
                    return newDate.toLocaleString();
                }
            },
            {field: 'hopeFinishDay', title: 'hopeFinishDay', width: 120, align: 'center'},
            {
                field: 'expireDate', title: 'expireDate', width: 120, align: 'center'
            },
            {field: 'rewardAmount', title: 'rewardAmount', width: 120, align: 'center'},
            {field: 'creditAmount', title: 'creditAmount', width: 120, align: 'center'},
            {field: 'delayAmount', title: 'delayAmount', width: 120, align: 'center'},
            {field: 'orderDesc', title: 'orderDesc', width: 120, align: 'center'},
            {field: 'fileUrl', title: 'fileUrl', width: 120, align: 'center'},
            {field: 'orderStatusStr', title: 'orderStatusStr', width: 120, align: 'center'},
            {
                field: 'createdDate', title: 'createdDate', width: 240, align: 'center',
                formatter: function (value) {
                    var newDate = new Date();
                    newDate.setTime(Date.parse(value));
                    return newDate.toLocaleString();
                }
            },
            {
                field: 'modifiedDate', title: 'modifiedDate', width: 240, align: 'center',
                formatter: function (value) {
                    var newDate = new Date();
                    if (value) {
                        newDate.setTime(Date.parse(value));
                        return newDate.toLocaleString();
                    }
                    return "";
                }
            },
            {field: 'version', title: 'version', width: 120, align: 'center'},
            {field: 'accountNo', title: 'accountNo', width: 120, align: 'center'},
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
        $('#delayMGrid').datagrid('reload', {
            condition: JSON.stringify(qparam)
        });
    });
    $(".resetBtn").click(function () {
        $("#searchForm").form('clear');
        $("input[name='orderNo']").val("");
        $("input[name='orderUserNo']").val("");
    });

});
function editDelayD() {
    var row = $('#delayMGrid').datagrid('getSelected');
    if (row) {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit Detail');
        $('#fm').form('load', row);
        // url = 'updateHour?orderNo=' + row.orderNo;
    }

}
function editDelayL() {
    $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit All');
    // url = 'updateHourL';

}
function save() {
    // if (url.indexOf('updateHourAll')>0) {
    //     //更新全部
    //
    // }else {
    // //单独更新
    // }
    if (!$("#fm").form('validate')) {
        return;
    }
    $.ajax({
        type: 'POST',
        url: 'updateHourL',
        dataType: 'json',
        data: $('form').serializeJSON(),
        success: function (result) {
            if (!result.success) {
                var errorDesc = 'system exception';
                $.messager.show({
                    title: 'Error',
                    msg: errorDesc
                });
            } else {
                $('#dlg').dialog('close');        // close the dialog
                $('#delayMGrid').datagrid('reload');    // reload the user data
            }
        }
    });
};
function cancel(_this) {
    $("#" + _this).dialog('close');
}
