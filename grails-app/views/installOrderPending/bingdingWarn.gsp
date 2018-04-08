<%--
  Created by IntelliJ IDEA.
  User: like
  Date: 2016/1/15
  Time: 15:02
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
    <link href="http://static.35crm.com/mui/mui.min.css" rel="stylesheet"/>
    <title></title>
<style>
    body {
        font-family: '微软雅黑';
        font-size: 16px;
        font-size: 100%;
        font-size: 1em;
        background-color: #ffffff;
    }
    input, select, textarea {
        font-family: '微软雅黑';
        font-size: 16px;
    }
    .header_bg {
        background-color: #3285dd;
    }
    .mui-title_font {
        color: #fff;
    }
    .mui-input_bg {
        background-color: #f3f3f3;
        border-top-style: none;
        border-right-style: none;
        border-bottom-style: none;
        border-left-style: none;
    }
    .mui-input-group .mui-input-row:after {
        position: absolute;
        right: 0;
        bottom: 0;
        left: 15px;
        height: 0px;
        content: '';
        -webkit-transform: scaleY(.5);
        transform: scaleY(.5);
    }
    .mui-input-group input, .mui-input-group textarea {
        margin-bottom: 0px;
        border: 0px none;
        border-radius: 0px;
        background-color: #f6f6f6;
        box-shadow: none;
        position: absolute;
        left: 15px;
    }
    .mui-bar-nav {
        top: 0;
        -webkit-box-shadow: 0 1px 6px #ccc;
        box-shadow: 0 0px 0px #ccc
    }
    .mui-input-group {
        margin-top: -2px;
        padding-top: 24px;
        padding-left: 9px;
        padding-right: 24px;
        padding-bottom: 24px;
    }
    .mui-input-row {
        margin-top: 8px;
    }
    .mui-btn-primary, input[type="submit"] {
        color: #FFF;
        border: 1px solid #505050;
        background-color: #505050;
    }
    .mui-btn.mui-disabled, .mui-btn:disabled, button.mui-disabled, button:disabled, input.mui-disabled[type="button"], input[type="button"]:disabled, input.mui-disabled[type="reset"], input[type="reset"]:disabled, input.mui-disabled[type="submit"], input[type="submit"]:disabled {
        opacity: 1;
        height: 40px;
    }
    .button_margin {
        margin-top: 15px;
    }
    .button_margin_r {
        margin-right: 30px;
    }
    .mui-input-group::after {
        background-color: #ffffff;
    }
    .regedit{
        background-image: url(http://static.35crm.com/mui/images/regedit_01.jpg);
        background-position: center 20%;
        height: 250px;
        width: 100%;
        background-repeat: no-repeat;
    }
    .regedit_text{
        width:100%;
        display:block;
        text-align:center;
        position:relative;
        top:85%;
    }
    </style>
</head>

<body>
<header class="mui-bar mui-bar-nav header_bg">
    <h1 class="mui-title mui-title_font">注册提醒</h1>
</header>
<div class="regedit">
    <span class="regedit_text">你还没有注册，请先注册!</span>
</div>

<div class="mui-button-row">
    <button type="button" class="mui-btn mui-btn-primary button_margin button_margin_r" onclick="toBinding()">注册</button>
    <button type="button" class="mui-btn mui-btn-primary button_margin" onclick="cancle()">取消</button>
    &nbsp;&nbsp;
</div>
<script type="text/javascript">
    function toBinding(){
        window.location.href = '${createLink(controller: 'installOrderPending',action: 'toBinding',params: [openid:"${weixinCustomer.openid}"])}';
    }

    function cancle(){
        WeixinJSBridge.call('closeWindow');
    }
</script>
</body>
</html>