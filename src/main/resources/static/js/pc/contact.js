/**
 * Created by chendatao on 2018/11/20.
 */

function submitContent() {
    var content = '';
    content = content + '姓名：' + $("#name").val();
    content = content + '   邮箱：' + $("#email").val();
    var val = $("#content").val();
    if (val&&val.length > 200) {
        content = content + '   内容：' + val.substr(0, 200);

    } else {
        content = content + '   内容：' + $("#content").val();
    }
    $.ajax({
        url: '../feedBack/insert',
        type: 'post',
        dataType: 'json',
        data: {'content': content},
        success: function (data) {
            alert('我们已经收到您的提交内容，稍候将会有客服人员联系您，请保持联系方式畅通。');
            location.href ="/";
        },
        error: function () {
            alert('我们已经收到您的提交内容，稍候将会有客服人员联系您，请保持联系方式畅通。');
            location.href ="/";
        }
    });
}
