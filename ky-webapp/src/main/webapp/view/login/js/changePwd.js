/**
 * Created by yl on 2017/7/29.
 */

// 页面准备好后在运行
$(document).ready(function () {

    var ics = Ics.createNew();

    // 屏蔽鼠标右键
    Ics.doProhibit();

    // 登录按钮绑定回车时间
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            $("#changePwd").click();
        }
    });
    function changeBtnStatus(){
        if ($("#password").val() && $("#newPassword").val() && $("#confirmNewPassword").val()) {
            $("#changePwd").attr("disabled", false);
        } else {
            $("#changePwd").attr("disabled", true);
        }
    }
    $("#password").blur(function(){
        if ($(this).val()) {
            $("#error_7").hide();
            $("#error_1").hide();
        } else {
            $("#error_1").show();
        }
        changeBtnStatus();
    });
    var regPassword = /(?=^.{8,20}$)(?!^\d*$)(?!^[\W_]*$)(?!^[a-zA-Z]*$).*$/;
    $("#newPassword").blur(function(){
        var newPassword =  $(this).val();
        if (newPassword) {
            $("#error_2").hide();
            if(regPassword.test(newPassword)){
                $("#error_3").hide();
            }else{
                $("#error_3").show();
            }

        } else {
            $("#error_2").show();
        }
        changeBtnStatus();
    });
    $("#confirmNewPassword").blur(function(){
        var confirmNewPassword = $(this).val();
        if (confirmNewPassword) {
            $("#error_4").hide();
            if(regPassword.test(confirmNewPassword)){
                $("#error_5").hide();
                if ($("#newPassword").val() != confirmNewPassword) {
                    $("#error_6").show();
                } else {
                    $("#error_6").hide();
                }
            }else{
                $("#error_5").show();
            }
        } else {
            $("#error_4").show();
        }
        changeBtnStatus();
    });


    $("#changePwd").click(function () {

        var data = {
            oldPassword: ics.aesEncrypt($("#password").val()),
            newPassword: ics.aesEncrypt($("#newPassword").val())
        };
        $.ajax({
            type: 'POST',
            url: Ics.getFullUrl("/user/updatePassword"),
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8',
            dataType: "json",
            success: function (result) {
                if ("success" === result.result) {
                    window.location = Ics.getProjectUrl("/view/index.html");
                } else {
                    $("#error_7").show();
                }
                console.log(result.result);
            }
        });

        /*$.post("/ics-manage/control/user/updatePassword", JSON.stringify(data), function (result) {
         if ("success" === result.result) {
         window.location = "/ics-manage/view/index.html";
         } else {
         window.location = "/ics-manage/view/login/changePwd.html";
         }
         console.log(result.result);
         },"json");*/
    });

});
