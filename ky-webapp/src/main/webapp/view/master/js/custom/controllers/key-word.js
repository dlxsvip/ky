App.controller('keywordManageCtrl', ['$scope', '$location', '$http', '$rootScope', '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal', '$stateParams', 'urlService',
    function ($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, $stateParams, urlService) {
        'use strict';

        $scope.selected = {
            'name': ['名称', '状态'],
            'selected': "名称",
            'searchValue': ""
        };
        $scope.keywordStatus = {
            'status': ['全部','启用','停止'],
            'selected': "全部"
        };

        $scope.status = false;
        $scope.name = true;

        $scope.changeType = function() {
            if($scope.selected.selected == "状态") {
                $scope.status = true;
                $scope.name = false;
            } else {
                $scope.status = false;
                $scope.name = true;
            }
        };

        $scope.isEdit = false;

        $scope.keywordManage = {};
        $scope.keywordManage.tableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {

                var keywordManageBlockUI = blockUI.instances.get('keywordManageBlockUI');
                keywordManageBlockUI.start();
                $scope.isLoading = true;
                var getUrl = urlService.getFullUrl('/keyword/queryByPage?keywordName=' + $scope.selected.searchValue);

                var data = {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: 'createTime',
                    orderAsc: false
                };

                $http.get(getUrl, {"params": data})
                    .then(function (response) {
                        $defer.resolve(response.data.data.result);
                        params.total(response.data.data.totalRows);
                        $scope.isLoading = false;
                        keywordManageBlockUI.stop();
                    }, function () {
                        keywordManageBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + response.data.errorMsg, error_timeout);
                    });
            }
        });

        $scope.searchDetail = function () {
            $scope.keywordManage.tableParams.page(1);
            $scope.keywordManage.tableParams.reload();
        };


        $scope.testEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.keywordManage.tableParams.page(1);
                $scope.keywordManage.tableParams.reload();
            }
        };


        $scope.allsetlist = {allset:false};
        $scope.setAllSelected = function() {
            $scope.keywordManage.tableParams.data.forEach(function (value, index) {
                value.selected = $scope.allsetlist.allset;
            });

            if ($scope.allsetlist.allset) {
                $scope.sumChose = $scope.keywordManage.tableParams.data.length;
            } else {
                $scope.sumChose = 0;
            }
        };

        $scope.sumChose = 0;
        $scope.choseOne = function(choseIndex) {
            $scope.keywordManage.tableParams.data.forEach(function (value, index) {
                if (choseIndex == index) {
                    if (value.selected == true) {
                        $scope.sumChose = $scope.sumChose + 1;
                        if ($scope.sumChose == $scope.keywordManage.tableParams.data.length) {
                            $scope.allsetlist.allset = true;
                        }
                    } else {
                        $scope.sumChose = $scope.sumChose - 1;
                        if ($scope.sumChose < $scope.keywordManage.tableParams.data.length) {
                            $scope.allsetlist.allset = false;
                        }
                    }
                }
            });
        };


        $scope.startKeywordManageItemOpen = function (instanceId, regionId) {
            var modalInstance = $modal.open({
                templateUrl: 'keywordStartItem',
                controller: KeywordManageStartCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.keywordManage.tableParams;

            modalInstance.startData = {
                id: instanceId,
                regionId: regionId
            };
        };

        var KeywordManageStartCtrl = function ($scope, $modalInstance) {
            $scope.startItem = function () {
                var keywordStartBlock = blockUI.instances.get('keywordStartBlock');
                keywordStartBlock.start();
                var reqUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/manage/control/cloudEcs/use/StartEcsInstance";

                $http.post(reqUrl, '', {
                    params: {
                        instanceId: $modalInstance.startData.id,
                        regionId: $modalInstance.startData.regionId
                    }
                }).success(function (data) {
                    keywordStartBlock.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '启用', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '启用', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    keywordStartBlock.stop();
                    toaster.pop('error', '启用', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

        $scope.stopKeywordManageItemOpen = function (instanceId, regionId) {
            var modalInstance = $modal.open({
                templateUrl: 'keywordStopItem',
                controller: KeywordManageStopCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.keywordManage.tableParams;

            modalInstance.startData = {
                id: instanceId,
                regionId: regionId
            };
        };

        var KeywordManageStopCtrl = function ($scope, $modalInstance) {
            $scope.stopItem = function () {
                var keywordStopBlock = blockUI.instances.get('keywordStopBlock');
                keywordStopBlock.start();
                var reqUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/manage/control/cloudEcs/use/StartEcsInstance";

                $http.post(reqUrl, '', {
                    params: {
                        instanceId: $modalInstance.startData.id,
                        regionId: $modalInstance.startData.regionId
                    }
                }).success(function (data) {
                    keywordStopBlock.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '停止', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '停止', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    keywordStopBlock.stop();
                    toaster.pop('error', '停止', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }


        // 新增
        $scope.addKeyword = function () {
            var modalInstance = $modal.open({
                templateUrl: 'keywordDialog',
                controller: keywordAddItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.keywordManage.tableParams;

        };

        var keywordAddItemCtrl = function ($scope, $modalInstance) {
            $scope.isEdit = false;
            $scope.title = "添加";
            $scope.checkResult = false;

            $scope.hide = function(){
                $scope.checkResult = true;
            }


            // 重命名检查
            $scope.checkName = function(name){
                var reqUrl = urlService.getFullUrl('/keyword/queryName?name=' + name);
                $http.get(reqUrl)
                    .success(function (data) {
                        if ("success" === data.result) {
                            if(data.data === "不重名"){
                                $scope.checkResult = true;
                            }
                        } else {
                            $scope.checkResult = false;
                        }

                    }).error(function (data) {
                        toaster.pop('error', '查询', '失败： ' + data.errorMsg, error_timeout);
                    });
            }

            $scope.keywordInfo = {};

            $scope.submitForm = function () {
                var keywordAddBlock = blockUI.instances.get('keywordAddBlock');
                keywordAddBlock.start();
                var reqUrl = urlService.getFullUrl('/keyword/create');
                // 发送 json 体的 POST 请求
                $http.post(reqUrl, $scope.keywordInfo).success(function (data) {
                    keywordAddBlock.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '添加', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '添加', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');
                }).error(function (data) {
                    keywordAddBlock.stop();
                    toaster.pop('error', '添加', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        // 修改
        $scope.editKeyword = function (item) {
            var modalInstance = $modal.open({
                templateUrl: 'keywordDialog',
                controller: keywordEditItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.keywordManage.tableParams;
            modalInstance.item = item;
        };

        var keywordEditItemCtrl = function($scope, $modalInstance) {
            $scope.isEdit = true;
            $scope.title = "修改";

            $scope.keywordInfo = {
                keywordId:$modalInstance.item.keywordId,
                keywordName:$modalInstance.item.keywordName,
                description:$modalInstance.item.description,
                label:$modalInstance.item.label
            };

            $scope.submitForm = function () {
                var keywordAddBlock = blockUI.instances.get('keywordAddBlock');
                keywordAddBlock.start();
                var reqUrl = urlService.getFullUrl('/keyword/update');
                // 发送 json 体的 POST 请求
                $http.post(reqUrl, $scope.keywordInfo).success(function (data) {
                    keywordAddBlock.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '修改', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    keywordAddBlock.stop();
                    toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

        // 删除
        $scope.deleteKeyword = function (row) {
            var modalInstance = $modal.open({
                templateUrl: 'keywordDeleteItem',
                controller: keywordDeleteItemCtrl,
                backdrop: false,
                size: ''
            });

            // 父控制层 的表格 传递给 子控制层
            modalInstance.tableParams = $scope.keywordManage.tableParams;
            modalInstance.row = row;
        };

        var keywordDeleteItemCtrl = function ($scope, $modalInstance) {
            $scope.deleteItem = function () {
                var keywordDeleteBlock = blockUI.instances.get('keywordDeleteBlock');
                keywordDeleteBlock.start();
                var reqUrl = urlService.getFullUrl('/keyword/delete');

                var data = {keywordId: $modalInstance.row.keywordId};

                // 发送 url 参数的 POST 请求
                $http.post(reqUrl, '', {params: data})
                    .success(function (data) {
                        keywordDeleteBlock.stop();
                        if ("success" != data.result) {
                            toaster.pop('error', '删除', '失败： ' + data.errorMsg, error_timeout);
                        } else {
                            toaster.pop('success', '删除', '成功', success_timeout);
                            $modalInstance.tableParams.reload();
                        }

                        $modalInstance.close('closed');

                    }).error(function (data) {
                        keywordDeleteBlock.stop();
                        toaster.pop('error', '删除', '失败： ' + data.errorMsg, error_timeout);
                    });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

        // 查看详情
        $scope.keywordDetailOpen = function (row) {
            var modalInstance = $modal.open({
                templateUrl: 'keywordDetailDialog',
                controller: keywordDetailItemCtrl,
                backdrop: false,
                size: ''
            });

            // 父控制层 的表格 传递给 子控制层
            modalInstance.row = row;
        };

        var keywordDetailItemCtrl = function ($scope, $modalInstance) {

            var reqUrl = urlService.getFullUrl('/keyword/query?keywordId=' + $modalInstance.row.keywordId);

            var keywordDetailBlockUI = blockUI.instances.get('keywordDetailBlockUI');
            keywordDetailBlockUI.start();
                // 发送 url 参数的 POST 请求
            $http.get(reqUrl)
                .success(function (data) {
                    keywordDetailBlockUI.stop();
                    if ("success" === data.result) {
                        $scope.keywordInfo = data.data;
                    } else {
                        toaster.pop('error', '查询', '失败： ' + data.errorMsg, error_timeout);
                    }

                }).error(function (data) {
                    keywordDetailBlockUI.stop();
                    toaster.pop('error', '查询', '失败： ' + data.errorMsg, error_timeout);
                });

            $scope.isEdit = true;
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

    }]);