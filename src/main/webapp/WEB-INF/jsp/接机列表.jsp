<%--
  Created by IntelliJ IDEA.
  User: wangbin
  Date: 2015/3/3
  Time: 9:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="inc/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@ include file="inc/meta.jsp" %>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>接机列表</title>
    <%@ include file="inc/css.jsp" %>
    <link href="static/js/plugins/dropper/jquery.fs.dropper.css" rel="stylesheet">
    <script src="static/js/plugins/dropper/jquery.fs.dropper.js"></script>
</head>

<body>

<div id="posts" class="wrapper">

    <%@ include file="inc/nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">接机列表</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">

                    <div class="panel-heading">
                        <form class="navbar-form navbar-right" >
                            <div class="form-group">
                                <label>账号/名称：</label>
                                <input type="text" class="form-control" value="" id="s_name"  name="name" maxlength="20"
                                       placeholder="请输入账号/名称">
                            </div>
                            <div class="form-group">
                                <label>选择会议：</label>
                                <select class="form-control input-sm" id="forumId">
                                    <c:forEach items="${forumList}" var="forum">
                                        <option value="${forum.id}">${forum.titile}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <button type="button" id="c_search" class="btn btn-info btn-sm">查询</button>
                        </form>
                        <div class="clearfix"></div>
                    </div>
                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables">
                                <thead>
                                <tr>
                                    <th><input type="checkbox" onclick="$health.checkAll(this)" class="checkall"/></th>
                                    <th>用户名</th>
                                    <th>手机</th>
                                    <th>来程日期</th>
                                    <th>来程方式</th>
                                    <th>航班号/车次</th>
                                    <th>到达时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>

                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>
        </div>

    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<%@ include file="inc/footer.jsp" %>
</body>

<script type="text/javascript">


    var travelInformationList = {
        v: {
            id: "travelInformationList",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {
                var self = this;
                self.dataTableInit();

                $("#c_search").click(function () {
                    travelInformationList.v.dTable.ajax.reload();
                })

            },
            dataTableInit: function () {
                travelInformationList.v.dTable = $health.dataTable($('#dataTables'), {
                    "processing": true,
                    "serverSide": true,
                    "searching":false,
                    "ordering": false,
                    "ajax": {
                        "url": "travel/list",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "id"},
                        {"data": "user.name"},
                        {"data": "user.tel"},
                        {"data": "inboundDate"},
                        {"data": "inboundMode"},
                        {"data": "flightNumber"},
                        {"data": "flightDate"},
                        {"data": ""}
                    ],
                    "columnDefs": [
                        {
                            "data": null,
                            "defaultContent":
                            "<button  title='删除'  class='btn btn-default btn-circle disable'>" +
                            "<i class='fa fa-close'></i>" +
                            "</button>",
                            "targets": -1
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        $('td', row).eq(0).html("<input type='checkbox' value=" + data.id + ">");

                        if(data.inboundMode=='火车'){
                            $('td', row).eq(5).html(data.trainDate);
                            $('td', row).eq(4).html(data.trainTrips);

                        }
                    },
                    "footerCallback": function( tfoot, data, start, end, display ) {
                        travelInformationList.v.list=data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".disable").click(function(){
                            travelInformationList.fn.disable([data.id]);
                        })
                    },
                    "fnServerParams": function (aoData) {
                        aoData.forumId = $("#forumId").val();
                        aoData.name = $("#s_name").val();

                    }
                });
            },
            disable:function(ids){
                if (ids.length > 0) {
                    $health.optNotify(function () {
                        $health.ajax("travel/delete", {ids:JSON.stringify(ids)}, function (result) {
                            travelInformationList.fn.responseComplete(result);
                        })
                    },"你确定要删除吗？","确定");
                }
            },
            responseComplete: function (result,action,modal) {
                if (result.status == "200") {
                    if(modal){
                        modal.modal("hide");
                    }
                    if(action){
                        travelInformationList.v.dTable.ajax.reload(null, false);
                    }else{
                        travelInformationList.v.dTable.ajax.reload();
                    }
                    $health.notify(result.msg, "success");
                } else {
                    $health.notify(result.msg, "error");
                }
            },
            showModal: function (modalId, title) {
                $("#" + modalId).modal("show");
                $health.clearForm($("#" + modalId));
                if (title) {
                    $("#" + modalId + " .modal-title").text(title);
                }
            }
        }
    }

    $(document).ready(function () {
        travelInformationList.fn.init();
    });



</script>


</html>