App.controller('userInfoResetPwdCtrl', ['$scope', '$rootScope', '$stateParams', '$resource', 'userService', 'responseHandler', 'toaster', 'cryptoService',
    function ($scope, $rootScope, $stateParams, $resource, userService, responseHandler, toaster, cryptoService) {
        'use strict';

        if ($stateParams.userId == null) {
            return;
        }

        $scope.nickName = $stateParams.nickName;

        $scope.isCheckFail = true;

        $scope.checkPassword = function (currentPassword) {
            var data = {
                currentPassword: cryptoService.aesEncrypt(currentPassword)
            };
            userService.checkPassword(data).then(function (response) {
                if (!responseHandler.isSuccess(response)) {
                    $scope.isCheckFail = true;
                    toaster.pop("error", "校验密码", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                } else {
                    $scope.isCheckFail = false;
                    toaster.pop("success", "校验密码", "成功", success_timeout);
                }
            }, function () {
                $scope.isCheckFail = true;
                toaster.pop("error", "校验密码请求", "失败", error_timeout);
            });
        };

        $scope.resetPassword = function () {
            var data = {
                userId: $stateParams.userId,                            // 被重置的用户id
                newPassword: cryptoService.aesEncrypt($scope.newPassword)  // 被重置的用户新密码
            };
            userService.resetPassword(data).then(function (response) {
                if (!responseHandler.isSuccess(response)) {
                    toaster.pop("error", "修改密码", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                } else {
                    toaster.pop("success", "修改密码", "成功", success_timeout);
                    $scope.newPassword = "";
                }
            }, function () {
                toaster.pop("error", "修改密码请求", "失败", error_timeout);
            });
        };

    }
]);