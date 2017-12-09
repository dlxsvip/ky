/**
 * Created by yl on 2017/8/16.
 */
// 页面准备好后在运行
$(document).ready(function () {

    // 屏蔽鼠标右键
    Ics.doProhibit();

    //获取url中的参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        return null; //返回参数值
    }

    var error = getUrlParam('error');
    var msg = unescape(getUrlParam('msg'));

    if ('701' == error) {
        $("#heading").html("您的密码为系统初始密码，您必须修改密码");
        var url = Ics.getProjectUrl("/view/login/changePwd.html");
        $("#message").html("请<a href=\"" + url + "\">更改您的密码</a>");
    } else if ('702' == error) {
        $("#heading").html("您的账户未激活");
        var url = Ics.getProjectUrl("/view/login/login.html");
        $("#message").html("请<a href=\"" + url + "\">激活</a>");
    } else if ('703' == error) {
        $("#heading").html("您的账户被关禁闭");
        $("#message").html("改用户被禁闭，请" + msg + "分钟后再试");
    } else if ('704' == error) {
        $("#heading").html("您的账户密码即将到期");
        var url = Ics.getProjectUrl("/view/login/changePwd.html");
        var index = Ics.getHomeUrl();
        $("#message").html("请<a href=\"" + url + "\">更改您的密码</a> | <a href=\""+index+"\">暂不修改</a>");
    } else {
        $("#heading").html(error);
        $("#message").html(msg);
    }


    $("#foot").html(Ics.getFoot());
});
