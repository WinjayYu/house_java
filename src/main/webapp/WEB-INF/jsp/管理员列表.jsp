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
    <title>管理员列表</title>
    <%@ include file="inc/css.jsp" %>

</head>

<body>

<div id="posts" class="wrapper">

    <%@ include file="inc/nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">管理员列表</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">

                    <div class="panel-heading">
                        <div class="btn-group pull-left">
                            <a href="javascript:void(0)"  id="newMember" class="btn btn-outline btn-primary btn-lg"
                               role="button">创建管理员帐号</a>
                        </div>

                        <form class="navbar-form navbar-right" role="search">
                            <div class="form-group">
                                <label>账号/名称：</label>
                                <input type="text" class="form-control" value="" id="s_name"  name="name" maxlength="20"
                                       placeholder="请输入账号/名称">
                            </div>
                            <%--<div class="form-group">--%>
                                <%--<label>状态：</label>--%>
                                <%--<select class="form-control input-sm" id="s_isdel">--%>
                                    <%--<option value="" selected="selected">全部</option>--%>
                                    <%--<option value="false">启用</option>--%>
                                    <%--<option value="true">禁用</option>--%>
                                <%--</select>--%>
                            <%--</div>--%>
                            <button type="button" id="c_search" class="btn btn-info btn-sm">查询</button>
                        </form>

                        <div class="clearfix"></div>
                    </div>
                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table width="100%"  class="table table-striped table-bordered table-hover" id="dataTables">
                                <colgroup>
                                    <col class="gradeA even"/>
                                    <col class="gradeA odd"/>
                                    <col class="gradeA even"/>
                                    <col class="gradeA odd"/>
                                    <col class="gradeA even"/>
                                    <col class="gradeA odd"/>
                                    <col class="gradeA even"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th><input type="checkbox" onclick="$health.checkAll(this)" class="checkall"/></th>
                                    <th>账号</th>
                                    <th>姓名</th>
                                    <th>邮箱</th>
                                    <th>手机</th>
                                    <th>创建时间</th>
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

    <!-- Modal -->
    <div class="modal fade" id="memberModal" tabindex="-1" role="dialog" aria-labelledby="memberModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="memberModalLabel">操作</h4>
                </div>
                <div class="modal-body ">
                    <form id="memberForm" method="post" action="member/save" class="form-horizontal" role="form">
                        <input type="hidden" id="id" name="id" value="">
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">姓名:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="name" name="name" maxlength="20"
                                       data-rule="required" placeholder="请输入姓名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="username" class="col-sm-2 control-label">用户名:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="username" name="username" maxlength="20"
                                       data-rule="required" placeholder="请输入用户名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">密码:</label>
                            <div class="col-sm-5">
                                <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码(不修改请留空)">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-sm-2 control-label">用户名:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="email" name="email" maxlength="20"
                                       data-rule="email" placeholder="请输入邮箱">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mobile" class="col-sm-2 control-label">手机:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="mobile" name="mobile" maxlength="20"
                                       data-rule="mobile" placeholder="请输入手机号">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="memberSave" class="btn btn-primary">保存</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- Modal end -->


</div>
<!-- /#wrapper -->

<%@ include file="inc/footer.jsp" %>
</body>

<script type="text/javascript">


    var memberList = {
        v: {
            id: "memberList",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {
                memberList.fn.dataTableInit();

                $("#c_search").click(function () {
                    memberList.v.dTable.ajax.reload();
                })

                $("#batchDisables").click(function(){
                    var checkBox = $("#dataTables tbody tr").find('input[type=checkbox]:checked');
                    var ids = checkBox.getInputId();
                    memberList.fn.disable(ids)
                })

                $("#newMember").click(function(){
                    memberList.fn.showModal("memberModal","添加管理员帐号");
                })

                $("#memberSave").click(function(){
                    memberList.fn.save();
                })
            },
            dataTableInit: function () {
                memberList.v.dTable = $health.dataTable($('#dataTables'), {
                    "processing": true,
                    "serverSide": true,
                    "searching":false,
                    "ordering": false,
                    "ajax": {
                        "url": "member/list",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "id"},
                        {"data": "username"},
                        {"data": "name"},
                        {"data": "email"},
                        {"data": "mobile"},
                        {"data": "createDate"},
                        {"data": ""}
                    ],
                    "columnDefs": [
                        {
                            "data": null,
                            "defaultContent":
                            "<button   title='编辑'  class='btn btn-primary btn-circle edit'>" +
                            "<i class='fa fa-edit'></i>" +
                            "</button>" +
                            "&nbsp;&nbsp;" +
                            "<button  title='删除'  class='btn btn-default btn-circle disable'>" +
                            "<i class='fa fa-close'></i>" +
                            "</button>",
                            "targets": -1
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        $('td', row).eq(0).html("<input type='checkbox' value=" + data.id + ">");
                    },
                    "footerCallback": function( tfoot, data, start, end, display ) {
                        memberList.v.list=data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".disable").click(function(){
                            memberList.fn.disable([data.id]);
                        })
                        $('td', row).last().find(".edit").click(function(){
                            memberList.fn.edit(data.id);
                        })
                    },
                    "fnServerParams": function (aoData) {
                        aoData.name = $("#s_name").val();
                    }
                });
            },
            disable:function(ids){
                if (ids.length > 0) {
                    $health.optNotify(function () {
                        $health.ajax("member/delete", {ids:JSON.stringify(ids)}, function (result) {
                            memberList.fn.responseComplete(result);
                        })
                    },"你确定要删除吗？","确定");
                }
            },
            edit: function (id) {
                memberList.fn.showModal("memberModal","修改管理员帐号");
                var items = memberList.v.list;
                $.each(items, function (index, item) {
                    if (item.id == id) {
                        for (var key in item) {
                            var element = $("#memberForm :input[name=" + key + "]")
                            if (element.length > 0&&key!="password") {
                                element.val(item[key]);
                            }
                        }
                    }
                })
            },
            save: function () {
                if(!$('#memberForm').isValid()) {
                    return false;
                };
                $("#memberForm").ajaxSubmit({
                    dataType: "json",
                    success: function (result) {
                        memberList.fn.responseComplete(result,true,$("#memberModal"));
                    },
                    error:function(result){
                        $health.errCallBack(result);
                    }
                });
            },
            responseComplete: function (result,action,modal) {
                if (result.status == "200") {
                    if(modal){
                        modal.modal("hide");
                    }
                    if(action){
                        memberList.v.dTable.ajax.reload(null, false);
                    }else{
                        memberList.v.dTable.ajax.reload();
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
        memberList.fn.init();
    });



</script>


</html>