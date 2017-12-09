App.controller('keyPersonManageCtrl', ['$scope', '$location', '$http', '$rootScope' , '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal','$stateParams','fileUploadService','$timeout','urlService',
    function($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, $stateParams, fileUploadService, $timeout, urlService) {
        'use strict';

        $scope.selected = {
            'name': ['名称', '状态'],
            'selected': "名称",
            'searchValue': ""
        };
        $scope.keyPersonStatus = {
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

        //上传图片
        var defaultImage = 'app/img/default.png';

        $scope.keyPersonManage = {};
        $scope.keyPersonManage.tableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {

                var keyPersonManageBlockUI = blockUI.instances.get('keyPersonManageBlockUI');
                keyPersonManageBlockUI.start();
                $scope.isLoading = true;
                var getUrl = urlService.getFullUrl('/keyPerson/queryByPage?personName=' + $scope.selected.searchValue);
                var paramsData = {
                    pageNum: params.page(),
                    pageSize: params.count()
                }

                $http.get(getUrl, {"params": paramsData})
                    .then(function (response) {
                        var data = response.data.data;
                        if (data && data.result) {
                            $defer.resolve(data.result);
                            params.total(data.totalRows);
                            $scope.isLoading = false;
                        } else {
                            $scope.isLoading = true;
                        }
                        keyPersonManageBlockUI.stop();
                    }, function () {
                        keyPersonManageBlockUI.stop();
                        $scope.isLoading = false;
                        toaster.pop('error', '失败： ' + data.errorMsg, error_timeout);
                    });
            }
        });

        $scope.searchDetail = function () {
            $scope.keyPersonManage.tableParams.page(1);
            $scope.keyPersonManage.tableParams.reload();
        };


        $scope.testEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.keyPersonManage.tableParams.page(1);
                $scope.keyPersonManage.tableParams.reload();
            }
        };

        $scope.startKeyPersonManageItemOpen = function (instanceId, regionId) {
            var modalInstance = $modal.open({
                templateUrl: 'keyPersonStartItem',
                controller: KeyPersonManageStartCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.keyPersonManage.tableParams;

            modalInstance.startData = {
                id: instanceId,
                regionId: regionId
            };
        };

        var KeyPersonManageStartCtrl = function ($scope, $modalInstance) {
            $scope.startItem = function () {
                var keyPersonStartBlock = blockUI.instances.get('keyPersonStartBlock');
                keyPersonStartBlock.start();
                var reqUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/manage/control/cloudEcs/use/StartEcsInstance";

                $http.post(reqUrl, '', {
                    params: {
                        instanceId: $modalInstance.startData.id,
                        regionId: $modalInstance.startData.regionId
                    }
                }).success(function (data) {
                    keyPersonStartBlock.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '启用', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '启用', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }

                    $modalInstance.close('closed');

                }).error(function (data) {
                    keyPersonStartBlock.stop();
                    toaster.pop('error', '启用', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }


        //addKeyPerson
        $scope.addKeyPerson = function () {
            var modalInstance = $modal.open({
                templateUrl: 'keyPersonDialog',
                controller: keyPersonItemCtrl,
                backdrop: false,
                size: ''
            });

            modalInstance.tableParams = $scope.keyPersonManage.tableParams;

        };

        var keyPersonItemCtrl = function ($scope, $modalInstance) {
            $scope.title = "添加";
            $scope.isEdit = false;

            $scope.hide = function(){
                $scope.checkResult = true;
            };

            // 重命名检查
            $scope.checkName = function(name){
                var reqUrl = urlService.getFullUrl('/keyPerson/queryName?name=' + name);
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
            };

            //定义性别类型
            var sexType = [{type:"MALE", name:"男"}, {type:"FEMALE", name:"女"}];

            $scope.keyPersonInfo = {
                "description": '',
                "personName": '',
                "personImageAddress":defaultImage,
                "label":"",
                "age":"",
                "sex":"",
                "nationality":"",
                "area":"",
                "rank":""
            };
            $scope.data ={};
            //$scope.data.img = defaultImage;
            var images = {};
            $scope.triggerFileInput = function(name) {
                var myElement= document.getElementById(name);
                $timeout(function() {
                    angular.element(myElement).trigger('click');
                });

            };
            $scope.remit = {};
            $scope.remit.readFile = function (imgFile, imgParam) {
                $scope.check = null;
                var img = imgFile;
                var check = fileUploadService.checkUploadImage(img);
                if (check.code !== 0) {
                    toaster.pop('error', check.message, '', error_timeout);
                    $rootScope.$apply();
                    return;
                }

                var formData = new FormData();
                formData.append('file', img);
                formData.append('localDir', 'headPortrait');
                fileUploadService.uploadImage(formData)
                    .then(function(data){
                        if (data.responseCode == "200") {
                            $scope.data[imgParam] = data.data;
                            $scope.keyPersonInfo.personImageAddress =  $scope.data[imgParam];
                            images[imgParam] = true;
                            $scope.picSelected = images['img'];
                        }
                    });
            };


            $scope.submitForm = function () {
                var keyPersonAddBlock = blockUI.instances.get('keyPersonAddBlock');
                keyPersonAddBlock.start();
                var reqUrl = urlService.getFullUrl("keyPerson/create");
                $http.post(reqUrl, $scope.keyPersonInfo).success(function (data) {
                    if ("success" != data.result) {
                        toaster.pop('error', '添加', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '添加', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }
                    keyPersonAddBlock.stop();
                    $modalInstance.close('closed');
                }).error(function (data) {
                    keyPersonAddBlock.stop();
                    toaster.pop('error', '添加', '失败： ' + data.errorMsg, error_timeout);
                });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        // 修改
        $scope.editKeyPerson = function (item) {
            var modalInstance = $modal.open({
                templateUrl: 'keyPersonDialog',
                controller: keyPersonEditItemCtrl,
                backdrop: false,
                size: ''
            });
            modalInstance.tableParams = $scope.keyPersonManage.tableParams;
            //modalInstance.item = $scope.keyPersonManage.tableParams.data[index];
            modalInstance.item = item;

        };

        var keyPersonEditItemCtrl = function ($scope, $modalInstance) {
            $scope.title = "修改";
            $scope.isEdit = true;

            $scope.keyPersonInfo = {
                personId:$modalInstance.item.personId,
                personName:$modalInstance.item.personName,
                description:$modalInstance.item.description,
                personImageAddress:$modalInstance.item.personImageAddress,
                label:$modalInstance.item.label,
                age:$modalInstance.item.age,
                sex:$modalInstance.item.sex,
                nationality:$modalInstance.item.nationality,
                area:$modalInstance.item.area,
                rank:$modalInstance.item.rank
            };


            $scope.submitForm = function(){
                var keyPersonAddBlock = blockUI.instances.get('keyPersonAddBlock');
                keyPersonAddBlock.start();
                var getUrl = urlService.getFullUrl('/keyPerson/update');

                $http.post(getUrl, $scope.keyPersonInfo).success(function (data) {
                    keyPersonAddBlock.stop();
                    if ("success" != data.result) {
                        toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                    } else {
                        toaster.pop('success', '修改', '成功', success_timeout);
                        $modalInstance.tableParams.reload();
                    }
                    $modalInstance.close('closed');

                }).error(function (data) {
                    keyPersonAddBlock.stop();
                    toaster.pop('error', '修改', '失败： ' + data.errorMsg, error_timeout);
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        };

        // 删除
        $scope.deleteKeyPerson = function (row) {
            var modalInstance = $modal.open({
                templateUrl: 'keyPersonDeleteItem',
                controller: keyPersonDeleteItemCtrl,
                backdrop: false,
                size: ''
            });

            // 父控制层 的表格 传递给 子控制层
            modalInstance.tableParams = $scope.keyPersonManage.tableParams;
            modalInstance.row = row;
        };

        var keyPersonDeleteItemCtrl = function ($scope, $modalInstance) {
            $scope.deleteItem = function () {
                var keyPersonDeleteBlock = blockUI.instances.get('keyPersonDeleteBlock');
                keyPersonDeleteBlock.start();
                var reqUrl = urlService.getFullUrl('/keyPerson/delete');

                var data = {personId: $modalInstance.row.personId};

                // 发送 url 参数的 POST 请求
                $http.post(reqUrl, '', {params: data})
                    .success(function (data) {
                        keyPersonDeleteBlock.stop();
                        if ("success" != data.result) {
                            toaster.pop('error', '删除', '失败： ' + data.errorMsg, emeout);
                        } else {
                            toaster.pop('success', '删除', '成功', success_timeout);
                            $modalInstance.tableParams.reload();
                        }

                        $modalInstance.close('closed');

                    }).error(function (data) {
                        keyPersonDeleteBlock.stop();
                        toaster.pop('error', '删除', '失败： ' + data.errorMsg, error_timeout);
                    });

            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };


        // 查看详情
        $scope.keyPersonDetailOpen = function (row) {
            var modalInstance = $modal.open({
                templateUrl: 'keyPersonDetailDialog',
                controller: keyPersonDetailItemCtrl,
                backdrop: false,
                size: ''
            });

            // 父控制层 的表格 传递给 子控制层
            modalInstance.row = row;
        };

        var keyPersonDetailItemCtrl = function ($scope, $modalInstance) {

            var reqUrl = urlService.getFullUrl('/keyPerson/query?personId=' + $modalInstance.row.personId);

            var keyPersonDetailBlockUI = blockUI.instances.get('keyPersonDetailBlockUI');
            keyPersonDetailBlockUI.start();
            // 发送 url 参数的 POST 请求
            $http.get(reqUrl)
                .success(function (data) {
                    keyPersonDetailBlockUI.stop();
                    if ("success" === data.result) {
                        $scope.keyPersonInfo = data.data;
                    } else {
                        toaster.pop('error', '查询', '失败：'+ data.errorMsg, success_timeout);
                    }

                }).error(function (data) {
                    keyPersonDetailBlockUI.stop();
                    toaster.pop('error', '查询', '失败： ' + data.errorMsg, error_timeout);
                });

            $scope.isEdit = true;
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }

    }
]);