<%--
  Created by IntelliJ IDEA.
  User: like
  Date: 2015/7/30
  Time: 17:36
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>销售无忧,网站注册,移动crm方案,客户管理软件</title>
<meta name="description" content="销售无忧是傲融软件旗下主要产品,将销售软件,移动crm和云服务相结合,助力企业提高销售智能化,信息化,手机版销售软件免费下载,客户管理软件下载,在线试用 " />
<meta name="keywords" content="销售无忧,移动crm系统,销售软件,销售管理软件,客户管理软件排名" />
    <link rel="stylesheet" href="http://www.xiaoshouwuyou.com/js/Validform/css/style.css" type="text/css" media="all" />
    <link href="http://www.xiaoshouwuyou.com/css/style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="http://validform.rjboy.cn/wp-content/themes/validform/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="http://www.xiaoshouwuyou.com/js/Validform/Validform_v5.3.2_min.js"></script>
    <script type="text/javascript" src="${resource(dir: 'js',file: 'jquery.cityselect.js')}"></script>
    <style type="text/css">
    .mask {
        position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #777;
        z-index: 100; left: 0px;display: none;
        opacity:0.9; -moz-opacity:0.5;
    }
    </style>
    <script type="text/javascript">
        //placeholder IE8
        var _placeholderSupport = function() {
            var t = document.createElement("input");
            t.type = "text";
            return (typeof t.placeholder !== "undefined");
        }();


        window.onload = function() {
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
                    oTextbox.onfocus = function() {
                        this.style.color = this.getAttribute("old_color");
                        if (this.value === curPlaceholder)
                            this.value = "";
                    };
                    oTextbox.onblur = function() {
                        if (this.value === "") {
                            this.style.color = "#c0c0c0";
                            this.value = curPlaceholder;
                        }
                    }
                }
            }
        }
        function showMask(){
            $("#mask").css("height",$(document).height());
            $("#mask").css("width",$(document).width());
            $("#mask").show();
        }
        //隐藏遮罩层
        function hideMask(){
            $("#mask").hide();
        }
    </script></head>

<body style="background-color:#fff;">
<div class="header_login">
    <div class="logo"><a href="http://www.xiaoshouwuyou.com/"></a>
        <span class="logo_title"><img src="http://www.xiaoshouwuyou.com/images/logo_title.gif" width="276" height="27" /></span>
        <span class="return_home"><a href="index.html"><img src="http://www.xiaoshouwuyou.com/images/logo_return.gif" width="43" height="43" border="0"/></a></span>
    </div>
</div>
<div class="clear"></div>
<div class="sub_banner6"><img src="http://www.xiaoshouwuyou.com/images/sub_banner_08.jpg"/></div>
<div class="registered_main_box">
    <div class="registered_main_title">欢迎注册</div>
    <div class="registered_main_bar"><img src="http://www.xiaoshouwuyou.com/images/registered_main_bar2.gif" width="721" height="68" /></div>
    <div class="registered_main_form">
        <div class="registered_main_form_left"><script>document.write("<iframe style='padding: 0px; width: 100%;' height='385' src='http://www.35crm.com/iframe/registered/01.html' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='no'></iframe>");</script>
        </div>
        <div class="registered_main_form_right">
            <form class="regForm" method="post" action="${createLink(controller: 'register',action: 'init')}">
            <table border="0" cellspacing="5" cellpadding="0">
                <tr>
                    <td colspan="2">请填写以下信息</td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input  type="text" id="name" name="name" value="${params.name}" placeholder="公司名称" ajaxurl="${createLink(controller: 'register',action: 'uniqueUserName')}" datatype="*2-15"  errormsg="公司名称范围在2~15位之间！"  nullmsg="公司名称必须填写！"/>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input  type="text" id="employeeName" name="employeeName" value="${params.employeeName}" placeholder="联系人" datatype="*2-15"  errormsg="姓名范围在2~15位之间！"  nullmsg="姓名必须填写！"/>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input  type="text" id="email" name="email" value="${params.email}" placeholder="邮箱" ajaxurl="${createLink(controller: 'register',action: 'uniqueUserEmail')}" datatype="e"  errormsg="请输入正确的邮箱地址！"  nullmsg="邮箱必须填写！"/>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input  type="text" id="userId" name="userId" value="${params.userId}" placeholder="公司账号" ajaxurl="${createLink(controller: 'register',action: 'uniqueUserId')}" datatype="/^[\w]{6,16}$/"  errormsg="请输入6~16位字符、数字、下划线！"  nullmsg="公司账号必须填写！"/>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input  type="password" id="password" name="password"  placeholder="请输入密码" datatype="*6-15" errormsg="密码范围在6~15位之间！" nullmsg="密码必须填写！"/>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                    <td>
                        <label style=" position: relative; top: 8px; display: inline-block;"><input id="showPassword" type="checkbox" />显示密码</label>
                    </td>
                </tr>

              %{--  <tr id="city_1"  style="display:none">
                    <td>
                        <select id="province"  class="prov" name="province" style="width: 137px;" datatype="*" nullmsg="请选择省份！"></select>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                    <td>
                        <select id="city" class="city" name="city"  disabled="disabled" style="width: 137px;" datatype="*" nullmsg="请选择城市！"></select>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                </tr>--}%
                <!--
                <tr>
                    <td colspan="2"><select name="" style="width:99%;" datatype="*" nullmsg="请选择版本！">
                        <option value="">产品版本选择</option>
                        <option value="1">35CRM-销售通</option>
                        <option value="2">35CRM-外出宝</option>
                        <option value="3">35CRM-呼叫中心</option>
                    </select>
                        <div class="Validform_checktip" style="display:none"></div>
                    </td>
                </tr>-->
                <tr>
                    <td colspan="2">点击完成，表示您同意并愿意遵守本公司的使用条款。</td>
                </tr>
                <tr>
                    <td colspan="2" style="padding-top:10px;">
                        <img id="btn_submit" src="http://www.xiaoshouwuyou.com/images/registered_main_button02.gif" width="91" height="34" style="margin-left:16px;" />
                        <i>已有帐号：<a href="${resource(file: 'login1.html')}">直接登录</a></i></td>
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
    <div id="mask" class="mask">
        <div class="openWin">
            <span class="loading">
                <img src="http://www.xiaoshouwuyou.com/images/loading .gif" width="105" height="104" />
                <span class="loading_title">系统正在为您初始化，请稍候...</span>
            </span>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        $("#city_1").citySelect({
            nodata:"none",
            required:false
        });
        var reg = $(".regForm").Validform({
            tiptype:2,
            btnSubmit:"#btn_submit",
            tiptype:function(msg,o,cssctl){
                if(!o.obj.is("form")){
                    var objtip=o.obj.siblings(".Validform_checktip");
                    if(o.type==3){
                        cssctl(objtip,o.type);
                        objtip.text(msg);
                        objtip.show();
                    }else if(o.type==2){
                        cssctl(objtip,4);
                        objtip.text("");
                        objtip.hide();
                    }
                }
            },
            ajaxPost:false
        });
        if("${errors}"){
            $("#btn_submit").click();
        }
        $("#btn_submit").bind("click",function(){
            if($(".regForm").Validform().check()) {
                showMask();
            }
        });

        $("#showPassword").change(function(){
            if($("#showPassword").prop("checked")){
                var p = document.getElementById("password");
                p.type='text';
            }else{
                var p = document.getElementById("password");
                p.type='password';
            }
        });
    });
</script>
</body>
</html>