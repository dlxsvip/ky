App.controller('hostManageCtrl', ['$scope', '$location', '$http', '$rootScope' , '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal','$stateParams',
    function($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, $stateParams) {
        'use strict';

        $scope.selected = {
            'name': ['主机名称', '主机地址'],
            'selected': "主机名称",
            'searchValue': ""
        };

        $scope.hostManage = {};
        $scope.hostManage.tableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {

                var hostManageBlockUI = blockUI.instances.get('hostManageBlockUI');
                hostManageBlockUI.start();
                $scope.isLoading = true;
                var getUrl = 'server/hostManage.json';

                $http.get(getUrl)
                    .then(function (response) {
                        var data = response.data.data;
                        if (data && data.result === 'success') {
                            $defer.resolve(data.rows);
                            params.total(data.totalRows);
                        } else {
                            $scope.isLoading = false;
                        }
                        hostManageBlockUI.stop();
                    }, function () {
                        hostManageBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + data.errorMsg, error_timeout);
                    });
            }
        })

        $scope.searchDetail = function () {
            $scope.hostManage.tableParams.page(1);
            $scope.hostManage.tableParams.reload();
        };


        $scope.testEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.hostManage.tableParams.page(1);
                $scope.hostManage.tableParams.reload();
            }
        }


        $scope.allsetlist = {allset:false};
        $scope.setAllSelected = function() {
            $scope.hostManage.tableParams.data.forEach(function (value, index) {
                value.selected = $scope.allsetlist.allset;
            });

            if ($scope.allsetlist.allset) {
                $scope.sumChose = $scope.hostManage.tableParams.data.length;
            } else {
                $scope.sumChose = 0;
            }
        };

        $scope.sumChose = 0;
        $scope.choseOne = function(choseIndex) {
            $scope.hostManage.tableParams.data.forEach(function (value, index) {
                if (choseIndex == index) {
                    if (value.selected == true) {
                        $scope.sumChose = $scope.sumChose + 1;
                        if ($scope.sumChose == $scope.hostManage.tableParams.data.length) {
                            $scope.allsetlist.allset = true;
                        }
                    } else {
                        $scope.sumChose = $scope.sumChose - 1;
                        if ($scope.sumChose < $scope.hostManage.tableParams.data.length) {
                            $scope.allsetlist.allset = false;
                        }
                    }
                }
            });
        };

        $scope.deleteKeywordRecognitionItemOpen = function (instanceId, regionId) {
            var modalInstance = $modal.open({
                templateUrl: 'KeywordRecognitionItem',
                controller: KeywordRecognitionItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.hostManage.tableParams;

            modalInstance.startData = {
                id: instanceId,
                regionId: regionId
            };
        };

        var KeywordRecognitionItemCtrl = function ($scope, $modalInstance) {
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