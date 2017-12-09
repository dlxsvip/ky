App.service('userService', ['$q', '$http', '$location', 'urlService', function ($q, $http, $location, urlService) {
    var userService = {};

    userService.queryUser = function (jsonParams) {
        var url = urlService.getFullUrl('/user/queryByPage');

        return getByUrl(url, jsonParams);
    };

    userService.query = function (jsonParams) {
        var url = urlService.getFullUrl('/user/query');

        return getByUrl(url, jsonParams);
    };

    userService.addUser = function (jsonParams) {
        var url = urlService.getFullUrl('/user/create');

        return postByJson(url, jsonParams);
    };

    userService.updateUser = function (jsonParams) {
        var url = urlService.getFullUrl('/user/update');

        return postByJson(url, jsonParams);
    };

    userService.deleteUser = function (jsonParams) {
        var url = urlService.getFullUrl('/user/delete');

        return postByUrl(url, jsonParams);
    };

    /**
     * 导出初始密码
     * @param ids
     */
    userService.exportSystemInitPasswords = function (ids) {
        var url = urlService.getFullUrl('/user/exportSystemInitPasswords');
        if(navigator.userAgent.indexOf("Firefox") >-1){
            window.open(url+"?userIds="+ids);
        }else {
            window.location.href = url+"?userIds="+ids;
        }
    };

    /**
     * 重置密码
     * @param jsonParams
     * @returns {*}
     */
    userService.resetPassword = function (jsonParams) {
        var url = urlService.getFullUrl('/user/resetPassword');

        return postByJson(url, jsonParams);
    };

    /**
     * 修改密码
     * @param jsonParams
     * @returns {*}
     */
    userService.updatePassword = function (jsonParams) {
        var url = urlService.getFullUrl('/user/updatePassword');

        return postByJson(url, jsonParams);
    };

    /**
     * 校验密码
     * @param jsonParams
     * @returns {*}
     */
    userService.checkPassword = function (jsonParams) {
        var url = urlService.getFullUrl('/user/checkPassword');

        return getByUrl(url, jsonParams);
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


    return userService;
}]);