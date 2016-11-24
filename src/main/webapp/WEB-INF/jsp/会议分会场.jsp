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
    <title>会议分会场</title>
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
                <h1 class="page-header">分会场会议</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">

                    <div class="panel-heading">
                        <div class="btn-group pull-left">
                            <a href="javascript:void(0)"  id="newSeparate" class="btn btn-outline btn-primary btn-lg"
                               role="button">新建分会场</a>
                        </div>

                        <form class="navbar-form navbar-right" >
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
                                <colgroup>
                                    <col class="gradeA even"/>
                                    <col class="gradeA odd"/>
                                    <col class="gradeA even"/>
                                    <col class="gradeA odd"/>
                                    <col class="gradeA even"/>
                                    <col class="gradeA odd"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th><input type="checkbox" onclick="$health.checkAll(this)" class="checkall"/></th>
                                    <th>标题</th>
                                    <th>描述</th>
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
    <div class="modal fade" id="separateModal" tabindex="-1" role="dialog" aria-labelledby="separateModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="separateModalLabel">操作</h4>
                </div>
                <div class="modal-body ">
                    <form id="separateForm" method="post" action="forum/separate/save" class="form-horizontal" role="form">
                        <input type="hidden" id="id" name="id" value="">
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">标题:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="name" name="name" maxlength="20"
                                       data-rule="required" placeholder="请输入标题">
                            </div>
                        </div>
                        <div class="form-group img_tooltip" >
                            <label for="imageUrl" class="col-sm-2 control-label">图片:</label>
                            <div class="col-sm-5">
                                <input type="hidden" id="imageUrl" name="bgImg" value="">

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
                            <label for="description" class="col-sm-2 control-label">描述:</label>
                            <div class="col-sm-5">
                                 <textarea type="text" class="form-control" id="description" name="description" maxlength="500"
                                           rows="3"
                                           data-rule="required"
                                           placeholder="请输分会场描述"></textarea>
                            </div>
                        </div>
                       
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="separateSave" class="btn btn-primary">保存</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- Modal end -->

</div>
<!-- /#wrapper -->

<div class="modal fade" id="popModal" >
    <div class="modal-dialog">
        <div class="modal-content" style="width: 900px;">

        </div>
        <!-- /.modal-content -->
    </div>
</div>


<%@include file="inc/footer.jsp" %>
</body>

<script type="text/javascript">


    var separateList = {
        v: {
            id: "separateList",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {

                var self = this;

                self.dataTableInit();
                self.dropperInit();

                $("#c_search").click(function () {
                    separateList.v.dTable.ajax.reload();
                })

                $("#batchDelete").click(function(){
                    var checkBox = $("#dataTables tbody tr").find('input[type=checkbox]:checked');
                    var ids = checkBox.getInputId();
                    self.disable(ids)
                })

                $("#newSeparate").click(function(){
                    self.showModal("separateModal","添加会议主题");
                })

                $("#separateSave").click(function(){
                    self.save();
                })

                $("#removeImg").click(function () {
                    self.clearImageView();
                })
            },
            dataTableInit: function () {
                separateList.v.dTable = $health.dataTable($('#dataTables'), {
                    "processing": true,
                    "serverSide": true,
                    "searching":false,
                    "ordering": false,
                    "ajax": {
                        "url": "forum/separate/list",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "id"},
                        {"data": "name"},
                        {"data": "description"},
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
                            "<button   title='添加议程'  class='btn btn-danger btn-circle tagPop'>" +
                            "<i class='fa fa-plus'></i>" +
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
                        separateList.v.list=data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".disable").click(function(){
                            separateList.fn.disable([data.id]);
                        })
                        $('td', row).last().find(".edit").click(function(){
                            separateList.fn.edit(data.id);
                        })
                        $('td', row).last().find(".tagPop").click(function(){
                            separateList.fn.tagPop(data.id);
                        })
                    },
                    "fnServerParams": function (aoData) {
                        aoData.forumId = $("#forumId").val();
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
            dropperInit: function () {
                $("#separateModal" + " .dropped").dropper({
                    postKey: "file",
                    action: "gen/save/image",
                    postData: {thumbSizes: '480x320'},
                    label: "把文件拖拽到此处",
                    maxSize: 204857
                }).on("fileComplete.dropper", separateList.fn.onFileComplete)
                        .on("fileError.dropper", separateList.fn.onFileError);
            },
            onFileComplete: function (e, file, response) {
                if (response.status == '200') {
                    separateList.fn.viewImage(response.data);
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
                        $health.ajax("forum/separate/delete", {ids:JSON.stringify(ids)}, function (result) {
                            separateList.fn.responseComplete(result);
                        })
                    },"你确定要删除吗？","确定");
                }
            },
            tagPop:function(id){
                $('#popModal').modal('show').find('.modal-content').load("agenda/pop?"+(new Date()).valueOf()+"&tagId="+id);
            },
            edit: function (id) {
                separateList.fn.showModal("separateModal","修改会议分会场");
                var items = separateList.v.list;
                $.each(items, function (index, item) {
                    if (item.id == id) {
                        for (var key in item) {
                            var element = $("#separateForm :input[name=" + key + "]")
                            if (element.length > 0) {
                                element.val(item[key]);
                            }
                            if (key == "bgImg") {
                                if (item.bgImg) {
                                    separateList.fn.viewImage(item.bgImg);
                                } else {
                                    separateList.fn.clearImageView();
                                }
                            }
                        }
                    }
                })
            },
            save: function () {
                if(!$('#separateForm').isValid()) {
                    return false;
                };
                $("#separateForm").ajaxSubmit({
                    dataType: "json",
                    data:{forumId:$("#forumId").val()},
                    success: function (result) {
                        separateList.fn.responseComplete(result,true,$("#separateModal"));
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
                        separateList.v.dTable.ajax.reload(null, false);
                    }else{
                        separateList.v.dTable.ajax.reload();
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
        separateList.fn.init();
    });



</script>


</html>