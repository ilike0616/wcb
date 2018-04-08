<%--
  Created by IntelliJ IDEA.
  User: like
  Date: 2016/1/20
  Time: 9:31
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <script src="http://static.35crm.com/mui/mui.min.js"></script>
    <link href="http://static.35crm.com/mui/mui.min.css" rel="stylesheet"/>
    <title></title>
    <style>
    .header_bg {
        background-color: #3285dd;
    }
    .mui-title_font {
        color: #fff;
    }
    .form_margin {
        margin-top: 45px;
    }
    </style>
</head>

<body>
<header class="mui-bar mui-bar-nav header_bg">
    <h1 class="mui-title mui-title_font">我的安装单</h1>
</header>
<div class="mui-card form_margin" style="margin-bottom: 35px;">
    <ul class="mui-table-view">
    <g:each in="${installOrderList}">
        <li class="mui-table-view-cell mui-media">
            <div class="mui-media-body">
                预约时间
                <p class='mui-ellipsis'><g:formatDate format="yyyy-MM-dd HH:mm" date="${it.appointmentDate}"/></p>
            </div>
            <div class="mui-media-body">
                安装地址
                <p class='mui-ellipsis'>${it.installAddress}</p>
            </div>
            <div class="mui-media-body">
                安装描述
                <p class='mui-ellipsis'>${it.description}</p>
            </div>
            <div class="mui-media-body">
                备注
                <p class='mui-ellipsis'>${it.remark}</p>
            </div>
        </li>
    </g:each>
    </ul>
</div>
</body>
</html>