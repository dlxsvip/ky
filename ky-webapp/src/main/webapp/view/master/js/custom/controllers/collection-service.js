App.controller('collectionServiceCtrl', ['$scope', '$location', '$http', '$rootScope', '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal', '$stateParams', 'urlService',
    function ($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, $stateParams, urlService) {
        'use strict';

        $scope.selected = {
            'name': ['节点名称', '节点地址'],
            'selected': "节点名称",
            'searchValue': ""
        };

        $scope.collectionService = {};
        $scope.collectionService.tableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {
                var filterParams = makeFilterParams();
                var collectionServiceBlockUI = blockUI.instances.get('collectionServiceBlockUI');
                collectionServiceBlockUI.start();
                $scope.isLoading = true;
                var getUrl = urlService.getFullUrl('/captureHost/queryByPage?&hostName=' + filterParams.hostName + '&ip=' + filterParams.ip);

                var data = {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: '',
                    orderAsc: false
                };
                $http.get(getUrl, {"params": data})
                    .then(function (response) {
                        var data = response.data.data;
                        console.log(data);
                        $defer.resolve(data.result);
                        params.total(data.totalRows);
                        $scope.isLoading = false;

                        collectionServiceBlockUI.stop();
                    }, function () {
                        collectionServiceBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + response.data.errorMsg, error_timeout);
                    });
            }
        });

        $scope.searchDetail = function () {
            $scope.collectionService.tableParams.page(1);
            $scope.collectionService.tableParams.reload();
        };


        $scope.testEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.collectionService.tableParams.page(1);
                $scope.collectionService.tableParams.reload();
            }
        }


        $scope.allsetlist = {allset:false};
        $scope.setAllSelected = function() {
            $scope.collectionService.tableParams.data.forEach(function (value, index) {
                value.selected = $scope.allsetlist.allset;
            });

            if ($scope.allsetlist.allset) {
                $scope.sumChose = $scope.collectionService.tableParams.data.length;
            } else {
                $scope.sumChose = 0;
            }
        };

        $scope.sumChose = 0;
        $scope.choseOne = function(choseIndex) {
            $scope.collectionService.tableParams.data.forEach(function (value, index) {
                if (choseIndex == index) {
                    if (value.selected == true) {
                        $scope.sumChose = $scope.sumChose + 1;
                        if ($scope.sumChose == $scope.collectionService.tableParams.data.length) {
                            $scope.allsetlist.allset = true;
                        }
                    } else {
                        $scope.sumChose = $scope.sumChose - 1;
                        if ($scope.sumChose < $scope.collectionService.tableParams.data.length) {
                            $scope.allsetlist.allset = false;
                        }
                    }
                }
            });
        };

        $scope.addDialogOpen = function () {
            var modalInstance = $modal.open({
                templateUrl: 'dialogItem',
                controller: addItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.collectionService.tableParams;
        };

        var addItemCtrl = function ($scope, $modalInstance) {
            $scope.isEdit = false;
            $scope.title = "添加";
            $scope.Info = {};

            $scope.ok = function () {
                var myBlockUI = blockUI.instances.get('myBlockUI');
                myBlockUI.start();
                var reqUrl = urlService.getFullUrl('/captureHost/create');
                // 发送 json 体的 POST 请求
                $http.post(reqUrl, $scope.Info).success(function (data) {
                    myBlockUI.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '添加', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '添加', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    myBlockUI.stop();
                    toaster.pop('error', '添加', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        $scope.modifyDialogOpen = function (item) {
            var modalInstance = $modal.open({
                templateUrl: 'dialogItem',
                controller: modifyItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.collectionService.tableParams;

            modalInstance.item = item;
        };

        var modifyItemCtrl = function ($scope, $modalInstance) {
            $scope.isEdit = true;
            $scope.title = "修改";

            $scope.Info = {
                hostId:$modalInstance.item.hostId,
                hostName:$modalInstance.item.hostName,
                ip:$modalInstance.item.ip,
                description:$modalInstance.item.description
            };

            $scope.ok = function () {
                var myBlockUI = blockUI.instances.get('myBlockUI');
                myBlockUI.start();
                var reqUrl = urlService.getFullUrl('/captureHost/update');
                // 发送 json 体的 POST 请求
                $http.post(reqUrl, $scope.Info).success(function (data) {
                    myBlockUI.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '修改', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    myBlockUI.stop();
                    toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };


        $scope.deleteDialogOpen = function(item) {
            var modalInstance = $modal.open({
                templateUrl: 'deleteItem',
                controller: deleteItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.collectionService.tableParams;
            modalInstance.item = item

        };

        var deleteItemCtrl = function ($scope, $modalInstance,$http) {

            $scope.Delete = function () {
                var myBlockUI = blockUI.instances.get('deleteUI');
                myBlockUI.start();
                var reqUrl = urlService.getFullUrl('/captureHost/delete');

                var data = {hostId: $modalInstance.item.hostId};

                // 发送 url 参数的 POST 请求
                $http.post(reqUrl, '', {params: data})
                    .success(function (data) {
                        myBlockUI.stop();
                        if ("success" != data.result) {
                            toaster.pop('error', '删除', '失败： ' + data.errorMsg, error_timeout);
                        } else {
                            toaster.pop('success', '删除', '成功', success_timeout);
                            $modalInstance.tableParams.reload();
                        }

                        $modalInstance.close('closed');

                    }).error(function (data) {
                        myBlockUI.stop();
                        toaster.pop('error', '删除', '失败： ' + data.errorMsg, error_timeout);
                    });

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };


        function makeFilterParams() {
            var filterParams = {
                hostName: "",
                ip: ""
            };
            if ($scope.selected.selected == '节点名称') {
                filterParams.hostName = encodeURIComponent($scope.selected.searchValue);
            } else if ($scope.selected.selected == '节点地址') {
                filterParams.ip = $scope.selected.searchValue;
            }

            return filterParams;
        }
    }]);