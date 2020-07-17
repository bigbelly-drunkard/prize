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
    $('#feeRuleMGrid').datagrid({
        url: "/boss/feeRule/queryList",
        method: 'get',
        queryParams: {
            condition: JSON.stringify(iparam)
        },
        onBeforeLoad: function (param) {
        },
        singleSelect: true,
        columns: [[
            {field: 'id', title: 'ID', width: 40, align: 'center'},
            {field: 'ruleTypeStr', title: '收费规则类型', width: 40, align: 'center'},
            {field: 'feeTypeStr', title: '收费计算类型', width: 120, align: 'center'},
            {field: 'feeThresholdValue', title: '阈值', width: 120, align: 'center'},
            {field: 'feeValue', title: '阈值对应的满减或打折值', width: 120, align: 'center'},
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
            {
                field: 'operation', title: 'Operation', width: 100, align: 'center',
                formatter: function (value, row, index) {
                    var    op = "<a herf='javacript:;' onclick='deleteForId(" + '"' + row.id + '"' + ")' style='color: blue;cursor:pointer;'>删除</a>&nbsp";
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
        $('#feeRuleMGrid').datagrid('reload', {
            condition: JSON.stringify(qparam)
        });
    });
    $(".resetBtn").click(function () {
        $("#searchForm").form('clear');
        $("input[name='feeType']").val("");
        $("input[name='ruleType']").val("");
    });

});
function deleteForId(id) {
    $.messager.confirm('提示框', '你确定要删除吗?',function(data){
        if (data) {
        $.ajax({
            type: 'POST',
            url: 'delete',
            dataType: 'json',
            data: {'id':id},
            success: function (result) {
                if (!result.success) {
                    var errorDesc = 'system exception';
                    $.messager.show({
                        title: 'Error',
                        msg: errorDesc
                    });
                } else {
                    location.href = location.href;
                }
            }
        });
        }
    })
}
function editDetail() {
    var row = $('#feeRuleMGrid').datagrid('getSelected');
    if (row) {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'Edit Detail');
        $('#fm').form('load', row);
        url = "update";
    }
}
function addMore() {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', 'add ');
    url = "insert";

}
function save() {
    if (!$("#fm").form('validate')) {
        return;
    }
    $.ajax({
        type: 'POST',
        url: url,
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


