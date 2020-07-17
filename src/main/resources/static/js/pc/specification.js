$(function(){
 
});

function onchangeMao() {
    if(location.hash){
        var target = $(location.hash);
        if(target.length==1){
            var top = target.offset().top;
            if(top >= 0){
                $('html,body').animate({scrollTop:top}, 1000);
            }
        }
    }
}