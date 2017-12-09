App.controller('videoDataCtrl', ['$scope', '$location', '$http', '$rootScope' , '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal',
    function($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal) {
        'use strict';

        $scope.selected = {
            'name': ['视频名称', '主机地址'],
            'selected': "视频名称",
            "searchValue":""
        };
        $scope.videoData = {};
        $scope.videoData.tableParams = new ngTableParams({
                page: 1,             // show first page
                count: 10           // count per page
        }, {
            getData: function ($defer, params) {

                var videoDataBlockUI = blockUI.instances.get('videoDataBlockUI');
                videoDataBlockUI.start();
                $scope.isLoading = true;
                var getUrl = 'server/vedioData.json';

                $http.get(getUrl)
                    .then(function (response) {
                        var data = response.data.data;
                        if (data && data.result === 'success') {
                            $defer.resolve(data.rows);
                            params.total(data.totalRows);
                        } else {
                            $scope.isLoading = false;
                        }
                        videoDataBlockUI.stop();
                    }, function () {
                        videoDataBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + data.errorMsg, error_timeout);
                    });
            }
        })

        $scope.searchDetail = function () {
            $scope.videoData.tableParams.page(1);
            $scope.videoData.tableParams.reload();
        };


        $scope.testEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.videoData.tableParams.page(1);
                $scope.videoData.tableParams.reload();
            }
        }


        $scope.deleteVideoItem = function (instanceId, regionId) {
            var modalInstance = $modal.open({
                templateUrl: 'deleteVideoItem',
                controller: videoResourceCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.videoData.tableParams;

            modalInstance.startData = {
                id: instanceId,
                regionId: regionId
            };
        };

        var videoResourceCtrl = function ($scope, $modalInstance) {
            $scope.deleteItem = function () {
                var deleteVideoBlock = blockUI.instances.get('deleteVideoBlock');
                deleteVideoBlock.start();
                var reqUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/manage/control/cloudEcs/use/StartEcsInstance";

                $http.post(reqUrl, '', {
                    params: {
                        instanceId: $modalInstance.startData.id,
                        regionId: $modalInstance.startData.regionId
                    }
                }).success(function (data) {
                    deleteVideoBlock.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '删除视频', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '删除视频', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    deleteVideoBlock.stop();
                    toaster.pop('error', '删除视频', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

    }]);