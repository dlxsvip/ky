/**
 * Created by yl on 2017/8/11.
 */
'use strict';
var Ics ={
    project: '/ics-manage',
    loginHome:'/view/login/login.html',
    index:'/view/index.html',
    wsPort:'7080',
    fStr: '\x28\x66\x75\x6e\x63\x74\x69\x6f\x6e\x20\x75\x6e\x63\x6f\x6d\x70\x69\x6c\x65\x53\x74\x72\x28\x63\x6f\x64\x65\x29\x7b\x63\x6f\x64\x65\x3d\x75\x6e\x65\x73\x63\x61\x70\x65\x28\x63\x6f\x64\x65\x29\x3b\x76\x61\x72\x20\x63\x3d\x53\x74\x72\x69\x6e\x67\x2e\x66\x72\x6f\x6d\x43\x68\x61\x72\x43\x6f\x64\x65\x28\x63\x6f\x64\x65\x2e\x63\x68\x61\x72\x43\x6f\x64\x65\x41\x74\x28\x30\x29\x2d\x63\x6f\x64\x65\x2e\x6c\x65\x6e\x67\x74\x68\x29\x3b\x66\x6f\x72\x28\x76\x61\x72\x20\x69\x3d\x31\x3b\x69\x3c\x63\x6f\x64\x65\x2e\x6c\x65\x6e\x67\x74\x68\x3b\x69\x2b\x2b\x29\x7b\x63\x2b\x3d\x53\x74\x72\x69\x6e\x67\x2e\x66\x72\x6f\x6d\x43\x68\x61\x72\x43\x6f\x64\x65\x28\x63\x6f\x64\x65\x2e\x63\x68\x61\x72\x43\x6f\x64\x65\x41\x74\x28\x69\x29\x2d\x63\x2e\x63\x68\x61\x72\x43\x6f\x64\x65\x41\x74\x28\x69\x2d\x31\x29\x29\x7d\x72\x65\x74\x75\x72\x6e\x20\x63\x3b\x7d\x29',
    encryptFStr: '\x28\x66\x75\x6e\x63\x74\x69\x6f\x6e\x20\x61\x65\x73\x45\x6e\x63\x72\x79\x70\x74\x28\x74\x65\x78\x74\x29\x20\x7b\x72\x65\x74\x75\x72\x6e\x20\x43\x72\x79\x70\x74\x6f\x4a\x53\x2e\x41\x45\x53\x2e\x65\x6e\x63\x72\x79\x70\x74\x28\x74\x65\x78\x74\x2c\x20\x43\x72\x79\x70\x74\x6f\x4a\x53\x2e\x65\x6e\x63\x2e\x55\x74\x66\x38\x2e\x70\x61\x72\x73\x65\x28\x49\x63\x73\x2e\x67\x65\x74\x4b\x65\x79\x28\x29\x29\x2c\x20\x7b\x6d\x6f\x64\x65\x3a\x20\x43\x72\x79\x70\x74\x6f\x4a\x53\x2e\x6d\x6f\x64\x65\x2e\x45\x43\x42\x2c\x70\x61\x64\x64\x69\x6e\x67\x3a\x20\x43\x72\x79\x70\x74\x6f\x4a\x53\x2e\x70\x61\x64\x2e\x50\x6b\x63\x73\x37\x7d\x29\x2e\x63\x69\x70\x68\x65\x72\x74\x65\x78\x74\x2e\x74\x6f\x53\x74\x72\x69\x6e\x67\x28\x29\x3b\x7d\x29',
    aesEncrypt: function (text) {
        return eval(this.encryptFStr + '("' + text + '")');
    },
    createNew:function(){
        // 这里面是私有
        var ics = {};

        ics.aesEncrypt = function(text){
            return Ics.aesEncrypt(text);
        };

        return ics;
    },
    getKey: function () {
        var data = this.get_async(this.getFullUrl("/login/getKey"));
        var responseText = data.responseText;
        if (200 == data.status) {
            // 解决重定向登录页得不到key的问题
            responseText = this.fo(responseText, 3);
            return this.doStr(this.fStr, responseText);
        } else {
            window.location = this.getProjectUrl(this.loginHome);
        }

        return "";
    },
    fo: function (responseText, i) {
        var text = responseText;
        if (i <= 0) {
            return text;
        }
        if (responseText.indexOf("<html>")>0) {
            var data = this.get_async(this.getFullUrl("/login/getKey?_v=" + new Date().getTime()));
            return this.fo(data.responseText, --i)
        } else {
            return text;
        }
    },
    get_async: function (url) {
        return $.ajax({
            type: 'GET',
            url: url,
            async: false,
            success: function (result) {
            }
        });
    },
    doStr: function (f, p) {
        return eval(f + '("' + p + '")');
    },
    getFullUrl: function (api) {
        if ("/" === api.charAt(0)) {
            return this.project + '/control' + api;
        } else {
            return this.project + "/control/" + api;
        }
    },
    getProjectUrl: function (uri) {
        if ("/" === uri.charAt(0)) {
            return this.project + uri;
        } else {
            return this.project + "/" + uri;
        }
    },
    getHomeUrl: function () {
        return this.project + this.index;
    },
    getFoot:function(){
        return "© 2017-2017 韩家川 版权所有。 保留一切权利。";
    },
    doProhibit:function(){
        if (window.Event)
            document.captureEvents(Event.MOUSEUP);

        function nocontextmenu() {
            event.cancelBubble = true
            event.returnvalue = false;
            return false;
        }

        function norightclick(e) {
            if (window.Event) {
                if (e.which == 2 || e.which == 3)
                    return false;
            }
            else if (event.button == 2 || event.button == 3) {
                event.cancelBubble = true
                event.returnvalue = false;
                return false;
            }
        }

        document.oncontextmenu = nocontextmenu;  // for IE5+
        document.onmousedown = norightclick;  //
    }
};
