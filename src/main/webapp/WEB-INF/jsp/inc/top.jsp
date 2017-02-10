<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>
    <a class="navbar-brand" href="home"><img src="static/images/logo.png" alt=""></a>
</div>
<!-- /.navbar-header -->

<ul class="nav navbar-top-links navbar-right">
    <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
            <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
        </a>
        <ul class="dropdown-menu dropdown-user">
            <%--<li><a href="#"><i class="fa fa-user fa-fw"></i> User Profile</a>
            </li>--%>
            <li><a href="javascript:void(0)" id="mediator"><i class="fa fa-gear fa-fw"></i> 修改密码</a>
            </li>
            <%--<li class="divider"></li>--%>
            <li><a href="logout"><i class="fa fa-sign-out fa-fw"></i> 退出</a>
            </li>
        </ul>
        <!-- /.dropdown-user -->
    </li>
    <!-- /.dropdown -->
</ul>
<!-- /.navbar-top-links -->