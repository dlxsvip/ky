App.controller('userInfoUpdatePwdCtrl', ['$scope', '$rootScope', '$stateParams', '$resource', 'userService', 'responseHandler', 'toaster', 'cryptoService',
    function ($scope, $rootScope, $stateParams, $resource, userService, responseHandler, toaster, cryptoService) {
        'use strict';

        if ($stateParams.userId == null) {
            return;
        }
        $scope.pwdReg = /(?=^.{8,20}$)(?!^\d*$)(?!^[\W_]*$)(?!^[a-zA-Z]*$).*$/;
        $scope.nickName = $stateParams.nickName;

        $scope.checkPassword = function (oldPassword) {
            var data = {
                userId: $stateParams.userId,
                oldPassword: cryptoService.aesEncrypt(oldPassword)
            };
            userService.checkPassword(data).then(function (response) {
                if (!responseHandler.isSuccess(response)) {
                    toaster.pop("error", "校验密码", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                } else {
                    toaster.pop("success", "校验密码", "成功", success_timeout);
                }
            }, function () {
                toaster.pop("error", "校验密码请求", "失败", error_timeout);
            });
        };

        $scope.updatePassword = function () {
            var data = {
                userId: $stateParams.userId,
                oldPassword: cryptoService.aesEncrypt($scope.oldPassword),
                newPassword: cryptoService.aesEncrypt($scope.newPassword)
            };
            userService.updatePassword(data).then(function (response) {
                if (!responseHandler.isSuccess(response)) {
                    toaster.pop("error", "修改密码", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                } else {
                    toaster.pop("success", "修改密码", "成功", success_timeout);
                }
            }, function () {
                toaster.pop("error", "修改密码请求", "失败", error_timeout);
            });
        };

    }
]);