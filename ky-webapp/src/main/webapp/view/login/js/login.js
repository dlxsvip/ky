/**
 * Created by yl on 2017/7/29.
 */

// 页面准备好后在运行
$(document).ready(function () {
    'use strict';
    var ics = Ics.createNew();
    /*$("#userName, #password").bind("keyup", function (e) {
        if (e.keyCode == 13) {
            $('#submitLogin').focus();
            $("#submitLogin").select();
            $("#submitLogin").click();
        }
    });*/

    // 屏蔽鼠标右键
    Ics.doProhibit();
    /*$(document).bind("contextmenu",function(e){
        return false;
    });*/

    // 登录按钮绑定回车时间
    $(document).keydown(function(event){
        if(event.keyCode==13){
            $("#submitLogin").click();
        }
    });

    $("input[name='username'],input[name='password']").bind('input propertychange', function() {
        $("#error_1").hide();
    });

    $("#submitLogin").click(function () {
        var username = $("#j_username").val();
        var password = $("#j_password").val();
        var rememberMe = $("#remember").val();
        var data = {
            "username": username,
            "password": ics.aesEncrypt(password),
            "rememberMe":rememberMe
        };
        $.post(Ics.getProjectUrl("/security_check"), data, function (result) {
            if ("success" === result.result) {
                window.location = Ics.getProjectUrl("/view/index.html");
            } else if ('701' == result.errorMsg || '702' == result.errorMsg || '703' == result.errorMsg || '704' == result.errorMsg) {
                // 跳转到错误页面
                window.location = Ics.getProjectUrl("/view/login/errors.jsp?error=" + result.errorMsg + "&msg=" + escape(result.errorDetail));
            } else {
                var error = $("#error_1");
                error.text("");
                error.append(result.errorMsg);
                $("#error_1").show();
            }

            console.log(result.result);
        });
    });


});
