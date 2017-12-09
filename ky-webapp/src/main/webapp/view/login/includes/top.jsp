<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>提示</title>
    <link rel="stylesheet" href="css/initPwd.css"/>
    <script src="../vendor/jquery/dist/jquery.min.js"></script>
    <script src="../vendor/crypto-js/crypto-js.js"></script>
    <script src="../vendor/crypto-js/aes.js"></script>
    <script src="js/ics.js"></script>
    <script src="js/error.js"></script>
</head>
<body>


