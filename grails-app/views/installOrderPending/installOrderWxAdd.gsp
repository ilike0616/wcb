<%--
  Created by IntelliJ IDEA.
  User: like
  Date: 2016/1/12
  Time: 17:49
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <script src="http://static.35crm.com/mui/mui.min.js"></script>
    <link href="http://static.35crm.com/mui/mui.min.css" rel="stylesheet"/>
    <script src="http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min.js"></script>
    <script type="text/javascript" src="http://validform.rjboy.cn/wp-content/themes/validform/js/jquery-1.6.2.min.js"></script>
    <link href="http://static.35crm.com/mui/css/mui.picker.min.css" rel="stylesheet"/>
    <script src="http://static.35crm.com/mui/js/mui.picker.min.js"></script>
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
    textarea {
        height: auto;
        resize: none;
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
        overflow: visible;
        margin-right:15px;

    }
    .mui-btn-primary, input[type="submit"] {
        color: #FFF;
        border: 1px solid #505050;
        background-color: #505050;
    }
    .mui-btn-danger {
        color: #FFF;
        border: 1px solid #a9a9a9;
        background-color: #a9a9a9;
        height:40px;
    }
    .button_margin {
        margin-top: 15px;
    }
    .button_margin2 {
        margin-top: 120px;
    }
    .form_margin {
        margin-top: 42px;
    }

    .mui-input-group::after {
        background-color: #ffffff;
    }
    .mui-input-group::after {
        height: 0px;
    }
    button, input, optgroup, select, textarea {
        font: inherit;
        margin: 0px;
        color: #a2a2a2;}
    </style>
</head>

<body>
<header class="mui-bar mui-bar-nav header_bg">
    <h1 class="mui-title mui-title_font">安装单</h1>
</header>
<form class="mui-input-group form_margin" ng-app="myApp" name="myForm" ng-controller="formCtrl" method="post" novalidate
      ng-init="installOrder.openid='${weixinCustomer.openid}';installOrder.installAddress='${customer.address}'">
    <input type="hidden" name="openid" ng-model="installOrder.openid">
    <div class="mui-input-row">
        <label>预约时间<span style="color: red">*</span></label>
    </div>
    <div class="mui-input-row">
        <input type="text" id="appointmentDate" name="appointmentDate" ng-model="installOrder.appointmentDate" readonly="readonly" class="mui-input-clear" placeholder="预约时间">
    </div>
    <div class="mui-input-row">
        <label>安装地址<span style="color: red">*</span></label></div>
    <div class="mui-input-row">
        <input type="text" name="installAddress" ng-model="installOrder.installAddress" class="mui-input-clear" placeholder="安装地址" required>
    </div>
    <div class="mui-input-row">
        <label>安装描述<span style="color: red">*</span></label>
    </div>
    <div class="mui-input-row" style="height: 125px;">
        <textarea rows="5" name="description" ng-model="installOrder.description" class="mui-input-clear" placeholder="安装描述" required></textarea>
    </div>
    <div class="mui-input-row">
        <label>备注</label>
    </div>
    <div class="mui-input-row">
        <textarea rows="5" name="remark" ng-model="installOrder.remark" class="mui-input-clear" placeholder="备注"></textarea>
    </div>
    <div class="mui-button-row button_margin2">
        <button type="button" id="submitBtn" class="mui-btn mui-btn-primary button_margin" ng-disabled="myForm.$invalid" ng-click="submit(myForm.$valid)">确认</button>&nbsp;&nbsp;
        <button type="button" id="console" class="mui-btn mui-btn-primary button_margin" onclick="WeixinJSBridge.call('closeWindow')">取消</button>
    </div>
</form>
<script type="text/javascript">
    var app = angular.module('myApp', []);
    app.controller('formCtrl', function($scope, $http) {
        $scope.submit = function(isValid){
            if (!isValid) {
                alert('验证失败');
            }
            $("#submitBtn").attr('disabled',true);
            $http({
                method  : 'POST',
                url     : '${createLink(controller: 'installOrderPending',action: 'insert')}',
                data    : $.param($scope.installOrder),  // pass in data as strings
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
            }).success(function(data) {
                if(!data.success){
                    $("#submitBtn").attr('disabled',false);
                    alert(data.msg);
                }else{
                    mui.alert('保存成功！', '提示', function() {
                        //info.innerText = '你刚关闭了警告框';
                    });
                    WeixinJSBridge.call('closeWindow')
                }
            });
        };
        (function($) {
            var obj = $('#appointmentDate')[0];
            obj.addEventListener('click', function() {
                var optionsJson = '{}';
                var options = JSON.parse(optionsJson);
                var picker = new $.DtPicker(options);
                picker.show(function(rs) {
                    $('#appointmentDate')[0].value = rs.text;
                    $scope.installOrder.appointmentDate = rs.text;
//                    picker.dispose();
                });
            }, false);
        })(mui);
    });

</script>
</body>
</html>