//对字符串进行加密
function compileStr(code) {
    var c = String.fromCharCode(code.charCodeAt(0) + code.length);
    for (var i = 1; i < code.length; i++) {
        c += String.fromCharCode(code.charCodeAt(i) + code.charCodeAt(i - 1));
    }

    return escape(c);
}


//字符串进行解密
function uncompileStr(code) {
    code = unescape(code);
    var c = String.fromCharCode(code.charCodeAt(0) - code.length);
    for (var i = 1; i < code.length; i++) {
        c += String.fromCharCode(code.charCodeAt(i) - c.charCodeAt(i - 1));
    }

    return c;
}


// str -> 16
function _s_2_16(str) {
    var s = "";
    for (var i = 0; i < str.length; i++) {
        s += "\\x" + str.charCodeAt(i).toString(16);
    }
    return s;
}