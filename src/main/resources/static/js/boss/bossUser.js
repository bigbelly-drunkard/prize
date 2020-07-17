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
        url: "/boss/user/userQueryList/queryList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'ID', width: 40, align: 'center'},
            {field: 'userNo', title: 'userNo', width: 120, align: 'center'},
            {field: 'userName', title: 'userName', width: 120, align: 'center'},
            {field: 'password', title: 'password', width: 120, align: 'center'},
            {field: 'mwpsw', title: 'mwpsw', width: 120, align: 'center'},
            {field: 'recommendCode', title: 'recommendCode', width: 120, align: 'center'},
            {field: 'userHeadPic', title: 'userHeadPic', width: 120, align: 'center'},
            {field: 'userNickName', title: 'userNickName', width: 120, align: 'center'},
            {field: 'userType', title: 'userType', width: 120, align: 'center'},
            {field: 'userTypeStr', title: 'userTypeStr', width: 120, align: 'center'},
            {field: 'channelType', title: 'channelType', width: 120, align: 'center'},
            {field: 'channelTypeStr', title: 'channelTypeStr', width: 120, align: 'center'},
            {field: 'channelUniqNo', title: 'channelUniqNo', width: 120, align: 'center'},
            {field: 'userStatus', title: 'userStatus', width: 120, align: 'center'},
            {field: 'userStatusStr', title: 'userStatusStr', width: 120, align: 'center'},
            {field: 'score', title: 'score', width: 120, align: 'center'},
            {field: 'balance', title: 'balance', width: 120, align: 'center'},
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
        $("input[name='userType']").val("");
        $("input[name='channelType']").val("");
        $("input[name='userStatus']").val("");
    });

});
function editDetail() {
    var row = $('#MGrid').datagrid('getSelected');
    if (row) {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit Detail');
        $('#fm').form('load', row);
    }
}
function agentDone() {
    var row = $('#MGrid').datagrid('getSelected');
    if (row) {
        // $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'agent');
        $('#fm').form('load', row);
    }
    $.messager.confirm('提示框', '你确定让此用户变为代理吗?',function(data){
        if (data) {
            $.ajax({
                type: 'POST',
                url: "updateAgent",
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
                        location.href = location.href;
                    }
                }
            });
        }
    })
}
function save() {
    if (!$("#fm").form('validate')) {
        return;
    }
    $.ajax({
        type: 'POST',
        url: "updateStatus",
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
                location.href = location.href;
            }
        }
    });
};
function cancel(_this) {
    $("#" + _this).dialog('close');
}



