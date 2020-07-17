function initFormValidator() {
var $addApprovalControlForm = $("#add-approval-control-form");
    $addApprovalControlForm.bootstrapValidator({
        message: '表单输入项格式不正确',
        fields: {

            oldPwd: {
                message: '密码格式不正确',
                validators: {
                    stringLength: {
                        min: 6,
                        message: '密码长度不能小于6位'
                    }
                }
            }, newPwd: {
                message: '密码格式不正确',
                validators: {
                    stringLength: {
                        min: 6,
                        message: '密码长度不能小于6位'
                    }
                }
            }

        }
    });

}