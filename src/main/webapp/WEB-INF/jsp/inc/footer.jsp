<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<footer >
    <div class="container" >
        <div class="row">
            <div class="col-lg-12">
                <div class="submit">
                <p class="text-center"><a href="static/sb-admin-2/index.html" target="_blank"> sb admin bootstrap template</a></p>
                </div>
            </div>
        </div>
    </div>

</footer>


<script src="static/js/plugins/notify-bootstap.min.js"></script>


<!-- Metis Menu Plugin JavaScript -->
<script src="static/js/plugins/metisMenu/metisMenu.min.js"></script>

<script src="static/js/plugins/jquery.form.min.js"></script>

<script src="static/js/plugins/linkagesel/js/comm.js"></script>

<script src="static/js/plugins/linkagesel/js/linkagesel-min.js"></script>

<script src="static/js/plugins/nice-validator-0.10.2/dist/jquery.validator.min.js?local=zh-CN"></script>


<script src="static/js/plugins/jquery.uniform.min.js"></script>

<!--date-->
<script src="static/js/plugins/laydate/laydate.js"></script>
<script src="static/js/plugins/layer/layer.js"></script>

<!-- DataTables JavaScript -->
<script src="static/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="static/js/plugins/dataTables/dataTables.bootstrap.js"></script>

<!-- Custom Theme JavaScript -->
<script src="static/js/sb-admin-2.js"></script>
<script src="static/js/constant.js"></script>
<script src="static/js/global.js"></script>

<script type="text/javascript">
    $(function () {
        $("#mediator").click(function () {
            showModal();
        });
    });

    function modifyPwd() {
        if(!$('#pwdForm').isValid()){
            return false;
        };
        $("#pwdForm").ajaxSubmit({
            dataType: "json",
            success: function (result) {
                if (result.status == '0') {
                    alert(result.msg)
                    location.reload();
                } else {
                    $health.notify(result.msg, "error");
                }
            }
        })
    }

    function showModal() {
        $("#pwdModal").modal("show");
        $health.clearForm($("#pwdModal"));
    }


</script>