<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="inc/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@ include file="inc/meta.jsp" %>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>经济人详细信息</title>
    <%@ include file="inc/css.jsp" %>
</head>

<body>

<div id="wrapper">

    <%@ include file="inc/nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">经济人详细信息</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        经济人详细信息
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">昵称:</label>
                                        <span _name="name">${user.nickname}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">手机号:</label>
                                        <span _name="name">${user.mobile}</span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">头像:</label>
                                        <span _name="name"><img src="${user.head}" width="200" height="200"/></span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">性别:</label>
                                        <span _name="name">
                                            <c:if test="${user.sex == '10'}">男</c:if>
                                            <c:if test="${user.sex == '20'}">女</c:if>
                                        </span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">身份证:</label>
                                        <span _name="name">${agentMaterial.idcard}</span>
                                    </p>

                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">身份证正面照片:</label>
                                        <span _name="name"><img src="${agentMaterial.positive}" width="200" height="200"/></span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">身份证反面照片:</label>
                                        <span _name="name"><img src="${agentMaterial.negative}" width="200" height="200"/></span>
                                    </p>

                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">公司名称:</label>
                                        <span _name="name">${agentMaterial.companyName}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">营业执照:</label>
                                        <span _name="name">${agentMaterial.companyCode}</span>
                                    </p>

                                </div>
                                <div class="row">

                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">营业执照图片:</label>
                                        <span _name="name"><img src="${agentMaterial.companyPic}" width="200" height="200"/></span>
                                    </p>
                                </div>
                            </div>
                            <!-- /.col-lg-6 (nested) -->
                        </div>
                        <!-- /.row (nested) -->
                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->


</body>

<script type="text/javascript">

    $(document).ready(function () {
    });


</script>


</html>