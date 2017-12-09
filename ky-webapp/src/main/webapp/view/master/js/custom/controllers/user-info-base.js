App.controller('userInfoBaseCtrl', ['$scope', '$rootScope', '$stateParams', '$resource', 'userService', 'responseHandler', 'toaster',
    function ($scope, $rootScope, $stateParams, $resource, userService, responseHandler, toaster) {
        'use strict';

        if ($stateParams.userId == null) {
            return;
        }


        $scope.getUserInfo = function () {
            var data = {
                userId: $stateParams.userId
            };
            userService.query(data).then(function (response) {
                if (!responseHandler.isSuccess(response)) {
                    toaster.pop("error", "查询用户", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                }

                var info = response.data;
                $scope.userInfo = {
                    loginName: info.loginName,
                    nickName: info.nickName,
                    cellphoneNum: info.cellphoneNum,
                    email: info.email,
                    loginTime: info.loginTime,
                    roles: info.roles
                };
            }, function () {
                toaster.pop("error", "查询用户请求", "失败", error_timeout);
            });
        };

        $scope.getUserInfo();
    }
]);