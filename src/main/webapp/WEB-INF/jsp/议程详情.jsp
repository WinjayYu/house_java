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
    <title>议程详情</title>
    <%@ include file="inc/css.jsp" %>
    <link href="static/js/plugins/dropper/jquery.fs.dropper.css" rel="stylesheet">
    <script src="static/js/plugins/dropper/jquery.fs.dropper.js"></script>
    <script src="static/js/plugins/formstone/core.js"></script>
    <link href="static/js/plugins/formstone/themes/light.css" rel="stylesheet">
    <link href="static/js/plugins/formstone/upload/upload.css" rel="stylesheet">
    <script src="static/js/plugins/formstone/upload/upload.js"></script>
    upload.css
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
            <form id="agendaForm" method="post" enctype="multipart/form-data" action="agenda/save" class="form-horizontal" role="form">
                <input type="hidden" id="agendaId" name="id" value="${agenda.id}">

                <div class="form-group">
                    <label  class="col-sm-2 control-label">议程标题:</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="title" maxlength="20"
                                  value="${agenda.title}" placeholder="议程标题" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">选择会议：</label>
                    <div class="col-sm-3">
                        <select class="form-control " name="forum.id" id="forumId">
                            <c:forEach items="${forumList}" var="forum">
                                <option <c:if test="${agenda.forum.id==forum.id}">selected</c:if> value="${forum.id}">${forum.titile}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="start" class="col-sm-2 control-label">开始时间:</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="startDate" id="start" value="<fmt:formatDate value="${agenda.startDate}" pattern="yyyy-MM-dd HH:mm:ss" />" autocomplete="off" placeholder="">

                    </div>
                </div>
                <div class="form-group">
                    <label for="end" class="col-sm-2 control-label">结束时间:</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="endDate" id="end" value="<fmt:formatDate value="${agenda.endDate}" pattern="yyyy-MM-dd HH:mm:ss" />" autocomplete="off" placeholder="">
                    </div>
                </div>

                <div class="form-group">
                    <label  class="col-sm-2 control-label">地点描述:</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="location" maxlength="20"
                                value="${agenda.location}" placeholder="地点描述" />
                    </div>
                </div>

                <div class="form-group img_tooltip" >
                    <label  class="col-sm-2 control-label">议程图片:</label>

                    <div class="col-sm-3">
                        <input type="hidden" id="imageUrl" name="bgImgUrl" value="${agenda.bgImgUrl}">

                        <div class="image_show"  <c:if test="${agenda.bgImgUrl}==null}"> style="display: none"  </c:if>>
                            <img src="${agenda.bgImgUrl}" class='img-responsive' >
                        </div>
                        <div class="image_handle"  <c:if test="${agenda.bgImgUrl!=null}">  style="display: none"  </c:if>data-toggle="tooltip" data-placement="top" title="" data-original-title="建议上传宽480px高320px的图片">
                            <div class="dropped"></div>
                        </div>
                    </div>
                    <div class="col-sm-5">
                        <a href="javascript:void(0)" id="removeImg" class="btn btn-info" role="button" >删除图片</a>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="col-sm-2 control-label">议程介绍:</label>
                    <div class="col-sm-6">
                        <textarea type="text" class="form-control"  name="description" maxlength="500"
                                  rows="3"

                                  placeholder="请输入议程介绍">${agenda.description}</textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="col-sm-2 control-label">会议洞见:</label>
                    <div class="col-sm-6">
                        <textarea type="text" class="form-control"  name="aboutContent" maxlength="500"
                                  rows="3"

                                  placeholder="请输入会议洞见">${agenda.aboutContent}</textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="col-sm-2 control-label">议程状态:</label>
                    <div  class="col-sm-3">
                        <select type="select" class="form-control" name="status" id="status">
                            <option value="1" <c:if test="${agenda.status==1}">selected</c:if>>启用</option>
                            <option value="0" <c:if test="${agenda.status==0}">selected</c:if>>禁用</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="col-sm-2 control-label">热点议程:</label>
                    <div  class="col-sm-3">
                        <select type="select" class="form-control" name="hot" id="hot">
                            <option value="1" <c:if test="${agenda.hot==1}">selected</c:if>>热点</option>
                            <option value="0" <c:if test="${agenda.hot==0}">selected</c:if>>非热点</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label  class="col-sm-2 control-label">议程排序:</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="hotSort" maxlength="20"
                                value="${agenda.hotSort}" placeholder="议程排序" />
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label">议程附件:</label>
                    <div class="col-sm-3">
                        <div class="upload"></div>
                        <div class="filelists">
                            <h5>Complete</h5>
                            <ol class="filelist complete">
                            </ol>
                            <h5>Queued</h5>
                            <ol class="filelist queue">
                            </ol>
                            <span class="cancel_all">Cancel All</span>
                        </div>
                    </div>


                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" id="submitAgenda" class="btn btn-primary">提交</button>
                    </div>
                </div>
            </form>

        </div>
    </div>
    <!-- /#page-wrapper -->
</div>
<!-- /#wrapper -->

<%@include file="inc/footer.jsp" %>


</body>

