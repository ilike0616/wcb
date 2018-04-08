<%--
  Created by IntelliJ IDEA.
  User: like
  Date: 2015/8/5
  Time: 11:03
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>销售无忧,找回密码,移动crm方案,客户管理软件</title>
<meta name="description" content="销售无忧是傲融软件旗下主要产品,将销售软件,移动crm和云服务相结合,助力企业提高销售智能化,信息化,手机版销售软件免费下载,客户管理软件下载,在线试用 " />
<meta name="keywords" content="销售无忧,移动crm系统,销售软件,销售管理软件,客户管理软件排名" />
    <link href="http://www.xiaoshouwuyou.com/css/style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript"
            src="http://validform.rjboy.cn/wp-content/themes/validform/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript">
        //placeholder IE8
        var _placeholderSupport = function () {
            var t = document.createElement("input");
            t.type = "text";
            return (typeof t.placeholder !== "undefined");
        }();


        window.onload = function () {
            var arrInputs = document.getElementsByTagName("input");
            for (var i = 0; i < arrInputs.length; i++) {
                var curInput = arrInputs[i];
                if (!curInput.type || curInput.type == "" || curInput.type == "text")
                    HandlePlaceholder(curInput);
            }
        };

        function HandlePlaceholder(oTextbox) {
            if (!_placeholderSupport) {
                var curPlaceholder = oTextbox.getAttribute("placeholder");
                if (curPlaceholder && curPlaceholder.length > 0) {
                    oTextbox.value = curPlaceholder;
                    oTextbox.setAttribute("old_color", oTextbox.style.color);
                    oTextbox.style.color = "#c0c0c0";
                    oTextbox.onfocus = function () {
                        this.style.color = this.getAttribute("old_color");
                        if (this.value === curPlaceholder)
                            this.value = "";
                    };
                    oTextbox.onblur = function () {
                        if (this.value === "") {
                            this.style.color = "#c0c0c0";
                            this.value = curPlaceholder;
                        }
                    }
                }
            }
        }
    </script></head>

<body style="background-color:#fff;">
<div class="header_login">
    <div class="logo"><a href="http://www.xiaoshouwuyou.com/"></a>
        <span class="logo_title"><img src="http://www.xiaoshouwuyou.com/images/logo_title.gif" width="276" height="27"/></span>
        <span class="return_home">
            <a href="http://www.xiaoshouwuyou.com/index.html">
                <img src="http://www.xiaoshouwuyou.com/images/logo_return.gif" width="43" height="43" border="0"/></a>
        </span>
    </div>
</div>

<div class="clear"></div>

<div class="sub_banner6"><img src="http://www.xiaoshouwuyou.com/images/sub_banner_08.jpg"/></div>

<div class="registered_main_box">
    <div class="registered_main_title">找回密码</div>

    <div class="return_passport_bar"></div>

    <div class="registered_main_form">
        <div class="registered_main_form_left">
            <script>document.write("<iframe style='padding: 0px; width: 100%;' height='385' src='http://www.xiaoshouwuyou.com/iframe/registered/01.html' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='no'></iframe>");</script>
        </div>

        <div class="registered_main_form_right">
            <table border="0" cellspacing="5" cellpadding="0">
                <tr>
                    <td colspan="2">第三步：完成。</td>
                </tr>
                <tr>
                    <td colspan="2"><font class="info">新密码已经成功设置，<font id="second"></font>秒，系统会自动转入登录后页面，如需手动登录，请点击下方的登录按钮。</font></td>
                </tr>
                <tr>
                    <td colspan="2" style="padding-top:30px; text-align:center">
                            <img id="btn_login" src="http://www.xiaoshouwuyou.com/images/registered_main_button03.gif" width="90" height="34"/>
                    </td>
                </tr>
            </table>

        </div>
    </div>
</div>

<div class="clear"></div>

<div class="footer_box2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr align="center">
        <td width="14%"><a href="http://www.xiaoshouwuyou.com">首页</a></td>
        <td width="14%"><a href="http://www.xiaoshouwuyou.com/sales_management.html">销售无忧</a></td>
        <td width="14%"><a href="http://www.xiaoshouwuyou.com/field_management.html">行业方案</a></td>
        <td width="14%"><a href="http://www.xiaoshouwuyou.com/product_advantage.html">平台优势</a></td>
        <td width="14%"><a href="http://www.xiaoshouwuyou.com/trial_purchase.html">客户服务</a></td>
        <td width="14%"><a href="http://www.xiaoshouwuyou.com/cooperative_joining.html">合作加盟</a></td>
        <td><a href="http://www.xiaoshouwuyou.com/about_us.html">关于我们</a></td>
    </tr>
    <tr>
        <td colspan="8" align="center" style="font-size:16px;"><font style="font-family:Arial">Copyright 2003-2014 uniproud. All Rights Reserved. 上海傲融软件技术有限公司<br />ICP备案许可证号: 沪ICP备09024660号-3</td>
    </tr>
    <tr>
        <td colspan="8" align="center"><img src="http://www.xiaoshouwuyou.com/images/login_icon.jpg" width="57" height="56" /></td>
    </tr>

</table>

</div>
<script type="text/javascript">
    $(function(){
        $("#btn_login").bind('click',function(){
            login();
        });
        auotLogin(5);
    });
    function auotLogin(second){
        if(second>=0) {
            $("#second").empty().append(second);
        }
        second -= 1
        if(second >= 0){
            setInterval("auotLogin("+second+")",1000)
        }
        if(second<0){
            login();
        }
    }
    function login(){
        $.ajax({
            type:'POST',
            dataType:'JSON',
            url:"${createLink(controller: 'login',params: [email:employee.mobile,password:employee.password])}".replace("&amp;","&"),
            success:function(data){
                if(data.success)location.href = "${resource(file: 'u.html')}";
            }
        });
    }
</script>
</body>
</html>