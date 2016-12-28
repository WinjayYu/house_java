<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="inc/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@ include file="inc/meta.jsp" %>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>房源详情</title>
    <%@ include file="inc/css.jsp" %>
</head>

<body>

<div id="wrapper">

    <%@ include file="inc/nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">房源详细信息</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        房源详细信息
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">标题:</label>
                                        <span _name="name">${house.title}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">小区:</label>
                                        <span _name="name">${house.community.name}</span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">户型:</label>
                                        <span _name="name">${house.layout}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">用途:</label>
                                        <span _name="name">${house.purpose}</span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">面积:</label>
                                        <span _name="name">${house.area}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">价格:</label>
                                        <span _name="name">${house.price}</span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">楼层:</label>
                                        <span _name="name">${house.floor}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">朝向:</label>
                                        <span _name="name">${house.orientation}</span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">装修程度:</label>
                                        <span _name="name">${house.renovation}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">经济人:</label>
                                        <span _name="name">${house.agent.nickname}</span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">年份:</label>
                                        <span _name="name">${house.year}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">标签:</label>
                                        <span _name="name">${house.tags}</span>
                                    </p>
                                </div>
                                <div class="row">
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">核心:</label>
                                        <span _name="name">${feature.core}</span>
                                    </p>
                                    <p class="col-sm-6">
                                        <label class="col-sm-2 control-label">税收:</label>
                                        <span _name="name">${feature.traffic}</span>
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