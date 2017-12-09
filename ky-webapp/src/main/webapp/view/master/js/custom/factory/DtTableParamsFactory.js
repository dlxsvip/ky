/**
 * Created by lijm on 2017/8/7.
 */
App.factory('DtTableParamsFactory' , function(ngTableParams){
    var dtTableParamsFactory = {};
    dtTableParamsFactory.getTableParams =  function (getDataFn,opt) {
        var opt = opt || {};
        return new ngTableParams({
            page: opt.page || 1,            // show first page
            count: opt.count || 10,           // count per page
            sorting: opt.sorting || {}
        }, {
            counts: opt.counts || [10,25,50,100],
            getData : getDataFn
        });
    }
    return dtTableParamsFactory
});