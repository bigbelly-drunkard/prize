$(function () {
    $("input[name='checkType']").click(function () {
        console.log('okchecktype');
            var radio = $(this).val();
           if (radio=='checkFail') {
               $("#passDiv").hide();
               $("#rejectDiv").show();
            }else {
               $("#passDiv").show();
               $("#rejectDiv").hide();

            }
    });
    $("#credit_radio").change(function () {
    var val = $(this).val();
        if (val==0||val==1) {
        //    隐藏扣除信誉分
            $("#creditSub").hide();
        }
        else {
        //    展示扣除信誉分
            $("#creditSub").show();

        }

    });
    $("")
});

/**
 * 审核通过
 */
function pass() {
    var finishAccountTypeIndex = $("#credit_radio option:selected").val();
    var accOrderNo = $("#accOrderNo").val();
    var changeScore = $("#changeScore").val();
    var param = {'accOrderNo':accOrderNo,'finishAccountTypeIndex':finishAccountTypeIndex,'changeScore':changeScore};
    $.ajax({
        url:'checkSucc',
        type:'post',
        dataType:'json',
        data:param,
        success:function (data) {
            if (data.success) {
                location.href=location.href;
            }else {
                alert(data.respDesc);
            }
        },
        error:function () {
            alert("pass request error!")
        }

    });
}
/**
 * 审核拒绝
 */
function reject() {
    var submit = $("#checkFailForm").submit();
    // alert(submit);
    // location.href=location.href;
}
/**
 * 逾期确认
 */
function confirm() {
    var finishAccountTypeIndex = $("#credit_radio option:selected").val();
    var accOrderNo = $("#accOrderNo").val();
    var param = {'accOrderNo':accOrderNo,'finishAccountTypeIndex':finishAccountTypeIndex};
    $.ajax({
        url:'checkDelay',
        type:'post',
        dataType:'json',
        data:param,
        success:function (data) {
            if (data.success) {
                location.href=location.href;
            }else {
                alert(data.respDesc);
            }
        },
        error:function () {
            alert("pass request error!")
        }

    });
}
/**
 * 渲染增加一个上传文件input
 */
function addMoreFile() {
    $("#fileList").append('<input type="file" name="file" /> ');
}
/**
 * 渲染删除一个上传文件input
 * 从后开始删除
 */
function removeMoreFile() {
    var list=$("#fileList").find('input');
    list.eq(list.length-1).remove()
    $("#fileList").remove();
}