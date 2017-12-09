/**
 * Created by yl on 2017/8/7.
 */
App.service('$qService', ['$q', '$http', function ($q, $http) {
    var qService = {};

    qService.getByUrl = function (url, data) {
        var deferred = $q.defer();

        $http
            .get(url, {"params": data})
            .then(function (response) {
                deferred.resolve(response.data);
            }, function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
            });

        return deferred.promise;
    };


    qService.postByUrl = function (url, data) {
        var deferred = $q.defer();

        $http
            .post(url, '', {params: data})
            .then(function (response) {
                deferred.resolve(response.data);
            }, function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
            });

        return deferred.promise;
    };

    qService.postByJson = function (url, data) {
        var deferred = $q.defer();

        $http
            .post(url, data)
            .then(function (response) {
                deferred.resolve(response.data);
            }, function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
            });

        return deferred.promise;
    };


    return qService;
}]);