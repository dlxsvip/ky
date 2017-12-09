App.service('roleService', ['$q', '$http', '$location', 'urlService', function ($q, $http, $location, urlService) {
    var roleService = {};

    roleService.queryRoles = function (jsonParams) {
        var url = urlService.getFullUrl('/role/queryByPage');

        return getByUrl(url, jsonParams);
    };

    roleService.queryRole = function (jsonParams) {
        var url = urlService.getFullUrl('/role/query');

        return getByUrl(url, jsonParams);
    };

    roleService.addRole = function (jsonParams) {
        var url = urlService.getFullUrl('/role/create');

        return postByJson(url, jsonParams);
    };

    roleService.updateRole = function (jsonParams) {
        var url = urlService.getFullUrl('/role/update');

        return postByJson(url, jsonParams);
    };

    roleService.deleteRole = function (jsonParams) {
        var url = urlService.getFullUrl('/role/delete');

        return postByUrl(url, jsonParams);
    };




    function getByUrl(url, data) {
        var deferred = $q.defer();

        $http
            .get(url, {"params": data})
            .then(function (response) {
                deferred.resolve(response.data);
            }, function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
            });

        return deferred.promise;
    }


    function postByUrl(url, data) {
        var deferred = $q.defer();

        $http
            .post(url, '', {params: data})
            .then(function (response) {
                deferred.resolve(response.data);
            }, function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
            });

        return deferred.promise;
    }

    function postByJson(url, data) {
        var deferred = $q.defer();

        $http
            .post(url, data)
            .then(function (response) {
                deferred.resolve(response.data);
            }, function () {
                deferred.reject({code: 999, msg: "服务器没有响应，请稍后再试!"});
            });

        return deferred.promise;
    }


    return roleService;
}]);