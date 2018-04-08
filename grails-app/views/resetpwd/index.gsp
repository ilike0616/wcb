<%--
  Created by IntelliJ IDEA.
  User: like
  Date: 2015/8/4
  Time: 17:18
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>销售无忧,找回密码,移动crm方案,客户管理软件</title>
<meta name="description" content="销售无忧是傲融软件旗下主要产品,将销售软件,移动crm和云服务相结合,助力企业提高销售智能化,信息化,手机版销售软件免费下载,客户管理软件下载,在线试用 " />
<meta name="keywords" content="销售无忧,移动crm系统,销售软件,销售管理软件,客户管理软件排名" />
    <link rel="stylesheet" href="http://www.xiaoshouwuyou.com/js/Validform/css/style.css" type="text/css" media="all"/>
    <link href="http://www.xiaoshouwuyou.com/css/style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript"
            src="http://validform.rjboy.cn/wp-content/themes/validform/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="http://www.xiaoshouwuyou.com/js/Validform/Validform_v5.3.2_min.js"></script>
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
    </script>
</head>

<body style="background-color:#fff;">
<div class="header_login">
    <div class="logo"><a href="http://www.xiaoshouwuyou.com/"></a>
        <span class="logo_title"><img src="http://www.xiaoshouwuyou.com/images/logo_title.gif" width="276" height="27"/></span>
        <span class="return_home"><a href="http://www.xiaoshouwuyou.com/index.html"><img
                src="http://www.xiaoshouwuyou.com/images/logo_return.gif" width="43" height="43" border="0"/></a></span>
    </div>
</div>

<div class="clear"></div>

<div class="sub_banner6"><img src="http://www.xiaoshouwuyou.com/images/sub_banner_08.jpg"/></div>

<div class="registered_main_box">
    <div class="registered_main_title">找回密码</div>

    <div class="return_passport_bar"></div>

    <div class="registered_main_form">
        <div class="registered_main_form_left"><script>document.write("<iframe style='padding: 0px; width: 100%;' height='385' src='http://www.xiaoshouwuyou.com/iframe/registered/01.html' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='no'></iframe>");</script>
        </div>
        <div class="registered_main_form_right">
            <form class="regForm" method="post" action="${createLink(controller: 'resetpwd', action: 'resetpwd')}">
                <table border="0" cellspacing="5" cellpadding="0">
                    <tr>
                        <td colspan="2">第一步：身份验证</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="text" id="mobile" name="mobile" placeholder="请输入手机号码" ajaxurl="${createLink(controller: 'resetpwd',action: 'mobileIsExist')}" datatype="m"
                                   errormsg="请输入正确的手机号码！" nullmsg="手机号码必须填写！"/>
                            <div class="Validform_checktip" style="display:none"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="text" name="captcha" placeholder="请输入识别码" datatype="*"
                                   ajaxurl="${createLink(controller: 'captcha', action: 'validate')}" errormsg="验证码是4位"
                                   nullmsg="识别码必须输入！" maxlength="4"/>
                            <div class="Validform_checktip" style="display:none"></div>
                        </td>
                        <td>
                            <img src="${createLink(controller: 'captcha')}" width="79" id="imageCaptcha" height="36"
                                 onclick="reloadCaptcha();"/>
                            <img src="http://www.xiaoshouwuyou.com/images/registered_main_sbm2.gif" width="43"
                                 onclick="reloadCaptcha();" height="36" onclick="reloadCaptcha();"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="text" id="verify" name="verify" placeholder="请输入手机验证码" datatype="*"
                                   nullmsg="手机验证码必须输入！"/>
                            <div class="Validform_checktip" style="display:none"></div>
                        </td>
                        <td><span id="securityCode" class="identifying_code">获取验证码</span></td>
                    </tr>
                    <tr>
                        <td colspan="2">如果一段时间没收到，请重新获取。</td>
                    </tr>
                    <tr>
                        <td colspan="2"><img id="btn_submit"
                                             src="http://www.xiaoshouwuyou.com/images/registered_main_button01.gif" width="90"
                                             height="34" style="margin-left:16px;"/>
                            <i>无法登录：<a href="${resource(file: 'login1.html')}">查看帮助</a></i></td>
                    </tr>
                </table>
            </form>
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
    $(function () {
        var reg = $(".regForm").Validform({
            tiptype: 2,
            btnSubmit: "#btn_submit",
            tiptype: function (msg, o, cssctl) {
                if (!o.obj.is("form")) {
                    var objtip = o.obj.siblings(".Validform_checktip");
                    if (o.type == 3) {
                        cssctl(objtip, o.type);
                        objtip.text(msg);
                        objtip.show();
                    } else if (o.type == 2) {
                        cssctl(objtip, 4);
                        objtip.text("");
                        objtip.hide();
                    }
                }
            },
            ajaxPost: false
        });
        $("#securityCode").bind("click", function (event) {
            if (!verifyCountdown()) {
                return;
            }
            reg.ignore("#verify");
            if (reg.check(false) == true) {
                var moboleFlag = false;
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: 'resetpwd/mobileIsExist' ,
                    data: {param:$("#mobile").val()} ,
                    async:false,
                    success: function(data) {
                        if (data.status == 'y') {
                            moboleFlag = true;
                        }
                    }
                });
                if(!moboleFlag){
                    return;
                }
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: 'smsRecord/sendVerifyCode',
                    data: {mobile: $("#mobile").val()},
                    async:false,
                    success: function (data) {
                        if (data.success == true) {
                            if (window.localStorage) {
                                window.localStorage.setItem("lastVerifyTime", event.timeStamp);
                                verifyCountdown();
                            }
                        } else {
                            if(data.limit){
                                var verifyTip = $("#verify").parent().find('.Validform_checktip');
                                verifyTip.addClass("Validform_wrong");
                                verifyTip.text(data.msg);
                                verifyTip.show();
                            }
                        }
                    }
                });
            }
            reg.unignore("#verify");
        });
        verifyCountdown();
        if ("${errors}") {
            $("#btn_submit").click();
        }
    });
    function verifyCountdown() {
        var lastVerifyTime = window.localStorage.getItem("lastVerifyTime");
        if (!lastVerifyTime)
            return true;
        var lastTime = Number(lastVerifyTime);
        var nowTime = new Date().getTime();
        if (nowTime - lastTime > 60 * 1000) {
            $("#securityCode").empty().append('获取验证码');
            return true;
        } else {
            var sec = parseInt((nowTime - lastTime) / 1000);
            $("#securityCode").empty().append((60 - sec) + '秒后可重新获取');
            setTimeout("verifyCountdown()", 1000);
        }
    }
    function reloadCaptcha() {
        $('#imageCaptcha').attr('src', "${createLink(controller: 'captcha')}?math=" + new Date().getTime());
    }

</script>
</body>
</html>