<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="inc/taglibs.jsp" %>
<div class="modal-header">
    <h4 class="modal-title" >${popTitle}</h4>
</div>
<div class="modal-body">
    <input type="hidden" id="userPopType" value="${type}">
    <input type="hidden" id="userPopForumId" value="${forumId}">
    <input type="hidden" id="userPopAgendaId" value="${agendaId}">
    <input type="hidden" id="userPopCategory" value="${category}">
    <div class="row">
        <div class="col-lg-12">
            <section class="panel">
                <div class="table-responsive">
                    <table width="100%"  class="table table-striped table-bordered table-hover" id="popDataTables">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>姓名</th>
                            <th>医院</th>
                            <th>职位</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    </div>
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-sm btn-default btn-s-large" data-dismiss="modal">关闭</button>
</div>




<script type="text/javascript">


    var popUserList = {
        v: {
            id: "popUserList",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {

                var self = this;

                self.dataTableInit();

            },
            dataTableInit: function () {
                popUserList.v.dTable = $health.dataTable($('#popDataTables'), {
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
                        {"data": ""}
                    ],
                    "columnDefs": [
                        {
                            "data": null,
                            "defaultContent":
                            "<button   title='添加'  class='btn btn-primary btn-circle add'>" +
                            "<i class='fa fa-plus'></i>" +
                            "</button>"+
                            "&nbsp;&nbsp;" +
                            "<button  title='删除'  class='btn btn-default btn-circle delete'>" +
                            "<i class='fa fa-close'></i>" +
                            "</button>",
                            "targets": -1
                        }
                    ],
                    "createdRow": function (row, data, index) {
                        if(data.thisForum){
                            $(row).addClass("success")
                        }
                    },
                    "footerCallback": function( tfoot, data, start, end, display ) {
                        popUserList.v.list=data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".add").click(function(){
                            popUserList.fn.add(data.id);
                        })
                        $('td', row).last().find(".delete").click(function(){
                            popUserList.fn.delete(data.id);
                        })
                    },
                    "fnServerParams": function (aoData) {
                        aoData.type = $("#userPopType").val()
                        aoData.forumId = $("#userPopForumId").val()
                        aoData.agendaId = $("#userPopAgendaId").val()
                        aoData.category = $("#userPopCategory").val()
                    }
                });
            },
            add:function(id){
                var forumId = $("#userPopForumId").val();
                var agendaId = $("#userPopAgendaId").val();
                var category = $("#userPopCategory").val();
                $health.ajax("user/pop/save", {userId:id,category:category,forumId:forumId,agendaId:agendaId}, function (result) {
                    popUserList.fn.responseComplete(result,true)
                })
            },
            delete:function(id){
                var forumId = $("#userPopForumId").val()
                var agendaId = $("#userPopAgendaId").val();
                var category = $("#userPopCategory").val();
                $health.ajax("user/pop/delete", {userId:id,category:category,forumId:forumId,agendaId:agendaId}, function (result) {
                    popUserList.fn.responseComplete(result,true)
                })
            },
            responseComplete: function (result,action,modal) {
                if (result.status == "200") {
                    if(modal){
                        modal.modal("hide");
                    }
                    if(action){
                        popUserList.v.dTable.ajax.reload(null, false);
                    }else{
                        popUserList.v.dTable.ajax.reload();
                    }
                    $health.notify(result.msg, "success");
                } else {
                    $health.notify(result.msg, "error");
                }
            },

        }
    }

    $(document).ready(function () {
        popUserList.fn.init();
    });



</script>