/**
 * Created by chendatao on 2019/4/18.
 */
$(function () {
    initFormValidator();

});
function switchOver($this) {
    $($this).addClass('black');
}
function switchOut($this) {
    $($this).removeClass('black');
}
function foucs($this) {
    $($this).siblings('a').children('div').removeClass('foucs');
    $($this).children('div').addClass('foucs');

}
function logOut() {
    $.ajax({
        url : "/user/logout",
        type : "POST",
        data : {},
        dataType : "json",
        success : function(data, textStatus) {
            if (data.status) {
                location.href = '/';
                return;
            }
            alert(data.msg);

        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        },
        complete : function(XMLHttpRequest, textStatus) {
        }
    });
}
function resetPwd() {
    $('#add-approval-control-dialog').modal('show');
    $('#add-approval-control-dialog').on('shown.bs.modal', function () {
        $("form[id=add-approval-control-form]  input[name=oldPwd]").val("");
        $("form[id=add-approval-control-form]  input[name=newPwd]").val("");
        $("form[id=add-approval-control-form]").bootstrapValidator('resetForm', true);
    });
}
function confirmResetPwd() {
    $.ajax({
        url : "/user/modifyPwd",
        type : "POST",
        data : $("#add-approval-control-form").serialize(),
        dataType : "json",
        success : function(data, textStatus) {
            if (data.status) {
                alert(data.msg);
                location.href = '/';
                return;
            }
            alert(data.msg);

        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        },
        complete : function(XMLHttpRequest, textStatus) {
        }
    });
}