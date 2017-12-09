/**
 * Created by lijm on 2017/8/7.
 */
App.service('httpService',['$http', '$q', 'toaster', 'urlService', function ($http, $q, toaster, urlService) {
    return{
        getByUrl:function (url,data) {
            var deferred = $q.defer();
            $http({
                url:urlService.getFullUrl(url),
                method:'GET',
                params:data || {}
            }).success(function (response) {
                console.log('data:请求成功');
                console.log(response.data);
                deferred.resolve(response.data);
            }).error(function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
                //toaster.pop('error', "链接失败", "服务器没有响应，请稍后再试!", error_timeout);
            });
            return deferred.promise;
        },
        postByJson:function (url,data) {
            var deferred = $q.defer();
            $http
                .post(urlService.getFullUrl(url), data)
                .then(function (response) {
                    deferred.resolve(response.data);
                }, function () {
                    deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
                    //toaster.pop('error', "链接失败", "服务器没有响应，请稍后再试!", error_timeout);
                });

            return deferred.promise;
        },
        postByUrl:function (url,data) {
            var deferred = $q.defer();
            $http
                .post(urlService.getFullUrl(url), '', {params: data})
                .then(function (response) {
                    deferred.resolve(response.data);
                }, function () {
                    deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
                    //toaster.pop('error', "链接失败", "服务器没有响应，请稍后再试!", error_timeout);
                });

            return deferred.promise;
        },
        postByForm:function (url,data) {
            var deferred = $q.defer();
            $http({
                method:'POST',
                url:urlService.getFullUrl(url),
                data:data,
                headers: {
                    'Content-Type': undefined
                },
            }).success(function (response) {
                console.log('post-request');
                deferred.resolve(response.data);
            }).error(function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
                //toaster.pop('error', "链接失败", "服务器没有响应，请稍后再试!", error_timeout);
            });
            return deferred.promise;
        }
    }
}])