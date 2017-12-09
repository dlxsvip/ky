App.controller('keywordRecognitionCtrl', ['$scope', '$location', '$http', '$rootScope' , '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal','urlService',
    function($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, urlService) {
        'use strict';

        $scope.selected = {
            'name': ['关键词', '视频名称', '地址'],
            'selected': "关键词",
            "searchValue":""
        };
        $scope.keywordRecognition = {};
        $scope.keywordRecognition.tableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {
                var filterParams = makeFilterParams();
                var keywordRecognitionBlockUI = blockUI.instances.get('keywordRecognitionBlockUI');
                keywordRecognitionBlockUI.start();
                $scope.isLoading = true;
                var getUrl = urlService.getFullUrl('/keywordAnalysis/queryByPage?&keywordName='  + filterParams.keywordName + '&videoName=' + filterParams.videoName+ '&hostName=' + filterParams.hostName);

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
                        keywordRecognitionBlockUI.stop();
                    }, function () {
                        keywordRecognitionBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + data.errorMsg, error_timeout);
                    });
            }
        })

        $scope.searchDetail = function () {
            $scope.keywordRecognition.tableParams.page(1);
            $scope.keywordRecognition.tableParams.reload();
        };


        $scope.testEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.keywordRecognition.tableParams.page(1);
                $scope.keywordRecognition.tableParams.reload();
            }
        }


        $scope.allsetlist = {allset:false};
        $scope.setAllSelected = function() {
            $scope.keywordRecognition.tableParams.data.forEach(function (value, index) {
                value.selected = $scope.allsetlist.allset;
            });

            if ($scope.allsetlist.allset) {
                $scope.sumChose = $scope.keywordRecognition.tableParams.data.length;
            } else {
                $scope.sumChose = 0;
            }
        };

        $scope.sumChose = 0;
        $scope.choseOne = function(choseIndex) {
            $scope.keywordRecognition.tableParams.data.forEach(function (value, index) {
                if (choseIndex == index) {
                    if (value.selected == true) {
                        $scope.sumChose = $scope.sumChose + 1;
                        if ($scope.sumChose == $scope.keywordRecognition.tableParams.data.length) {
                            $scope.allsetlist.allset = true;
                        }
                    } else {
                        $scope.sumChose = $scope.sumChose - 1;
                        if ($scope.sumChose < $scope.keywordRecognition.tableParams.data.length) {
                            $scope.allsetlist.allset = false;
                        }
                    }
                }
            });
        };

        function makeFilterParams() {
            var filterParams = {
                keywordName: "",
                videoName: "",
                hostName: ""
            };
            if ($scope.selected.selected == '关键词') {
                filterParams.keywordName = encodeURIComponent($scope.selected.searchValue);
            } else if ($scope.selected.selected == '视频名称') {
                filterParams.videoName = $scope.selected.searchValue;
            } else if ($scope.selected.selected == '地址') {
                filterParams.hostName = $scope.selected.searchValue;
            }

            return filterParams;
        }

        $scope.deleteKeywordRecognitionItemOpen = function (instanceId, regionId) {
            var modalInstance = $modal.open({
                templateUrl: 'KeywordRecognitionItem',
                controller: deleteKeywordRecognitionItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.keywordRecognition.tableParams;

            modalInstance.startData = {
                id: instanceId,
                regionId: regionId
            };
        };

        var deleteKeywordRecognitionItemCtrl = function ($scope, $modalInstance) {
            $scope.deleteItem = function () {

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

        $scope.exportKeywordInfos = function () {
            var ids = [];
            for (var i = 0; i <= $scope.keywordRecognition.tableParams.data.length - 1; i++) {
                if ($scope.keywordRecognition.tableParams.data[i].selected) {
                    ids.push($scope.keywordRecognition.tableParams.data[i].analysisId);
                }
            }

            var url = urlService.getFullUrl('/keywordAnalysis/exportKeywordInfos');
            if (navigator.userAgent.indexOf("Firefox") > -1) {
                window.open(url + "?analysisIds=" + ids);
            } else {
                window.location.href = url + "?analysisIds=" + ids;
            }

            $scope.cancelSelect();
        };

        $scope.cancelSelect = function () {
            $scope.keywordRecognition.tableParams.data.forEach(function (value) {
                value.selected = false;
            });
            $scope.allsetlist.allset = false;
        }


    }
]);