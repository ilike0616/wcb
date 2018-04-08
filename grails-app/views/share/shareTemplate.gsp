<%--
  Created by IntelliJ IDEA.
  User: guozhen
  Date: 2015/9/11
  Time: 20:07
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <style>
    body {
        font-family: "微软雅黑";
        font-size: 14px;
        color: #494949;
        text-decoration: none;
        background-color: #eeeeee;
    }
    .share {
        display: block;
        width: 98%;
        margin: 0px auto;
        margin-top: 20px;
        margin-bottom: 20px;
    }
    .share_title {
        display: block;
        width: 100%;
        height: 28px;
        background-color: #6fa3c6;
        border-radius: 8px 8px 0px 0px;
    }
    .share_title label {
        color: #FFF;
        position: relative;
        top: 4px;
        left: 15px;
    }
    .share_main {
        background-color: #FFF;
        min-height: 100px;
        padding-top:15px;
        padding-bottom:15px;
        margin-bottom: 10px;
        border-right-width: 1px;
        border-bottom-width: 1px;
        border-left-width: 1px;
        border-right-style: solid;
        border-bottom-style: solid;
        border-left-style: solid;
        border-right-color: #6fa3c6;
        border-bottom-color: #6fa3c6;
        border-left-color: #6fa3c6;
        overflow:hidden;
    }
    .share_main ul{
        display:block;
    }
    .share_main li{
        width: 100%;
        display: block;
        margin-top: 5px;
        margin-bottom: 5px;
        list-style-type: none;
    }
    .share_main li label{
        width: 7%;
        display: inline-block;
        color:#6fa3c6;
        vertical-align:top;
        padding-top:3px;
    }
    .share_main li i{
        width:92%;
        display:inline-block;
        font-style:normal;
        line-height:24px;
    }
    .share_main li i img{
        margin-top:7px;
        margin-right:15px;
    }
    .share_icon{
        display:block;
        position:relative;
        left:110px;
        margin-top:25px;
        margin-bottom:15px;
    }
    .share_icon img{
        display: inline-block;
        margin-right:15px;
        width:3%;
    }

    /*Common For Max 980*/
    @media only screen and (max-width: 980px) {
        body {
            font-family: "微软雅黑";
            color: #333333;
            text-decoration: none;
            font-size:100%;
            font-size:14px;
            font-size: 1em;
        }
        .share {
            display: block;
            width: 98%;
            margin: 0px auto;
            margin-top: 1.5em;
            margin-bottom: 1.4em;
        }
        .share_title {
            display: block;
            width: 100%;
            height: 2.3em;
            background-color: #6fa3c6;
            border-radius: 0.5em 0.5em 0em 0em;
            font-size:2.8em;
        }
        .share_title label {
            color: #FFF;
            position: relative;
            top: 0.4em;
            left: 0.8em;
        }
        .share_main {
            background-color: #FFF;
            padding-top:0.8em;
            padding-bottom:0.8em;
            margin-bottom: 0.8em;
            border-right-width: 0.1em;
            border-bottom-width: 0.1em;
            border-left-width: 0.1em;
            border-right-style: solid;
            border-bottom-style: solid;
            border-left-style: solid;
            border-right-color: #6fa3c6;
            border-bottom-color: #6fa3c6;
            border-left-color: #6fa3c6;
            overflow:hidden;
        }
        .share_main ul{
            display:block;
        }
        .share_main li{
            width: 100%;
            display: block;
            margin-top: 0.3em;
            margin-bottom: 0.3em;
            list-style-type: none;
        }
        .share_main li label{
            width: 100%;
            display: inline-block;
            color:#6fa3c6;
            vertical-align:top;
            padding-top:0.5em;
            font-size:2.3em;
            font-weight:bold;
        }
        .share_main li i{
            width:96%;
            display:inline-block;
            font-style:normal;
            line-height:2em;
            font-size:2em;
            padding-top:0.2em;
        }
        .share_main li i img{
            margin-top:0.7em;
            margin-right:0.8em;
            width:100%;
        }
        .share_icon{
            display:block;
            position:relative;
            left:17%;
            margin-top:2em;
            margin-bottom:2em;
        }
        .share_icon img{
            display: inline-block;
            margin-right:1.5em;
            width:10%;
        }
    }


    /*Common For Max 768*/
    @media only screen and (max-width: 800px) {

        body {
            font-family: "微软雅黑";
            color: #333333;
            text-decoration: none;
            font-size:100%;
            font-size:14px;
            font-size: 1em;
        }
        .share {
            display: block;
            width: 98%;
            margin: 0px auto;
            margin-top: 1.5em;
            margin-bottom: 1.4em;
        }
        .share_title {
            display: block;
            width: 100%;
            height: 2.3em;
            background-color: #6fa3c6;
            border-radius: 0.5em 0.5em 0em 0em;
            font-size:2.8em;
        }
        .share_title label {
            color: #FFF;
            position: relative;
            top: 0.4em;
            left: 0.8em;
        }
        .share_main {
            background-color: #FFF;
            padding-top:0.8em;
            padding-bottom:0.8em;
            margin-bottom: 0.8em;
            border-right-width: 0.1em;
            border-bottom-width: 0.1em;
            border-left-width: 0.1em;
            border-right-style: solid;
            border-bottom-style: solid;
            border-left-style: solid;
            border-right-color: #6fa3c6;
            border-bottom-color: #6fa3c6;
            border-left-color: #6fa3c6;
            overflow:hidden;
        }
        .share_main ul{
            display:block;
            position:relative;
            left:-0.5em;
        }
        .share_main li{
            width: 97%;
            display: block;
            margin-top: 0.3em;
            margin-bottom: 0.3em;
            list-style-type: none;
        }
        .share_main li label{
            width: 100%;
            display: inline-block;
            color:#6fa3c6;
            vertical-align:top;
            padding-top:0.5em;
            font-size:2.3em;
            font-weight:bold;
        }
        .share_main li i{
            width:100%;
            display:inline-block;
            font-style:normal;
            line-height:2em;
            font-size:2em;
            padding-top:0.2em;
        }
        .share_main li i img{
            margin-top:0.7em;
            margin-right:0.8em;
            width:100%;
        }
        .share_icon{
            display:block;
            position:relative;
            left:15%;
            margin-top:2em;
            margin-bottom:2em;
        }
        .share_icon img{
            display: inline-block;
            margin-right:1.5em;
            width:10%;
        }

    }

    /*Common For Max 360*/
    @media only screen and (max-width: 360px) {

        body {
            font-family: "微软雅黑";
            color: #333333;
            text-decoration: none;
            font-size:100%;
            font-size:14px;
            font-size: 1em;
        }
        .share {
            display: block;
            width: 98%;
            margin: 0px auto;
            margin-top: 1.5em;
            margin-bottom: 1.4em;
        }
        .share_title {
            display: block;
            width: 100%;
            height: 1.8em;
            background-color: #6fa3c6;
            border-radius: 0.5em 0.5em 0em 0em;
            font-size:1em;
        }
        .share_title label {
            color: #FFF;
            position: relative;
            top: 0.2em;
            left: 0.8em;
        }
        .share_main {
            background-color: #FFF;
            padding-top:0.2em;
            padding-bottom:0em;
            margin-bottom: 0em;
            border-right-width: 0.1em;
            border-bottom-width: 0.1em;
            border-left-width: 0.1em;
            border-right-style: solid;
            border-bottom-style: solid;
            border-left-style: solid;
            border-right-color: #6fa3c6;
            border-bottom-color: #6fa3c6;
            border-left-color: #6fa3c6;
            overflow:hidden;
        }
        .share_main ul{
            display:block;
            position:relative;
            left:-1.8em;
            width:94%;
        }
        .share_main li{
            width: 100%;
            display: block;
            margin-top: 0.3em;
            margin-bottom: 0.3em;
            list-style-type: none;
        }
        .share_main li label{
            width: 100%;
            display: inline-block;
            color:#6fa3c6;
            vertical-align:top;
            padding-top:0.5em;
            font-size:1em;
            margin-bottom:0.5em;
            font-weight:bold;

        }
        .share_main li i{
            width:100%;
            display:inline-block;
            font-style:normal;
            line-height:1.7em;
            font-size:1em;
            padding-top:0em;
        }
        .share_main li i img{
            margin-top:0.7em;
            margin-right:0.8em;
            width:100%;
        }
        .share_icon{
            display:block;
            position:relative;
            left:12%;
            margin-top:1em;
            margin-bottom:1.5em;
        }
        .share_icon img{
            display: inline-block;
            margin-right:1em;
            width:10%;
        }

    }
    </style>

    <script type="text/javascript" src="http://validform.rjboy.cn/wp-content/themes/validform/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            var htmlContent = "${share.htmlContent}";
            if(htmlContent != ""){
                htmlContent = htmlContent.replace(new RegExp(/&lt;/g),'<').replace(new RegExp(/&gt;/g),'>')
                        .replace(new RegExp(/&quot;/g),"'").replace(new RegExp(/&amp;nbsp;/g)," ");
                $("#htmlContent").html(htmlContent);
            }
        })
    </script>
</head>
<body>
<div class="share">
    <div class="share_title"><label>我要分享</label></div>
    <div class="share_main">
        <ul>
            <li>
                <label>分享主题：</label>
                <i>${share.subject}</i>
            </li>
            <li>
                <label>分享摘要：</label>
                <i id="htmlContent">${share.htmlContent}</i>
            </li>
            <li>
                <label>分享图片：</label>
                <g:if test="${share.pic != null && share.pic != ''}">
                    <i><img src="${grailsApplication.config.HTTP_FILE_URL}/${share.pic?.name}" width="400" height="224"></i>
                </g:if>
                <g:else>
                    <i><img src="${grailsApplication.config.HTTP_FILE_URL}/defaultSharePic.gif" width="400" height="224"></i>
                </g:else>
            </li>
        </ul>
    </div>
</div>
</body>
</html>