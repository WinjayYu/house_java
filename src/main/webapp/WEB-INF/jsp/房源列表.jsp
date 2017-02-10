<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="inc/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@ include file="inc/meta.jsp" %>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>房源列表</title>
    <%@ include file="inc/css.jsp" %>
</head>

<body>

<div id="posts" class="wrapper">

    <%@ include file="inc/nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">房源列表</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-default">

                    <div class="panel-heading">

                        <form class="navbar-form navbar-right" role="search">
                            <div class="form-group">
                                <%-- <label>标题：</label>--%>
                                <input type="text" class="form-control" value="" id="title" maxlength="20"
                                       placeholder="请输入小区名称、地址...">
                            </div>
                            <%--<div class="form-group">
                                <label>状态：</label>
                                <select class="form-control input-sm" id="status">
                                    ${statusList}
                                </select>
                            </div>--%>
                            <button type="button" id="c_search" class="btn btn-info btn-sm">查询</button>
                        </form>

                        <div class="clearfix"></div>
                    </div>

                    <!-- /.panel-heading -->

                    <div class="panel-body">
                        <div class="row">
                            <ul calss="list-group">
                                <li>
                                    <c:forEach items="${houseList}" var="house">
                                        <div class="col-lg-6">
                                            <p>${house.title}</p>
                                        </div>
                                    </c:forEach>
                                </li>
                            </ul>
                        </div>
                        <%--<div class="table-responsive">--%>
                        <%--<table width="100%" class="table table-striped table-bordered table-hover" id="dataTables">--%>
                        <%--<colgroup>--%>
                        <%--<col class="gradeA even"/>--%>
                        <%--<col class="gradeA odd"/>--%>
                        <%--<col class="gradeA even"/>--%>
                        <%--<col class="gradeA odd"/>--%>
                        <%--<col class="gradeA even"/>--%>
                        <%--<col class="gradeA odd"/>--%>
                        <%--<col class="gradeA even"/>--%>
                        <%--</colgroup>--%>
                        <%--<thead>--%>
                        <%--<tr>--%>
                        <%--<th>ID</th>--%>
                        <%--<th>标题</th>--%>
                        <%--<th>户型</th>--%>
                        <%--<th>状态</th>--%>
                        <%--<th>操作</th>--%>
                        <%--</tr>--%>
                        <%--</thead>--%>
                        <%--<tbody>--%>
                        <%--</tbody>--%>
                        <%--</table>--%>
                        <%--</div>--%>

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

    var _this = {
        v: {
            id: "_this",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {

                var self = this;

                self.dataTableInit();

                $("#c_search").click(function () {
                    _this.v.dTable.ajax.reload();
                });

                $("#batchDelete").click(function () {
                    var checkBox = $("#dataTables tbody tr").find('input[type=checkbox]:checked');
                    var ids = checkBox.getInputId();
                    self.disable(ids)
                })

                $("#newUser").click(function () {
                    self.showModal("userModal", "添加管理员帐号");
                })

                $("#userSave").click(function () {
                    self.save();
                })
            },
            dataTableInit: function () {
                _this.v.dTable = $health.dataTable($('#dataTables'), {
                    "processing": true,
                    "serverSide": true,
                    "searching": false,
                    "ordering": false,
                    "ajax": {
                        "url": "mgt/house/list",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": "id"},
                        {"data": "title"},
                        {"data": "layout"},
                        {"data": "statusDesc"},
                        {"data": ""}
                    ],
                    "columnDefs": [
                        {
                            "data": null,
                            "defaultContent": "",
                            "targets": -1
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        var mobileHtml = '<a target="_blank" href="mgt/house/detail?id=' + data.id + '">' + data.id + '</a>';
                        $('td', row).eq(0).html(mobileHtml);
                        var btnHtml = "<button   title='通过'  class='btn btn-primary btn-circle pass'>" +
                                "<i class='fa fa-check'></i>" +
                                "</button>" +
                                "&nbsp;&nbsp;" +
                                "<button  title='驳回'  class='btn btn-default btn-circle reject'>" +
                                "<i class='fa fa-close'></i>" +
                                "</button>";
                        if (data.status == "10") {
                            $('td', row).last().html(btnHtml);
                        }

                    },
                    "footerCallback": function (tfoot, data, start, end, display) {
                        _this.v.list = data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".pass").click(function () {
                            _this.fn.approve(data.id, "0");
                        });
                        $('td', row).last().find(".reject").click(function () {
                            _this.fn.approve(data.id, "1");
                        });
                    },
                    "fnServerParams": function (aoData) {
                        aoData.title = $("#title").val();
                        aoData.status = $("#status").val();
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
                _this.fn.showModal("userDetailModal", "用户详情");
                for (var key in item) {
                    var element = $("#userDetailModal span[_name=" + key + "]")
                    var text = item[key];
                    if (text == null) {
                        text = "";
                    }
                    if (element.length > 0) {
                        if (text.length <= 0) {
                            element.parent().parent().hide();
                        } else {
                            element.parent().parent().show();
                            element.text(text);
                        }
                    }
                }
            },
            disable: function (ids) {
                if (ids.length > 0) {
                    $health.optNotify(function () {
                        $health.ajax("user/delete", {ids: JSON.stringify(ids)}, function (result) {
                            _this.fn.responseComplete(result);
                        })
                    }, "你确定要删除吗？", "确定");
                }
            },
            approve: function (id, type) {
                var btn = "通过";
                if (type == "1") {
                    btn = "驳回";
                }
                $health.optNotify(function () {
                    $health.ajax("mgt/house/approve", {id: id, type: type}, function (result) {
                        _this.fn.responseComplete(result, true);
                    })
                }, "你确定要" + btn + "吗？", "确定");
            },
            edit: function (id) {
                _this.fn.showModal("userModal", "修改用户");
                var items = _this.v.list;
                $.each(items, function (index, item) {
                    if (item.id == id) {
                        for (var key in item) {
                            var element = $("#userForm :input[name=" + key + "]")
                            if (element.length > 0) {
                                element.val(item[key]);
                            }
                            if (key == "imgUrl") {
                                if (item.imgUrl) {
                                    _this.fn.viewImage(item.imgUrl);
                                } else {
                                    _this.fn.clearImageView();
                                }
                            }
                            if (key == "description") {
                                $("#description").text(item[key])
                            }
                        }
                    }
                })
            },
            save: function () {
                if (!$('#userForm').isValid()) {
                    return false;
                }
                ;
                $("#userForm").ajaxSubmit({
                    dataType: "json",
                    success: function (result) {
                        _this.fn.responseComplete(result, true, $("#userModal"));
                    },
                    error: function (result) {
                        $health.errCallBack(result);
                    }
                });
            },
            responseComplete: function (result, action, modal) {
                if (result.status == "0") {
                    if (modal) {
                        modal.modal("hide");
                    }
                    if (action) {
                        _this.v.dTable.ajax.reload(null, false);
                    } else {
                        _this.v.dTable.ajax.reload();
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
        _this.fn.init();
    });


</script>


</html>