App.controller('keyPersonRecognitionCtrl', ['$scope', '$location', '$http', '$rootScope', '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal', 'urlService',
    function ($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, urlService) {
        'use strict';

        $scope.selected = {
            'name': ['人物名称', '视频名称', '地址'],
            'selected': "人物名称",
            searchValue:""
        };
        $scope.faceRecognition = {};
        $scope.faceRecognition.tableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {
                var filterParams = makeFilterParams();
                var faceRecognitionBlockUI = blockUI.instances.get('faceRecognitionBlockUI');
                faceRecognitionBlockUI.start();
                $scope.isLoading = true;
                var getUrl = urlService.getFullUrl('/keyPersonAnalysis/queryByPage?&personName=' + filterParams.personName + '&videoName=' + filterParams.videoName + '&hostName=' + filterParams.hostName);
                var data = {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: '',
                    orderAsc: false
                };

                $http.get(getUrl, {"params": data})
                    .then(function (response) {
                        var data = response.data.data;
                        $defer.resolve(data.result);
                        params.total(data.totalRows);
                        $scope.isLoading = false;

                        faceRecognitionBlockUI.stop();
                    }, function () {
                        faceRecognitionBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + response.data.errorMsg, error_timeout);
                    });
            }
        });

        $scope.searchDetail = function () {
            $scope.faceRecognition.tableParams.page(1);
            $scope.faceRecognition.tableParams.reload();
        };


        $scope.testEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.faceRecognition.tableParams.page(1);
                $scope.faceRecognition.tableParams.reload();
            }
        }

        $scope.allsetlist = {allset:false};
        $scope.setAllSelected = function() {
            $scope.faceRecognition.tableParams.data.forEach(function (value, index) {
                value.selected = $scope.allsetlist.allset;
            });

            if ($scope.allsetlist.allset) {
                $scope.sumChose = $scope.faceRecognition.tableParams.data.length;
            } else {
                $scope.sumChose = 0;
            }
        };

        $scope.sumChose = 0;
        $scope.choseOne = function(choseIndex) {
            $scope.faceRecognition.tableParams.data.forEach(function (value, index) {
                if (choseIndex == index) {
                    if (value.selected == true) {
                        $scope.sumChose = $scope.sumChose + 1;
                        if ($scope.sumChose == $scope.faceRecognition.tableParams.data.length) {
                            $scope.allsetlist.allset = true;
                        }
                    } else {
                        $scope.sumChose = $scope.sumChose - 1;
                        if ($scope.sumChose < $scope.faceRecognition.tableParams.data.length) {
                            $scope.allsetlist.allset = false;
                        }
                    }
                }
            });
        };


        //$scope.deleteKeyPersonRecognitionItemOpen = function (row) {
        //    var modalInstance = $modal.open({
        //        templateUrl: 'faceRecognitionItem',
        //        controller: faceRecognitionModalCtrl,
        //        backdrop: false,
        //        size: ''
        //    });
        //
        //    modalInstance.tableParams = $scope.faceRecognition.tableParams;
        //
        //    modalInstance.item = item
        //};
        //
        //var faceRecognitionModalCtrl = function ($scope, $modalInstance) {
        //    $scope.deleteItem = function () {
        //        var faceRecognitionBlock = blockUI.instances.get('faceRecognitionBlock');
        //        faceRecognitionBlock.start();
        //        var reqUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/manage/control/cloudEcs/use/StartEcsInstance";
        //
        //        $http.post(reqUrl, '', {
        //            params: {
        //                instanceId: $modalInstance.startData.id,
        //                regionId: $modalInstance.startData.regionId
        //            }
        //        }).success(function (data) {
        //            faceRecognitionBlock.stop();
        //            if ("success" != data.result) {
        //                toaster.pop('error', '删除视频', '失败： ' + data.errorMsg, error_timeout);
        //            } else {
        //                toaster.pop('success', '删除视频', '成功', success_timeout);
        //                $modalInstance.tableParams.reload();
        //            }
        //
        //            $modalInstance.close('closed');
        //
        //        }).error(function (data) {
        //            faceRecognitionBlock.stop();
        //            toaster.pop('error', '删除视频', '失败： ' + data.errorMsg, error_timeout);
        //        });
        //
        //    };
        //    $scope.cancel = function () {
        //        $modalInstance.dismiss('cancel');
        //    };
        //}

        // 查看详情
        $scope.detailLabelItemOpen = function (row) {
            var modalInstance = $modal.open({
                templateUrl: 'labelDetailDialog',
                controller: labelDetailItemCtrl,
                backdrop: false,
                size: ''
            });

            // 父控制层 的表格 传递给 子控制层
            modalInstance.row = row;
        };

        var labelDetailItemCtrl = function ($scope, $modalInstance) {

            var reqUrl = urlService.getFullUrl('/keyPerson/query?personId=' + $modalInstance.row.personId);

            var labelDetailBlockUI = blockUI.instances.get('labelDetailBlockUI');
            labelDetailBlockUI.start();
            // 发送 url 参数的 POST 请求
            $http.get(reqUrl)
                .success(function (data) {
                    labelDetailBlockUI.stop();
                    if ("success" === data.result) {
                        $scope.keyPersonInfo = data.data;
                        $scope.keyPersonInfo.hostName = $modalInstance.row.hostName;
                        $scope.keyPersonInfo.channelName = $modalInstance.row.channelName;
                    } else {
                        toaster.pop('error', '查询', '失败：'+ data.errorMsg, success_timeout);
                    }

                }).error(function (data) {
                    labelDetailBlockUI.stop();
                    toaster.pop('error', '查询', '失败： ' + data.errorMsg, error_timeout);
                });

            $scope.editable = false;
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }




        function makeFilterParams() {
            var filterParams = {
                personName: "",
                videoName: "",
                hostName: ""
            };
            if ($scope.selected.selected == '人物名称') {
                filterParams.personName = encodeURIComponent($scope.selected.searchValue);
            } else if ($scope.selected.selected == '视频名称') {
                filterParams.videoName = $scope.selected.searchValue;
            } else if ($scope.selected.selected == '地址') {
                filterParams.hostName = $scope.selected.searchValue;
            }

            return filterParams;
        }

        $scope.exportKeyPersonInfos = function () {
            var ids = [];
            for (var i = 0; i <= $scope.faceRecognition.tableParams.data.length - 1; i++) {
                if ($scope.faceRecognition.tableParams.data[i].selected) {
                    ids.push($scope.faceRecognition.tableParams.data[i].analysisId);
                }
            }

            var url = urlService.getFullUrl('/keyPersonAnalysis/exportKeyPersonInfos');
            if (navigator.userAgent.indexOf("Firefox") > -1) {
                window.open(url + "?analysisIds=" + ids);
            } else {
                window.location.href = url + "?analysisIds=" + ids;
            }

            $scope.cancelSelect();
        };

        $scope.cancelSelect = function () {
            $scope.faceRecognition.tableParams.data.forEach(function (value) {
                value.selected = false;
            });
            $scope.allsetlist.allset = false;
        }
    }
]);