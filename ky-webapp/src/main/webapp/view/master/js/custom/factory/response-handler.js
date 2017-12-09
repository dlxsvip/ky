/**
 * Created by yl on 2017/7/16.
 */
App.factory('responseHandler', function() {
    return {
        isSuccess: function(response) {
            var ret = false;
            if ("success" == response.result) {
                ret = true;
            }
            return ret;
        },
        getData: function(response) {
            return response.data;
        },
        getErrorMsg : function(response) {
            return response.errorMsg;
        },
        getErrorDetail: function(response) {
            return response.errorDetail;
        },
        getTotalRows: function(response) {
            var totalRows = 0;
            if ("success" == response.result) {
                totalRows = response.data.totalRows;
            }
            return totalRows;
        },
        getRows: function(response) {
            var rows = [];
            if ("success" == response.result) {
                rows = response.data.rows;
            }
            return rows;
        }
    }
})
