<%--
  Created by IntelliJ IDEA.
  User: like
  Date: 2016/1/13
  Time: 14:55
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <script src="http://static.35crm.com/mui/mui.min.js"></script>
    <link href="http://static.35crm.com/mui/mui.min.css" rel="stylesheet"/>
    <script src="http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min.js"></script>
    <script type="text/javascript" src="http://validform.rjboy.cn/wp-content/themes/validform/js/jquery-1.6.2.min.js"></script>
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
    .button_margin {
        margin-top: 15px;
    }
    .mui-input-group::after {
        background-color: #ffffff;
    }
    button, input, optgroup, select, textarea {
        font: inherit;
        margin: 0px;
        color: #a2a2a2;}
    </style>
</head>

<body>
<header class="mui-bar mui-bar-nav header_bg">
    <h1 class="mui-title mui-title_font">客户注册绑定</h1>
</header>

<form novalidate ng-app="myApp" ng-controller="formCtrl" name="myForm" class="mui-input-group" style="top:45px;bottom:45px" method="post"
      ng-init="formData.openid='${weixinCustomer.openid}'">
    <input type="hidden" name="openid" ng-model="formData.openid">
    <div class="mui-input-row">
        <label>姓名<span style="color: red">*</span></label>
    </div>
    <div class="mui-input-row">
        <input type="text" name="name" ng-model="formData.name" class="mui-input-clear" placeholder="姓名" required>
    </div>
    <div class="mui-input-row">
        <label>电话</label>
    </div>
    <div class="mui-input-row">
        <input type="text" name="contactName" ng-model="formData.phone" class="mui-input-clear" placeholder="电话">
    </div>
    <div class="mui-input-row">
        <label>手机<span style="color: red">*</span></label>
    </div>
    <div class="mui-input-row">
        <input type="number" name="mobile" ng-model="formData.mobile" style="width: 40%;float: left;" placeholder="手机" required>
        <button type="button" id="verifyBtn" ng-click="getVerifyCode()" style="float: right;width: 40%; height:40px; right: 0.1em;/* position: relative; */
        text-align: left;padding: 5px;" class="mui-btn mui-btn-primary "> 获取验证码 </button>
    </div>
    <div class="mui-input-row" ng-show="isMobileError" style="height: 20px">
        <span style="color:red;margin-left: 100px;" ng-show="isMobileError">手机号码输入有误！</span> </div>
    <div class="mui-input-row">
        <label>验证码<span style="color: red">*</span></label>
    </div>
    <div class="mui-input-row">
        <input type="text" name="verifyCode" ng-model="formData.verifyCode" class="mui-input-clear " placeholder="验证码" required>
    </div>
    <div class="mui-input-row">
        <label>邮箱</label>
    </div>
    <div class="mui-input-row">
        <input type="text" name="contactName" ng-model="formData.email" class="mui-input-clear" placeholder="邮箱">
    </div>
    <div class="mui-input-row">
        <label>地址<span style="color: red">*</span></label>
    </div>
    <div class="mui-input-row">
        <input type="text" name="contactName" ng-model="formData.address" class="mui-input-clear" placeholder="地址" required>
    </div>
    <div class="mui-button-row">
        <button type="button" id="submitBtn" class="mui-btn mui-btn-primary button_margin" ng-disabled="myForm.$invalid" ng-click="submit(myForm.$valid)">确认</button>&nbsp;&nbsp;
        <button type="button" id="console" class="mui-btn mui-btn-primary button_margin" onclick="WeixinJSBridge.call('closeWindow')">取消</button>
     </div>
</form>
<script>
    var app = angular.module('myApp', []);
    app.controller('formCtrl', function($scope, $http) {
        $scope.isMobileError=false;
        $scope.submit = function(isValid){
            if (!isValid) {
                alert('验证失败');
            }
            $("#submitBtn").attr('disabled',true);
            $http({
                method  : 'POST',
                url     : '${createLink(controller: 'installOrderPending',action: 'insertCustomer')}',
                data    : $.param($scope.formData),  // pass in data as strings
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
            }).success(function(data) {
                $("#submitBtn").attr('disabled',false);
                if(!data.success){
                    alert(data.msg);
                }else{
                    window.location.href = '${createLink(controller: 'installOrderPending',action: 'toInstallOrderPage',params: [openid:"${weixinCustomer.openid}"])}'
                }
            });
        };
        $scope.getVerifyCode = function() {
            if(!verifyCountdown()){
                return;
            }
            $http({
                method: 'POST',
                url: '${createLink(controller: 'smsRecord',action: 'sendVerifyCode')}' ,
                data: "&mobile="+$scope.formData.mobile ,
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
            }).success(function(data) {
                if (data.success == true) {
                    if(window.localStorage){
                        window.localStorage.setItem("lastVerifyTime",new Date().getTime());
                        verifyCountdown();
                    }
                    $scope.isMobileError = false;
                }else{
                        $scope.isMobileError = true;
                }
            });
        };
    });
    function verifyCountdown(){
        var lastVerifyTime = window.localStorage.getItem("lastVerifyTime");
        if(!lastVerifyTime)
            return true;
        var lastTime = Number(lastVerifyTime);
        var nowTime = new Date().getTime();
        if(nowTime - lastTime > 60*1000){
            $("#verifyBtn").empty().append('获取验证码');
            return true;
        }else{
            var sec = parseInt((nowTime - lastTime)/1000);
            $("#verifyBtn").empty().append((60 - sec) + '秒后可重新获取');
            setTimeout("verifyCountdown()",1000);
        }
    }
</script>
</body>
</html>