<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="inc/taglibs.jsp" %>
<div class="modal-header">
    <h4 class="modal-title" >添加议程</h4>
</div>
<div class="modal-body">
    <input type="hidden" id="popTagId" value="${tag.id}">
    <input type="hidden" id="tagGropId" value="${tag.tagGrop.id}">
    <input type="hidden" id="popForumId" value="${tag.tagGrop.forum.id}">
    <div class="row">
        <div class="col-lg-12">
            <section class="panel">
                <div class="table-responsive">
                    <table width="100%"  class="table table-striped table-bordered table-hover" id="popDataTables">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>标题</th>
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


    var popAgendaList = {
        v: {
            id: "popAgendaList",
            list: [],
            dTable: null
        },
        fn: {
            init: function () {

                var self = this;

                self.dataTableInit();
             
            },
            dataTableInit: function () {
                popAgendaList.v.dTable = $health.dataTable($('#popDataTables'), {
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
                       if(data.thisTag){
                           $(row).addClass("success")
                       }
                    },
                    "footerCallback": function( tfoot, data, start, end, display ) {
                        popAgendaList.v.list=data
                    },
                    "rowCallback": function (row, data) {
                        $('td', row).last().find(".add").click(function(){
                            popAgendaList.fn.add(data.id);
                        })
                        $('td', row).last().find(".delete").click(function(){
                            popAgendaList.fn.delete(data.id);
                        })
                    },
                    "fnServerParams": function (aoData) {
                        aoData.forumId = $("#popForumId").val();
                        aoData.tagId = $("#popTagId").val();
                    }
                });
            },
            add:function(id){
                var tagId = $("#popTagId").val()
                $health.ajax("agenda/pop/save", {id:id,tagId:tagId}, function (result) {
                    popAgendaList.fn.responseComplete(result,true)
                })
            },
            delete:function(id){
                var tagId = $("#popTagId").val()
                $health.ajax("agenda/pop/delete", {id:id,tagId:tagId}, function (result) {
                    popAgendaList.fn.responseComplete(result,true)
                })
            },
            responseComplete: function (result,action,modal) {
                if (result.status == "200") {
                    if(modal){
                        modal.modal("hide");
                    }
                    if(action){
                        popAgendaList.v.dTable.ajax.reload(null, false);
                    }else{
                        popAgendaList.v.dTable.ajax.reload();
                    }
                    $health.notify(result.msg, "success");
                } else {
                    $health.notify(result.msg, "error");
                }
            },

        }
    }

    $(document).ready(function () {
        popAgendaList.fn.init();
    });



</script>