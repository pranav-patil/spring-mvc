    /*
        https://github.com/wenzhixin/bootstrap-table/issues/2210
        http://bootstrap-table.wenzhixin.net.cn/documentation/
        http://markcell.github.io/jquery-tabledit/#documentation
        http://markcell.github.io/jquery-tabledit/#examples
        https://github.com/wenzhixin/bootstrap-table-examples/tree/master/methods
        http://jsfiddle.net/wenyi/e3nk137y/36/
        https://bootsnipp.com/snippets/featured/dynamic-table-row-creation-and-deletion
        http://issues.wenzhixin.net.cn/bootstrap-table/
        http://bootstrap-table.wenzhixin.net.cn/examples/
    */

	$(function() {

	    $.notifyDefaults({
	        newest_on_top: false,
	        offset: 20,
	        spacing: 10,
	        delay: 5000,
	        timer: 1000,
	        placement: {
	            from: "top",
	            align: "right"
	        },
	        animate: {
	            enter: 'animated fadeInDown',
	            exit: 'animated fadeOutUp'
	        }
	    });

	    var errorNotification = function(messageText) {
	        $.notify({
	            icon: 'fa fa-exclamation-triangle',
	            message: messageText
	        }, {
	            type: 'danger',
	        });
	    }

	    var tableEditRefresh = function() {
	        $('#batchtable').Tabledit({
	            url: '/web/batch/job/operation',
	            hideIdentifier: true,
	            restoreButton: false,
	            columns: {
	                identifier: [0, 'id'],
	                editable: [
	                    [1, 'collection'],
	                    [2, 'service'],
	                    [3, 'refreshDuration'],
	                    [4, 'lastExecutionDate']
	                ]
	            },
	            onSuccess: function(data, textStatus, jqXHR) {
	                $('#batchtable').bootstrapTable('refresh');
	            },
	            onFail: function(jqXHR, textStatus, errorThrown) {
	                var responseText = jQuery.parseJSON(jqXHR.responseText);
	                console.log(responseText);
	                errorNotification(responseText.message);
	                //alert(responseText.message);
	            }
	        });
	    }

	    tableEditRefresh();
	    $('#batchtable').on('reset-view.bs.table', tableEditRefresh);

	    $("#add_job").click(function() {
	        $("#createBatchJobForm")[0].reset();
	    });

	    $("#createBatchJobForm").submit(function(event) {
	        event.preventDefault();

	        $.ajax({
	            type: "POST",
	            url: "/web/batch/job/operation",
	            contentType: 'application/x-www-form-urlencoded',
	            dataType: "json",
	            data: $('#createBatchJobForm').serialize(),
	            success: function(json, textStatus, jqXHR) {

	                /*
	                $('#batchtable').bootstrapTable('insertRow', {
	                     index: $('#batchtable tr').length + 1,
	                     row: {
	                         id: json.id,
	                         collection: json.collection,
	                         service: json.service,
	                         refreshDuration: json.refreshDuration,
	                         lastExecutionDate: json.lastExecutionDate
	                     }
	                });
	                */

	                $("#createBatchJobForm")[0].reset();
	                $('#createBatchJobModal').modal('hide')
	                $('#batchtable').bootstrapTable('refresh');
	            },
	            error: function(jqXHR, textStatus, errorThrown) {
	                var responseText = jQuery.parseJSON(jqXHR.responseText);
	                console.log(responseText);
	                alert(responseText.message);
	            }
	        });
	    });
	});