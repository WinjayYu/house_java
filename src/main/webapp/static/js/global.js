$(function () {
    //menu
    $('#side-menu').find("li").each(function () {
        var menu_a = $(this).find("a").eq(0);
        var page_title = $("#page-wrapper .page-header").text();
        if (menu_a.text() == page_title) {
            menu_a.addClass("active");
            var ul = $(this).parent("ul .nav-second-level")
            if (ul.length > 0) {
                ul.addClass("in")
                ul.parent("li").addClass("active")
            }

        }
    })


    //小提示框
    $('.img_tooltip').tooltip({
        selector: "[data-toggle=tooltip]",
        container: "body"
    })

})


var $health = {
    v: {
        ajaxOption: {method: 'get', dataType: 'json', async: true},
        notifyMethod: null,
        dataTableL:{
            "language": {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            }
        }
    },
    notify: function (msg, status) {
        var option = {
            position: "top center",
            autoHideDelay: 2000,
            className: status,
            arrowSize: 10
        }
        $.notify(msg, option);
    },
    optNotify: function (method,title,button) {

        if($("#notifyjs-foo-alert-option").length>0){
            return false;
        }

        if(title===undefined){
            title = '确认删除么？删除后不可恢复！';
        }
        if(button===undefined){
            button = '删除';
        }

        $.notify({
            title: title,
            button:button
        }, {
            style: 'foo',
            autoHide: false,
            clickToHide: false,
            position: "top center"
        });
        if (method != undefined) {
            $health.v.notifyMethod = method;
        }
    },
    uiform: function () {
        jQuery('tbody input:checkbox').click(function () {
            if (jQuery(this).is(':checked')) {
                jQuery(this).parent().addClass('checked');
                jQuery(this).parents('tr').addClass('warning');
            } else {
                jQuery(this).parent().removeClass('checked');
                jQuery(this).parents('tr').removeClass('warning');
            }
        });
    },
    cutText: function (sub, length,less) {
        var str = "";
        if (sub && sub.length > length) {
            str =  sub.substr(0, length);
            if(less){
                str=str+less;
                str =  '<p title="'+sub+'">' + str + '</p>';
            }
        } else {
            str =  sub;
        }

        return str;
    },
    checkAll: function (obj) {
        var parentTable = jQuery(obj).parents('table');
        var ch = parentTable.find('tbody input[type=checkbox]');
        if (jQuery(obj).is(':checked')) {
            ch.each(function () {
                jQuery(this).prop('checked', true);
                jQuery(this).parent().addClass('checked');
                jQuery(this).parents('tr').addClass('warning');
            });
        } else {
            ch.each(function () {
                jQuery(this).removeAttr('checked')
                jQuery(this).parent().removeClass('checked');
                jQuery(this).parents('tr').removeClass('warning');
            });
        }
    },
    ajax: function (url, data, callbackFun, option) {
        if (option == null || option == undefined) {
            option = $health.v.ajaxOption;
        } else {
            if (option.method == null || option.method == undefined) {
                option.method = $health.v.ajaxOption.method;
            }
            if (option.dataType == null || option.dataType == undefined) {
                option.dataType = $health.v.ajaxOption.dataType;
            }
            if (option.async == null || option.async == undefined) {
                option.async = $health.v.ajaxOption.async;
            }
        }
        jQuery.ajax({
            dataType: option.dataType,
            url: url,
            data: data,
            async: option.async,
            success: function (data) {
                callbackFun(data);
            },
            statusCode: {
                401: function () {

                },
                403: function (res) {
                    $health.errCallBack(res);
                },
                500: function (res) {
                    $health.errCallBack(res);
                }
            }
        });
    },
    errCallBack:function(res){
        $health.notify(JSON.parse(res.responseText).msg, "error");
    },
    turnArray: function (arr) {
        var newArr = [];
        for (var i = arr.length - 1; i >= 0; i--) {
            newArr.push(arr[i])
        }
        return newArr;
    },
    /**
     * 清理表单参数
     * @param form
     * @param option boolean类型，为true清理select插件
     */
    clearForm: function (form) {
        form.find("textarea").val("");
        form.find("input[type!=checkbox]").val("");
        form.validator("cleanUp");

        form.find("input[type=checkbox]").attr("checked",false)
    },
    dataTable: function (obj, option) {

        var data = {
            "language": {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            }
        };
        return obj.DataTable($.extend(data, option))
    }

}


$.notify.addStyle('foo', {
    html: "<div id='notifyjs-foo-alert-option'>" +
    "<div class='clearfix'>" +
    "<div class='title' data-notify-html='title'/>" +
    "<div class='buttons'>" +
    "<button class='no'>取消</button>" +
    "<button class='yes' data-notify-text='button'></button>" +
    "</div>" +
    "</div>" +
    "</div>"
});

$(document).on('click', '.notifyjs-foo-base .no', function () {
    $(this).trigger('notify-hide');
});
$(document).on('click', '.notifyjs-foo-base .yes', function () {
    if ($health.v.notifyMethod != null) {
        eval("$health.v.notifyMethod()");
    }
    $(this).trigger('notify-hide');
});


(function ($) {
    $.fn.getInputId = function (sigle) {
        var checkIds = [];

        this.each(function () {
            checkIds.push($(this).val())
        });

        if (sigle) {
            if (checkIds.length > 1) {
                $health.notify('只能选择一条记录！', 'error');
                return false;
            }
            else if (checkIds.length == 0) {
                $health.notify('请选择一条记录操作！', 'error');
                return false;
            }
            else {
                return checkIds[0];
            }
        } else {
            if(checkIds.length == 0){
                $health.notify('请选择至少一条记录操作！', 'error');
                return false;
            }else{
                return checkIds;
            }

        }
    };
})(jQuery);
