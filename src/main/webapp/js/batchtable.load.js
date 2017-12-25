	$(function(){
         var tableEditRefresh = function () {
            $('#batchtable').Tabledit({
                url: '/web/batch/job/operation',
                //hideIdentifier: true,
                columns: {
                    identifier: [0, 'id'],
                    editable: [[1, 'collection'], [2, 'service'], [3, 'refreshDuration'], [4, 'lastExecutionDate']]
                }
            });
         }

         tableEditRefresh();
         $('#batchtable').on('reset-view.bs.table', tableEditRefresh);

         var counter = $('#batchtable tr').length;

         $("#add_row").click(function(){

            $('#batchtable').bootstrapTable('insertRow', {
                         index: counter,
                         row: {
                             id: counter,
                             collection: "",
                             service: "",
                             refreshDuration: "",
                             lastExecutionDate: ""
                         }
                     });
            counter++;
         });
     });

// https://github.com/wenzhixin/bootstrap-table/issues/2210
// http://bootstrap-table.wenzhixin.net.cn/documentation/
// http://markcell.github.io/jquery-tabledit/#documentation
// http://markcell.github.io/jquery-tabledit/#examples
// https://github.com/wenzhixin/bootstrap-table-examples/tree/master/methods
// http://jsfiddle.net/wenyi/e3nk137y/36/
// https://bootsnipp.com/snippets/featured/dynamic-table-row-creation-and-deletion
// http://issues.wenzhixin.net.cn/bootstrap-table/
// http://bootstrap-table.wenzhixin.net.cn/examples/