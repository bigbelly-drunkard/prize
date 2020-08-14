/**
 * Created by chendatao on 2018/8/13.
 */
var cssV = 1;
document.write('<link href="../static/css/common/common.css?v=' + cssV + '" rel="stylesheet" type="text/css">');
//alert弹窗
document.write('<div class="mask hide" id="mask"></div> <div class="alertMsg hide" id="network"> <svg class="icon" aria-hidden="true" color="#aaaaaa">' +
    ' <use xlink:href="#icon-wangluo"></use> </svg> <span>网络出小差了，请稍后再试</span>' +
    ' <div class="btn" onclick="closeAlert();">知道了</div> </div>');
document.write('<div class="alertMsg hide" id="error"> <svg class="icon" aria-hidden="true" color="#aaaaaa">' +
    ' <use xlink:href="#icon-error"></use> </svg> <span id="error-msg">msg</span>' +
    ' <div class="btn" onclick="closeAlert();">知道了</div> </div>');