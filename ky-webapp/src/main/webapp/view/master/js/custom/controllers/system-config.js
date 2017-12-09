App.controller('SystemConfigController', ['$scope', '$location', '$http', '$rootScope', '$resource', '$modal', '$state', 'ngTableParams', 'blockUI', 'toaster', 'urlService', 'httpService',
    function ($scope, $location, $http, $rootScope, $resource, $modal, $state, ngTableParams, blockUI, toaster, urlService, httpService) {
        'use strict';
        $scope.locationPath = $location.path();
        // about tab
        $scope.tabList = [
            {CN: "OSS配置", name: "OSS", route: "app.system-config.store", active: true},
            {CN: "参数配置", name: "parameter", route: "app.system-config.parameter", active: false},
            {CN: "通知配置", name: "notice", route: "app.system-config.notice", active: false}
        ];

        $scope.setActiveTab = function (tab) {
            $scope.tabList.forEach(function (t) {
                t.active = false;
            });

            tab.active = true;
            $state.go($scope.tabList[0].route);
            if (angular.isDefined(tab.params)) {
                $state.go(tab.route, tab.params);
            }
            else {
                $state.go(tab.route);
            }
        };

        $scope.active = function (tab) {
            var activeValue = false;
            if ($state.includes(tab.route)) {
                activeValue = true;
            }
            return activeValue;
        };

        $scope.$on("$stateChangeSuccess", function () {
            if ($state.is('app.system-config')) {
                var activeTab = {};
                $scope.tabList.forEach(function (t) {
                    if (t.active) {
                        activeTab = t;
                    }
                });
                $scope.setActiveTab(activeTab);
            }
            $scope.tabList.forEach(function (tab) {
                tab.active = $scope.active(tab);
            });
        });


        //OSS-config
        $scope.bucket = {
            endPoint: "",
            bucketName: '',
            accesskeyID: '',
            accesskeyKey: ''
        };

        // get OSS-config
        var getOssConfig = function () {
            var bucketConfigBlock = blockUI.instances.get("bucketConfig");
            bucketConfigBlock.start("bucket配置加载中...");
            var getUrl = urlService.getFullUrl('/systemConfig/getOSSConfig');
            $http.get(getUrl).success(function (data) {
                if (data.result == "success") {
                    $scope.bucket.endPoint = data.data.endPoint;
                    $scope.bucket.bucketName = data.data.bucketName;
                    $scope.bucket.accesskeyID = data.data.accesskeyID;
                    $scope.bucket.accesskeyKey = data.data.accesskeyKey;
                    bucketConfigBlock.stop();
                } else {
                    toaster.pop('error', data.errorMsg, data.errorDetail, error_timeout);
                    bucketConfigBlock.stop();
                }
            }).error(function (data) {
                toaster.pop('error', '获取bucket配置', '失败', error_timeout);
                bucketConfigBlock.stop();
            })
        };


        // set OSS-config
        $scope.ossConfig = function () {
            var modalInstance = $modal.open({
                templateUrl: 'SystemBucketConfig',
                controller: SystemBucketConfigCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.endPoint = $scope.bucket.endPoint;
            modalInstance.bucketName = $scope.bucket.bucketName;
            modalInstance.accesskeyID = $scope.bucket.accesskeyID;
            modalInstance.accesskeyKey = $scope.bucket.accesskeyKey;

            modalInstance.result.then(function (newImage) {
                getOssConfig();
            }, function (reson) {
            });
        };
        var SystemBucketConfigCtrl = function ($scope, $modalInstance, $http) {

            $scope.newBucket = {
                endPoint: $modalInstance.endPoint,
                bucketName: $modalInstance.bucketName,
                accesskeyID: $modalInstance.accesskeyID,
                accesskeyKey: $modalInstance.accesskeyKey
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.submitForm = function () {

                var bucketConfigSet = blockUI.instances.get("bucketConfigSet");
                bucketConfigSet.start("bucket配置中...");
                var postData = {
                    params: {
                        endPoint: $scope.newBucket.endPoint,
                        bucketName: $scope.newBucket.bucketName,
                        accesskeyID: $scope.newBucket.accesskeyID,
                        accesskeyKey: $scope.newBucket.accesskeyKey
                    }
                };

                var postUrl = urlService.getFullUrl('/systemConfig/setOssConfig');

                $http.post(postUrl, '', postData).success(function (data) {
                    if (data.result == "success") {
                        toaster.pop('success', '配置bucket', '成功', success_timeout);
                        bucketConfigSet.stop();
                        $modalInstance.close('closed');
                    } else {
                        toaster.pop('error', "配置bucket失败", data.errorDetail, error_timeout);
                        bucketConfigSet.stop();
                    }
                }).error(function (data) {
                    toaster.pop('error', '配置bucket', '失败', error_timeout);
                    bucketConfigSet.stop();
                    $modalInstance.close('closed');
                })
            }
        }


        //clean up config
        $scope.cleanConfig = function (type) {
            var modalInstance = $modal.open({
                templateUrl: 'clearBucketConfig',
                controller: clearBucketConfigCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.result.then(function () {
                getOssConfig();
            }, function (reson) {
            });
        };

        var clearBucketConfigCtrl = function ($scope, $modalInstance, $http) {
            $scope.clearBucket = function () {
                var clearBucketConfig = blockUI.instances.get("clearBucketConfig");
                clearBucketConfig.start();
                var postUrl = urlService.getFullUrl('/systemConfig/cleanOssConfig');
                $http.post(postUrl, '', {}).success(function (data) {
                    if (data.result == "success") {
                        toaster.pop('success', '清空bucket', '成功', success_timeout);
                    } else {
                        toaster.pop('error', "清空bucket失败", data.errorDetail, error_timeout);
                    }
                    clearBucketConfig.stop();
                    $modalInstance.close('closed');
                }).error(function () {
                    toaster.pop('error', '清空bucket', '失败', error_timeout);
                    $modalInstance.close('closed');
                    clearBucketConfig.stop();
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }


        // about parameter-config,confirm number-configValue is number
        $scope.parameterJson = function (parameter) {
            parameter.forEach(function (item) {
                if (item.valueType == "float" || item.valueType == "int") {
                    item.configValue = parseFloat(item.configValue);
                }
            });
            return parameter;
        };

        $scope.pConf = {};
        var getParameterConfig = function () {
            var parameterConfigBlock = blockUI.instances.get("parameterConfig");
            parameterConfigBlock.start("parameter配置加载中...");
            httpService.postByUrl('/systemConfig/getAllConfig')
                .then(function (response) {
                    if (response.result == "success") {
                        $scope.parameterConfig = $scope.parameterJson(response.data);
                        $scope.pConf = angular.copy($scope.parameterConfig);
                        parameterConfigBlock.stop();
                    }
                }, function (e) {
                    toaster.pop('error', '获取parameter配置', '失败', error_timeout);
                    parameterConfigBlock.stop();
                })
        };


        //parameterConfigCopy ==== pConf
        var parameterHttp = function (url, data, fn) {
            httpService.postByJson(url, data)
                .then(function (response) {
                    if (response.result == "success") {
                        fn();
                    }
                }, function (e) {
                    toaster.pop('error', '修改参数配置', '失败', error_timeout);
                })
        };
        $scope.cancelEditParameter = function (p, index) {
            p.edit = !p.edit;
            $scope.pConf[index].configValue = $scope.parameterConfig[index].configValue;
        };
        $scope.saveEditParameter = function (p) {
            function editSuccess() {
                getParameterConfig();
            };
            if (p.configKey == "aes.key") {
                var regRSA = /^[0-9a-zA-Z]{16}$/;
                var b = regRSA.test(p.configValue);
                if (!b) {
                    toaster.pop('error', '修改失败', '验证密码的秘钥必须为16位，允许字母数字和下划线', error_timeout);
                    return;
                }
            };
            parameterHttp('/systemConfig/updateConfig', p, editSuccess);

        };

        //set parameter-config
        $scope.setParameterConfig = function () {
            var modalInstance = $modal.open({
                templateUrl: 'parameterConfig',
                controller: parameterConfigCtrl,
                backdrop: true,
                size: 'lg'
            });

            modalInstance.pConfModel = angular.copy($scope.parameterConfig);
            modalInstance.parameterHttp = parameterHttp;
            modalInstance.result.then(function (result) {
                console.log('配置成功');
            }, function (reson) {
                console.log('配置失败');
            });
        };
        var parameterConfigCtrl = function ($scope, $modalInstance) {
            $scope.pConfModel = $modalInstance.pConfModel;

            $scope.submitForm = function () {
                function editSuccess() {
                    $modalInstance.close();
                    getParameterConfig();
                };
                $modalInstance.parameterHttp('/systemConfig/updateAllConfig', $scope.pConfModel, editSuccess);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        $scope.mailConfig = {};
        var getMailConfig = function(){
            httpService.postByUrl('/systemConfig/getMailConfig')
                .then(function (response) {
                    if (response.result == "success") {
                        $scope.mailConfig = response.data;
                    }
                }, function (e) {
                    toaster.pop('error', '获取邮件配置', '失败', error_timeout);
                })
        };

        $scope.setMailConfig = function() {
            var modalInstance = $modal.open({
                templateUrl: "app/views/system-config/setMailConfig.html",
                controller: setMailConfigController,
                backdrop: false,
                resolve: "parsley"
            });

            modalInstance.currentConfig = JSON.parse(JSON.stringify($scope.mailConfig));
            if ( modalInstance.currentConfig.length == 0) {
                modalInstance.currentConfig = {};
                modalInstance.currentConfig.smtpPort = 25;
            }
            modalInstance.result.then(function () {
                toaster.pop('success','设置邮件配置','成功',success_timeout);
                getMailConfig();
            }, function () {

            });
        };

        var setMailConfigController = function($scope, $modalInstance) {
            $scope.config = $modalInstance.currentConfig;
            $scope.submitForm = function() {
                var data = $scope.config;
                httpService.postByJson('/systemConfig/setMailConfig',data) .then(function (response) {
                    if (response.result != "success"){
                        toaster.pop('error', '配置邮件', '失败', error_timeout);
                    }
                    $modalInstance.close('closed');
                }, function (e) {
                    toaster.pop('error', '配置邮件', '失败', error_timeout);
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        $scope.clearMailConfig = function() {
            httpService.postByJson('/systemConfig/clearMailConfig',{}) .then(function (response) {
                if (response.result == "success") {
                    $scope.mailConfig = {};
                    toaster.pop('success','清除邮件配置','成功',success_timeout);
                } else {
                    toaster.pop('error', '清除邮件配置失败', response.errorMsg , error_timeout);
                }
            }, function (e) {
                toaster.pop('error', '清除邮件配置失败', response.errorMsg , error_timeout);
            });
        };

        //get data when the tab is active
        if ($scope.locationPath.indexOf('store') > 0) {
            getOssConfig();
        } else if ($scope.locationPath.indexOf('parameter') > 0) {
            getParameterConfig();
        }else if ($scope.locationPath.indexOf('notice') > 0) {
            getMailConfig();
        }


    }]);