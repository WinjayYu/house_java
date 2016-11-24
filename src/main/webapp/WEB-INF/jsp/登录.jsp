<%--
  Created by IntelliJ IDEA.
  User: wangbin
  Date: 2014/12/5
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="inc/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh-cn"  >
<head>
    <%@ include file="inc/meta.jsp"%>
    <meta name="description" content="">
    <meta name="author" content="">

    <title>IVF平台系统登陆</title>

    <!-- Bootstrap Core CSS -->
    <link href="static/css/bootstrap.min.css" rel="stylesheet">


    <!-- Custom CSS -->
    <link href="static/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="static/font-awesome-4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="static/js/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="static/js/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style>

    </style>

</head>


<body style="background-image: url('static/images/background.jpg');">


<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">IVF平台系统</h3>
                </div>

                <div class="panel-body">
                    <form name="form"  action="login/check" method="POST">
                        <fieldset>
                            <div class="form-group  <c:if test="${empty error}">has_error</c:if>">
                            <input class="form-control" placeholder="用户名" name="username" value="${name}" type="text" autofocus>
                            <c:if test="${!empty error}">
                                <label class="control-label" >${error}</label>
                            </c:if>
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="密码" name="password" type="password" value="">
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input name="remark" type="checkbox" value="Remember Me">记住我
                                </label>
                            </div>
                            <!-- Change this to a button or input when using this as a form -->
                            <!--<a href="#" id="login" class="btn btn-lg btn-success btn-block">登录</a>-->
                            <button type="submit"  class="btn btn-lg btn-success btn-block">登录</button>
                        </fieldset>
                </form>
            </div>
        </div>
    </div>


</div>


</div>



<!-- jQuery Version 1.11.0 -->
<script src="static/js/jquery-1.11.0.js"></script>
<!-- Bootstrap Core JavaScript -->
<script src="static/js/bootstrap.min.js"></script>



</body>




</html>
