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
    <title>用户列表</title>
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
                <h1 class="page-header">用户列表</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">

                    <div class="panel-heading">
                        <div class="btn-group pull-left">
                            <a href="javascript:void(0)"  id="newUser" class="btn btn-outline btn-primary btn-lg"
                               role="button">创建主讲人</a>
                            <a href="javascript:void(0)"  id="batchDelete" class="btn btn-outline btn-primary btn-lg"
                               role="button">批量删除</a>
                        </div>

                        <form class="navbar-form navbar-right" role="search">
                            <div class="form-group">
                                <label>账号/名称：</label>
                                <input type="text" class="form-control" value="" id="s_name"  name="name" maxlength="20"
                                       placeholder="请输入账号/名称">
                            </div>
                            <button type="button" id="c_search" class="btn btn-info btn-sm">查询</button>
                        </form>

                        <div class="clearfix"></div>
                    </div>
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
                                    <th><input type="checkbox" onclick="$health.checkAll(this)" class="checkall"/></th>
                                    <th>姓名</th>
                                    <th>医院</th>
                                    <th>职位</th>
                                    <th>英文名</th>
                                    <th>类型</th>
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

                self.dropperInit();

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
                        "url": "user/list",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "id"},
                        {"data": "name"},
                        {"data": "hospital"},
                        {"data": "post"},
                        {"data": "ename"},
                        {"data": "type"},
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
                        $('td', row).eq(1).html("<a  href='javascript:void(0)'>"+data.name+"</a>");
                        if(data.type=="1"){
                            $('td', row).eq(5).html("普通用户");
                        }
                        if(data.type=="9"){
                            $('td', row).eq(5).html("主讲人");
                        }
                    },
                    "footerCallback": function( tfoot, data, start, end, display ) {
                        userList.v.list=data


                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".disable").click(function(){
                            userList.fn.disable([data.id]);
                        })
                        $('td', row).last().find(".edit").click(function(){
                            userList.fn.edit(data.id);
                        })

                        $('td', row).eq(1).click(function () {
                            userList.fn.userDetail(data);
                        })
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
            dropperInit: function () {
                $("#userModal" + " .dropped").dropper({
                    postKey: "file",
                    action: "gen/save/image",
                    postData: {thumbSizes: '480x320'},
                    label: "把文件拖拽到此处",
                    maxSize: 204857
                }).on("fileComplete.dropper", userList.fn.onFileComplete)
                        .on("fileError.dropper", userList.fn.onFileError);
            },
            onFileComplete: function (e, file, response) {
                if (response.status == '200') {
                    userList.fn.viewImage(response.data);
                } else {
                    $health.notify(response.msg, "error");
                }
            },
            onFileError: function (e, file, error) {
                $health.notify(error, "error");
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
                if (result.status == "200") {
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