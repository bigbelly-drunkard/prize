var host = "http://localhost:8080/";
/**
 * 静默请求
 * @param url
 * @param param
 * @param callback
 */
function call_slient(url, param, callback) {
    call_common(url, param, true, callback);

}
/**
 * 非静默请求
 * @param url
 * @param param
 * @param callback
 */
function call(url, param, callback) {
    call_common(url, param, false, callback);
}
/**
 * 通用网络请求方法
 * @param url
 * @param param
 * @param callback
 */
function call_common(url, param, slientFlag, callback) {

    $.ajax({
        url: host+url,
        type: 'post',
        data: param,
        dataType: 'json',
        headers: {},
        success: function (res) {
            if (!res.isSuccess) {
                if (!slientFlag) {
                    showError(res.code);
                }
                return;
            }
            callback(res.data);

        }, fail: function (res) {
            if (!slientFlag) {
                showError();
            }
        }
    });
}

function showError(code) {
    if (code) {
        $("#error-msg").html(getMsg(code));
        $("#error").show();
        $("#mask").show();
    } else {
        $("#network").show();
        $("#mask").show();
    }

}
function closeAlert() {
    $("#mask").hide();
    $("#error").hide();
    $("#network").hide();
}
function getMsg(code) {
    var msg = "网络出小差了，请稍后再试";
    if (!code) {
        return msg;
    }
    switch (code) {
        case '':
            break;
        default:
            break;
    }
    return msg;

}