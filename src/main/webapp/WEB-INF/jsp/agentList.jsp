<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="inc/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@ include file="inc/meta.jsp" %>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>经济人列表</title>
    <%@ include file="inc/css.jsp" %>
</head>

<body>

<div id="posts" class="wrapper">

    <%@ include file="inc/nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">经济人列表</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">


                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables">
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
                                    <th>头像</th>
                                    <th>昵称</th>
                                    <th>手机号</th>
                                    <th>状态</th>
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
    <div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="userModalLabel">操作</h4>
                </div>
                <div class="modal-body ">
                    <form id="userForm" method="post" action="user/save" class="form-horizontal" role="form">
                        <input type="hidden" id="id" name="id" value="">
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">姓名:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="name" name="name" maxlength="20"
                                       data-rule="required" placeholder="请输入姓名">
                            </div>
                        </div>
                        <div class="form-group img_tooltip" >
                            <label for="imageUrl" class="col-sm-2 control-label">头像:</label>
                            <div class="col-sm-5">
                                <input type="hidden" id="imageUrl" name="imgUrl" value="">

                                <div class="image_show" style="display: none">

                                </div>
                                <div class="image_handle"   data-toggle="tooltip" data-placement="top" title="" data-original-title="建议上传宽480px高320px的图片">
                                    <div class="dropped"></div>
                                </div>
                            </div>
                            <div class="col-sm-5">
                                <a href="javascript:void(0)" id="removeImg" class="btn btn-info" role="button" >删除图片</a>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">英文名:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="ename" name="ename" maxlength="20"
                                       data-rule="required" placeholder="请输入英文名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">国籍:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="nationality" name="nationality" maxlength="20"
                                       data-rule="required" placeholder="请输入国籍">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">医院:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="hospital" name="hospital" maxlength="20"
                                       data-rule="required" placeholder="请输入医院">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">职务:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="post" name="post" maxlength="20"
                                       data-rule="required" placeholder="请输入职务">
                            </div>
                        </div>  
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">职称:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="titles" name="titles" maxlength="20"
                                       data-rule="required" placeholder="请输入职称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">科室:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="department" name="department" maxlength="20"
                                       data-rule="required" placeholder="请输入科室">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述:</label>
                            <div class="col-sm-5">
                                 <textarea type="text" class="form-control" id="description"  name="description" maxlength="500"
                                           rows="3"
                                           data-rule="required"
                                           placeholder="请输入描述"></textarea>
                            </div>
                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="userSave" class="btn btn-primary">保存</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- Modal end -->



    <!-- Modal -->
    <div class="modal fade" id="userDetailModal" tabindex="-1" role="dialog" aria-labelledby="userDetailModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="userDetailModalLabel">用户详情</h4>
                </div>
                <div class="modal-body" id="print-row">
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">姓名:</label>
                            <span _name="name"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">手机:</label>
                            <span _name="tel"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">邮箱:</label>
                            <span _name="email"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">英文名:</label>
                            <span _name="ename"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">国籍:</label>
                            <span _name="nationality"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">性别:</label>
                            <span _name="sex"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">医院:</label>
                            <span _name="hospital"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">职务:</label>
                            <span _name="post"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">科室:</label>
                            <span _name="titles"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">职称:</label>
                            <span _name="department"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">备注:</label>
                            <span _name="remark"></span>
                        </p>
                    </div>
                    <div class="row">
                        <p class="col-sm-9">
                            <label class="col-sm-3 control-label">注册时间:</label>
                            <span _name="createDate"></span>
                        </p>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
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

    var userList = {
        v: {
            id: "userList",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {

                var self = this;

                self.dataTableInit();

                $("#c_search").click(function () {
                    userList.v.dTable.ajax.reload();
                })

                $("#batchDelete").click(function(){
                    var checkBox = $("#dataTables tbody tr").find('input[type=checkbox]:checked');
                    var ids = checkBox.getInputId();
                    self.disable(ids)
                })

                $("#newUser").click(function(){
                    self.showModal("userModal","添加管理员帐号");
                })

                $("#userSave").click(function(){
                    self.save();
                })
            },
            dataTableInit: function () {
                userList.v.dTable = $health.dataTable($('#dataTables'), {
                    "processing": true,
                    "serverSide": true,
                    "searching":false,
                    "ordering": false,
                    "ajax": {
                        "url": "mgt/user/agentList",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "head"},
                        {"data": "nickname"},
                        {"data": "mobile"},
                        {"data": "agentStatus"},
                        {"data": "addTime"},
                        {"data": ""}
                    ],
                    "columnDefs": [
                        {
                            "data": null,
                            "defaultContent":"",
                            "targets": -1
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        var headHtml = '<img  class="headHtml" src=' + data.head + ' />';
                        $('td', row).eq(0).html(headHtml);
                        var mobileHtml = '<a target="_blank" href="mgt/user/agentDetail?agentId='+data.id+'">'+data.mobile+'</a>';
                        $('td', row).eq(2).html(mobileHtml);
                        var status = "";
                        if(data.agentStatus=="10"){
                            status="待审核";
                        }else if(data.agentStatus=="20"){
                            status="通过";
                        }else if(data.agentStatus=="30"){
                            status="驳回";
                        }
                        $('td', row).eq(3).html(status);
                        var btnHtml = "<button   title='通过'  class='btn btn-primary btn-circle pass'>" +
                                "<i class='fa fa-check'></i>" +
                                "</button>" +
                                "&nbsp;&nbsp;" +
                                "<button  title='驳回'  class='btn btn-default btn-circle reject'>" +
                                "<i class='fa fa-close'></i>" +
                                "</button>";
                        if(data.agentStatus != "20" && data.agentStatus != "30"){
                            $('td', row).last().html(btnHtml);
                        }

                    },
                    "footerCallback": function( tfoot, data, start, end, display ) {
                        userList.v.list=data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".pass").click(function(){
                            userList.fn.approve(data.id,"0");
                        });
                        $('td', row).last().find(".reject").click(function(){
                            userList.fn.approve(data.id,"1");
                        });
                    },
                    "fnServerParams": function (aoData) {
                        aoData.name = $("#s_name").val();
                    }
                });
            },
            clearImageView: function () {
                $("#imageUrl").val("");
                $(".image_show").html("");
                $(".image_handle").show();
                $(".dropper-input").val("");
            },
            viewImage: function (image) {
                if (image) {
                    $(".dropper-input").val("");
                    $(".image_handle").hide();
                    $(".image_show").show();
                    $("#imageUrl").val(image);
                    $(".image_show").html("<img src='" + image + "' class='img-responsive' >");
                }
            },
            userDetail: function (item) {
                userList.fn.showModal("userDetailModal", "用户详情");
                for (var key in item) {
                    var element = $("#userDetailModal span[_name=" + key + "]")
                    var text = item[key];
                    if (text == null) {
                        text = "";
                    }
                    if (element.length > 0) {
                        if (text.length <= 0) {
                            element.parent().parent().hide();
                        }else {
                            element.parent().parent().show();
                            element.text(text);
                        }
                    }
                }
            },
            disable:function(ids){
                if (ids.length > 0) {
                    $health.optNotify(function () {
                        $health.ajax("user/delete", {ids:JSON.stringify(ids)}, function (result) {
                            userList.fn.responseComplete(result);
                        })
                    },"你确定要删除吗？","确定");
                }
            },
            approve:function(id,type){
                var btn = "通过";
                if (type == "1") {
                    btn = "驳回";
                }
                $health.optNotify(function () {
                    $health.ajax("mgt/user/agentApprove", {id:id,type:type}, function (result) {
                        userList.fn.responseComplete(result,true);
                    })
                },"你确定要"+btn+"吗？","确定");
            },
            edit: function (id) {
                userList.fn.showModal("userModal","修改用户");
                var items = userList.v.list;
                $.each(items, function (index, item) {
                    if (item.id == id) {
                        for (var key in item) {
                            var element = $("#userForm :input[name=" + key + "]")
                            if (element.length > 0) {
                                element.val(item[key]);
                            }
                            if (key == "imgUrl") {
                                if (item.imgUrl) {
                                    userList.fn.viewImage(item.imgUrl);
                                } else {
                                    userList.fn.clearImageView();
                                }
                            }
                            if(key == "description"){
                                $("#description").text(item[key])
                            }
                        }
                    }
                })
            },
            save: function () {
                if(!$('#userForm').isValid()) {
                    return false;
                };
                $("#userForm").ajaxSubmit({
                    dataType: "json",
                    success: function (result) {
                        userList.fn.responseComplete(result,true,$("#userModal"));
                    },
                    error:function(result){
                        $health.errCallBack(result);
                    }
                });
            },
            responseComplete: function (result,action,modal) {
                if (result.status == "0") {
                    if(modal){
                        modal.modal("hide");
                    }
                    if(action){
                        userList.v.dTable.ajax.reload(null, false);
                    }else{
                        userList.v.dTable.ajax.reload();
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
        userList.fn.init();
    });



</script>


</html>