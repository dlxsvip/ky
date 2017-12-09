App.controller('channelManageCtrl', ['$scope', '$location', '$http', '$rootScope' , '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal','$stateParams','urlService',
    function($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, $stateParams, urlService) {
        'use strict';

        $scope.detailTitle = $stateParams.hostName;
        $scope.channelManage = {};
        $scope.channelManage.tableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {

                var channelManageBlockUI = blockUI.instances.get('channelManageBlockUI');
                channelManageBlockUI.start();
                $scope.isLoading = true;
                var getUrl = urlService.getFullUrl('/captureChannel/queryChannelsByHostId?&hostId=' + $stateParams.hostId);


                $http.get(getUrl)
                    .then(function (response) {
                        var data = response.data.data;

                        typeChange(data.result);

                        $defer.resolve(data.result);
                        params.total(data.totalRows);
                        $scope.isLoading = false;
                        channelManageBlockUI.stop();
                    }, function () {
                        channelManageBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + data.errorMsg, error_timeout);
                    });
            }
        });

        function typeChange (data){
            for(var i = 0; i < data.length; i++){
                if('UNCAPTURE' === data[i].captureStatusType){
                    data[i].captureStatusType = "未采集";
                }else if('START' === data[i].captureStatusType){
                    data[i].captureStatusType = "开始采集";
                }else if('PROCESSING' === data[i].captureStatusType){
                    data[i].captureStatusType = "采集中";
                }else if('OVER' === data[i].captureStatusType){
                    data[i].captureStatusType = "采集结束";
                }else if('FAILED' === data[i].captureStatusType){
                    data[i].captureStatusType = "采集失败";
                }
            }
        }



        $scope.beginCollectingItem = function (item) {
            var modalInstance = $modal.open({
                templateUrl: 'beginCollectingItem',
                controller: beginCollectingItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.channelManage.tableParams;
            modalInstance.item = item;

        };

        var beginCollectingItemCtrl = function ($scope, $modalInstance) {
            $scope.tipChannel = $modalInstance.item.channelName;
            $scope.ok = function () {
                var myBlockUI = blockUI.instances.get('beginBlockUI');
                myBlockUI.start();

                var data = {channelId: $modalInstance.item.channelId};

                // 发送 url 参数的 POST 请求
                var reqUrl = urlService.getFullUrl('/captureChannel/start');

                $http.post(reqUrl, '', {params: data})
                    .success(function (data) {
                    myBlockUI.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', "采集", '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', "采集", '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    myBlockUI.stop();
                    toaster.pop('error', "采集", '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        $scope.stopCollectingItem = function (item) {
            var modalInstance = $modal.open({
                templateUrl: 'stopCollectingItem',
                controller: stopCollectingItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.channelManage.tableParams;
            modalInstance.item = item;

        };

        var stopCollectingItemCtrl = function ($scope, $modalInstance) {
            $scope.tipChannel = $modalInstance.item.channelName;
            $scope.ok = function () {
                var myBlockUI = blockUI.instances.get('stopBlockUI');
                myBlockUI.start();
                var data = {channelId: $modalInstance.item.channelId};

                // 发送 url 参数的 POST 请求
                var reqUrl = urlService.getFullUrl('/captureChannel/stop');

                $http.post(reqUrl, '', {params: data})
                    .success(function (data) {
                    myBlockUI.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', "停止采集", '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', "停止采集", '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    myBlockUI.stop();
                    toaster.pop('error', "停止采集", '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

        $scope.deleteCollectingItem = function (item) {
            var modalInstance = $modal.open({
                templateUrl: 'deleteCollectingItem',
                controller: deleteCollectingCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.channelManage.tableParams;

            modalInstance.item = item;
        };

        var deleteCollectingCtrl = function ($scope, $modalInstance) {
            $scope.tipChannel = $modalInstance.item.channelName;
            $scope.delete = function () {
                var deleteBlock = blockUI.instances.get('deleteBlock');
                deleteBlock.start("数据正在加载中...");
                $scope.isLoading = true;
                var data = {channelId: $modalInstance.item.channelId};

                // 发送 url 参数的 POST 请求
                var reqUrl = urlService.getFullUrl('/captureChannel/delete');

                $http.post(reqUrl, '', {params: data})
                    .success(function (data) {
                    deleteBlock.stop();
                    $scope.isLoading = false;
                    if ("success" != data.result) {
                        toaster.pop('error', '删除', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '删除', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }
                    $modalInstance.close('closed');
                }).error(function (data) {
                    deleteBlock.stop();
                    $scope.isLoading = false;
                    toaster.pop('error', '删除', '失败： ' + data.errorMsg, error_timeout);
                });

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };


        // 频道编辑
        $scope.editCollectingItem = function (item) {
            var modalInstance = $modal.open({
                templateUrl: 'editCollectingItem',
                controller: editCollectingCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.channelManage.tableParams;

            modalInstance.item = item;
        };

        var editCollectingCtrl = function ($scope, $modalInstance) {
            $scope.isEdit = true;



            $scope.Info = {
                channelId:$modalInstance.item.channelId,
                channelName:$modalInstance.item.channelName,
                hostId:$modalInstance.item.hostId,
                description:$modalInstance.item.description,
                channelNo:$modalInstance.item.channelNo,
                captureStatusType:$modalInstance.item.captureStatusType
            };

            $scope.ok = function () {
                var editCollectingBlockUI = blockUI.instances.get('editCollectingBlockUI');
                editCollectingBlockUI.start();
                var reqUrl = urlService.getFullUrl('/captureChannel/update');
                // 发送 json 体的 POST 请求

                if('未采集' === $modalInstance.item.captureStatusType){
                    $scope.Info.captureStatusType = "UNCAPTURE";
                }else if('开始采集' === $modalInstance.item.captureStatusType){
                    $scope.Info.captureStatusType = "START";
                }else if('采集中' === $modalInstance.item.captureStatusType){
                    $scope.Info.captureStatusType = "PROCESSING";
                }else if('采集结束' === $modalInstance.item.captureStatusType){
                    $scope.Info.captureStatusType = "OVER";
                }else if('采集失败' === $modalInstance.item.captureStatusType){
                    $scope.Info.captureStatusType = "FAILED";
                }
                $http.post(reqUrl, $scope.Info).success(function (data) {
                    editCollectingBlockUI.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '修改', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    editCollectingBlockUI.stop();
                    toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };
    }]);