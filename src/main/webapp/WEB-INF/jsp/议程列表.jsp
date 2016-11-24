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
    <title>议程列表</title>
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
                <h1 class="page-header">议程列表</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">

                    <div class="panel-heading">
                        <div class="btn-group pull-left">
                            <a href="agenda/details"  id="newForum" class="btn btn-outline btn-primary btn-lg"
                               role="button">新建议程</a>
                        </div>

                        <form class="navbar-form navbar-right" >
                            <div class="form-group">
                                <label>标题：</label>
                                <input type="text" class="form-control" value="" id="title"  name="title" maxlength="20"
                                       placeholder="请输入标题">
                            </div>
                            <div class="form-group">
                                <label>状态：</label>
                                <select class="form-control input-sm" id="status">
                                <option value="1" selected>启用</option>
                                <option value="0">禁用</option>
                                </select>
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
                            <table width="100%"  class="table table-striped table-bordered table-hover" id="dataTables">

                                <thead>
                                <tr>
                                    <th><input type="checkbox" onclick="$health.checkAll(this)" class="checkall"/></th>
                                    <th>标题</th>
                                    <th>地点</th>
                                    <th>开始时间</th>
                                    <th>结束时间</th>
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
    <div class="modal fade" id="forumModal" tabindex="-1" role="dialog" aria-labelledby="forumModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="forumModalLabel">操作</h4>
                </div>
                <div class="modal-body ">
                    <form id="forumForm" method="post" action="forum/save" class="form-horizontal" role="form">
                        <input type="hidden" id="id" name="id" value="">
                        <div class="form-group">
                            <label for="titile" class="col-sm-2 control-label">标题:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="titile" name="titile" maxlength="20"
                                       data-rule="required" placeholder="请输入标题">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="location" class="col-sm-2 control-label">地点:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="location" name="location" maxlength="20"
                                       data-rule="required" placeholder="请输入地点 ">
                            </div>
                        </div>
                        <div class="form-group img_tooltip" >
                            <label for="imageUrl" class="col-sm-2 control-label">图片:</label>
                            <div class="col-sm-5">
                                <input type="hidden" id="imageUrl" name="bgImgUrl" value="">

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
                            <label for="start" class="col-sm-2 control-label">开始时间:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="startDate" id="start" value="" autocomplete="off" placeholder="">

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="end" class="col-sm-2 control-label">结束时间:</label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="endDate" id="end" value="" autocomplete="off" placeholder="">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="forumSave" class="btn btn-primary">保存</button>
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


<%@ include file="inc/footer.jsp" %>
</body>

<script type="text/javascript">


    var forumList = {
        v: {
            id: "forumList",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {

                var self = this;

                self.dataTableInit();
                self.dateInit();
                self.dropperInit();

                $("#c_search").click(function () {
                    forumList.v.dTable.ajax.reload();
                })

                $("#batchDelete").click(function(){
                    var checkBox = $("#dataTables tbody tr").find('input[type=checkbox]:checked');
                    var ids = checkBox.getInputId();
                    self.disable(ids)
                })


                $("#forumSave").click(function(){
                    self.save();
                })

                $("#removeImg").click(function () {
                    self.clearImageView();
                })
            },
            //日期选择
            dateInit: function () {
                var start = {
                    elem: '#start',
                    format: 'YYYY-MM-DD hh:mm:ss',
                    min: laydate.now(), //设定最小日期为当前日期
                    max: '2099-06-16 23:59:59', //最大日期
                    istime: true,
                    choose: function(datas){
                        end.min = datas; //开始日选好后，重置结束日的最小日期
                        end.start = datas; //将结束日的初始值设定为开始日
                    }
                };
                var end = {
                    elem: '#end',
                    format: 'YYYY-MM-DD hh:mm:ss',
                    min: laydate.now(),
                    max: '2099-06-16 23:59:59',
                    istime: true,
                    choose: function(datas){
                        start.max = datas; //结束日选好后，重置开始日的最大日期
                    }
                };
                laydate(start);
                laydate(end);
            },
            dataTableInit: function () {
                forumList.v.dTable = $health.dataTable($('#dataTables'), {
                    "processing": true,
                    "serverSide": true,
                    "searching":false,
                    "ordering": false,
                    "ajax": {
                        "url": "agenda/list",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "id"},
                        {"data": "title"},
                        {"data": "location"},
                        {"data": "startDate"},
                        {"data": "endDate"},
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
                            "<button   title='添加会议主讲人'  class='btn btn-danger btn-circle userPop'>" +
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
                        forumList.v.list=data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".disable").click(function(){
                            forumList.fn.disable([data.id]);
                        })
                        $('td', row).last().find(".edit").click(function(){
                            forumList.fn.edit(data.id);
                        })
                        $('td', row).last().find(".userPop").click(function(){
                            forumList.fn.userPop(data.id);
                        })
                    },
                    "fnServerParams": function (aoData) {
                        aoData.title = $("#title").val();
                        aoData.status = $("#status").val();
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
                    $("#imageUrl").val(image.relUrl);
                    $(".image_show").html("<img src='" + image.fullUrl + "' class='img-responsive' >");
                }
            },
            userPop:function(id){
                $('#popModal').modal('show').find('.modal-content').load("user/pop?agendaId="+id);
            },
            dropperInit: function () {
                $("#forumModal" + " .dropped").dropper({
                    postKey: "file",
                    action: "gen/save/image",
                    postData: {thumbSizes: '480x320'},
                    label: "把文件拖拽到此处",
                    maxSize: 204857
                }).on("fileComplete.dropper", forumList.fn.onFileComplete)
                        .on("fileError.dropper", forumList.fn.onFileError);
            },
            onFileComplete: function (e, file, response) {
                if (response.status == '200') {
                    forumList.fn.viewImage(response.data);
                } else {
                    $health.notify(response.msg, "error");
                }
            },
            disable:function(ids){
                if (ids.length > 0) {
                    $health.optNotify(function () {
                        $health.ajax("forum/delete", {ids:JSON.stringify(ids)}, function (result) {
                            forumList.fn.responseComplete(result);
                        })
                    },"你确定要删除吗？","确定");
                }
            },
            edit: function (id) {
                location.href="/ivf/agenda/details?id="+id;
            },
            save: function () {
                if(!$('#forumForm').isValid()) {
                    return false;
                };
                $("#forumForm").ajaxSubmit({
                    dataType: "json",
                    success: function (result) {
                        forumList.fn.responseComplete(result,true,$("#forumModal"));
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
                        forumList.v.dTable.ajax.reload(null, false);
                    }else{
                        forumList.v.dTable.ajax.reload();
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
        forumList.fn.init();
    });



</script>


</html>