<script type="text/javascript">
    var agenda = {
        v: {
            id: "agenda",
            list: [],
            dTable: null
        },
        fn: {
            init: function() {
                var self = this;
                $("#submitAgenda").click(function(){
                    agenda.fn.save();
                })
                $(".page-header").html("议程详情");
                self.dateInit();
                self.dropperInit();


                $("#removeImg").click(function () {
                    self.clearImageView();
                })
            },
            //日期选择
            dateInit: function () {
                var start = {
                    elem: '#start',
                    format: 'YYYY-MM-DD hh:mm:ss',
                    min: '2015-01-01 23:59:59',
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
                    min: '2015-01-01 23:59:59',
                    max: '2099-06-16 23:59:59',
                    istime: true,
                    choose: function(datas){
                        start.max = datas; //结束日选好后，重置开始日的最大日期
                    }
                };
                laydate(start);
                laydate(end);
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
                $("#agendaForm" + " .dropped").dropper({
                    postKey: "file",
                    action: "gen/save/image",
                    postData: {thumbSizes: '480x320'},
                    label: "把文件拖拽到此处",
                    maxSize: 204857
                }).on("fileComplete.dropper", agenda.fn.onFileComplete)
                        .on("fileError.dropper", agenda.fn.onFileError);


                // PDF 上传
                $(".upload").upload({
                    postKey: "file",
                    action: "gen/save/image",
                    label: "把文件拖拽到此处",
                    multiple:true,
                    theme:"fs-light",
                    maxSize: 1073741824,
//                    beforeSend: onBeforeSend
                }).on("start.upload", onStart)
                        .on("complete.upload", onComplete)
                        .on("filestart.upload", onFileStart)
                        .on("fileprogress.upload", onFileProgress)
                        .on("filecomplete.upload", onFileComplete)
                        .on("fileerror.upload", onFileError)
                        .on("queued.upload", onQueued);
                $(".filelist.queue").on("click", ".cancel", onCancel);
                $(".cancel_all").on("click", onCancelAll);

                function onCancel(e) {
//                    console.log("Cancel");
                    var index = $(this).parents("li").data("index");
                    $(this).parents("form").find(".upload").upload("abort", parseInt(index, 10));
                }

                function onCancelAll(e) {
//                    console.log("Cancel All");
                    $(this).parents("form").find(".upload").upload("abort");
                }

                function onBeforeSend(formData, file) {
//                    console.log("Before Send");
                    formData.append("test_field", "test_value");
                    // return (file.name.indexOf(".jpg") < -1) ? false : formData; // cancel all jpgs
                    return formData;
                }

                function onQueued(e, files) {
//                    console.log("Queued");
                    var html = '';
                    for (var i = 0; i < files.length; i++) {
                        html += '<li data-index="' + files[i].index + '"><span class="file">' + files[i].name + '</span><span class="cancel">Cancel</span><span class="progress">Queued</span></li>';
                    }

                    $(this).parents("form").find(".filelist.queue")
                            .append(html);
                }

                function onStart(e, files) {
                    //console.log("Start");
                    $(this).parents("form").find(".filelist.queue")
                            .find("li")
                            .find(".progress").text("Waiting");
                }

                function onComplete(e) {
                    ////console.log("Complete");
                    // All done!
                }

                function onFileStart(e, file) {
                    //console.log("File Start");
                    $(this).parents("form").find(".filelist.queue")
                            .find("li[data-index=" + file.index + "]")
                            .find(".progress").text("0%");
                }

                function onFileProgress(e, file, percent) {
                    //console.log("File Progress");
                    $(this).parents("form").find(".filelist.queue")
                            .find("li[data-index=" + file.index + "]")
                            .find(".progress").text(percent + "%");
                }

                function onFileComplete(e, file, response) {
                    //console.log("File Complete");
                    if (response.trim() === "" || response.toLowerCase().indexOf("error") > -1) {
                        $(this).parents("form").find(".filelist.queue")
                                .find("li[data-index=" + file.index + "]").addClass("error")
                                .find(".progress").text(response.trim());
                    }
                    else {
                        var $target = $(this).parents("form").find(".filelist.queue").find("li[data-index=" + file.index + "]");
                        $target.find(".file").text(file.name);
                        $target.find(".progress").remove();
                        $target.find(".cancel").remove();
                        $target.appendTo($(this).parents("form").find(".filelist.complete"));
                    }
                }

                function onFileError(e, file, error) {
                    //console.log("File Error");
                    $(this).parents("form").find(".filelist.queue")
                            .find("li[data-index=" + file.index + "]").addClass("error")
                            .find(".progress").text("Error: " + error);
                }

                function onBeforeSend(formData, file) {
//                    //console.log("Before Send");
//                    formData.append("test_field", "test_value");
//                    return (file.name.indexOf(".jpg") < -1) ? false : formData; // cancel all jpgs
//                    return formData;
                }

            },
            onFileComplete: function (e, file, response) {
                if (response.status == '200') {
                    agenda.fn.viewImage(response.data);
                } else {
                    $health.notify(response.msg, "error");
                }
            },
            onFileError: function (e, file, error) {
                $health.notify(error, "error");
            },
            save: function () {
                if(!$('#agendaForm').isValid()) {
                    return false;
                };
                $("#agendaForm").ajaxSubmit({
                    dataType: "json",
                    success: function (result) {
                        agenda.fn.responseComplete(result);
                    }
                });
            },
            responseComplete: function (result) {
                if (result.status == "200") {
                    $("#agendaId").val(result.data.id)
                    $health.notify(result.msg, "success");
                } else {
                    $health.notify(result.msg, "error");
                }
            }
        }
    }

    $(document).ready(function () {
        agenda.fn.init();
    });



</script>


</html>