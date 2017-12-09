/**
 * Created by lijm on 2017/8/9.
 */
App.controller('RolePrivilegeCtrl', ['$scope', '$location', '$filter', '$http', '$state', '$modal', 'httpService', 'DtTableParamsFactory', 'blockUI', 'toaster', 'userService', 'responseHandler',
    function ($scope, $location, $filter, $http, $state, $modal, httpService, DtTableParamsFactory, blockUI, toaster, userService, responseHandler) {
        $scope.privilegeTableParams = DtTableParamsFactory.getTableParams(function ($defer, params) {
            $scope.getPrivilegePageData($filter, $defer, params);
        });
        $scope.getPrivilegePageData = function ($filter, $defer, params) {
            var myBlockUIPrivilege = blockUI.instances.get('myBlockUIPrivilege');
            myBlockUIPrivilege.start("数据正在加载中...");
            $scope.isLoading = true;
            var data = {
                pageNum: params.page(),
                pageSize: params.count(),
                orderBy: '',
                orderAsc: false,
                privilegeName: $scope.searchWordName
            };

            httpService.getByUrl('/privilege/queryByPage', data)
                .then(function (data) {
                    params.total(data.totalRows);
                    if (data.totalRows) {
                        if (data.result != null) {
                            $defer.resolve(data.result);
                        } else {
                            params.page(params.page() - 1);
                        }
                    } else {
                        $defer.resolve(data.result);
                    }

                    $scope.currentPage = data.result;
                    myBlockUIPrivilege.stop();
                    $scope.isLoading = false;
                }, function (e) {
                    myBlockUIPrivilege.stop();
                    $scope.isLoading = false;
                });
        };

        // search
        $scope.searchPrivilege = function () {
            if ($scope.searchPrivilegeName == "") {
                $scope.searchPrivilegeName = null;
            }
            $scope.privilegeTableParams.page(1);
            $scope.privilegeTableParams.reload();
        };
        $scope.searchPrivilegeEnter = function(e) {
            var keycode = window.event?e.keyCode:e.which;
            if (keycode == 13) {
                $scope.searchPrivilege();
            }
        };


        $scope.roleRefresh=function () {
            $scope.privilegeTableParams.reload();
        };

        // model dialog for add or motify privilege
        $scope.openPrivilegeEditDialog = function (isMotify, item) {
            var modelDate = {
                isMotify:isMotify,
                tableParams:$scope.privilegeTableParams,
                privilegeData:item || {}
            };
            var modalInstance = $modal.open({
                templateUrl: '/privilegeConfDialogId.html',
                controller: 'privilegeEditCtrl',
                backdrop: false,
                size: 'lg',
                resolve:{
                    items:function () {
                        return modelDate;
                    }
                }
            });
            modalInstance.result.then(function (newPrivilege) {
                console.log('Canceled' + isMotify ? 'modified' : 'add' + 'privilege');
                $scope.roleRefresh();
                if (!isMotify) {
                    console.log('add new privilege :', newPrivilege);
                }
            }, function () {
                console.log('Canceled' + isMotify ? 'modified' : 'add' + 'privilege');
            });
        };

    //    model dialog for delete privilege
        $scope.openPrivilegeDelDialog = function (privilegeIndex) {
            var modalInstance = $modal.open({
                templateUrl: '/privilegeDelDialog.html',
                controller: DelPrivilegeModalInstanceCtrl,
                backdrop: false,
            });

            modalInstance.privilegeData = $scope.currentPage[privilegeIndex];
            modalInstance.tableParams = $scope.privilegeTableParams;
            modalInstance.result.then(function () {
                $scope.roleRefresh();
                console.log('Deleted privilege index: ', privilegeIndex);
                $scope.privilegeTableParams.reload();
            }, function () {
                console.log('Canceled add privilege.');
            });
        };
        var DelPrivilegeModalInstanceCtrl = function ($scope, $modalInstance) {

            $scope.delrole = $modalInstance.privilegeData.privilegeName;
            $scope.submitForm = function () {
                var privilegeId = $modalInstance.privilegeData.id;
                var delPrivilegeBlockUI = blockUI.instances.get('DelPrivilegeBlockUI');
                $modalInstance.close();
                 delPrivilegeBlockUI.start();
                 httpService.postByUrl('/privilege/delete',{'privilegeId':privilegeId})
                     .then(function (data) {
                         console.log('删除权限成功：');
                         console.log(data);
                         $modalInstance.tableParams.reload();
                          delPrivilegeBlockUI.stop();
                         $modalInstance.close();
                     },function () {
                          delPrivilegeBlockUI.stop();
                         $modalInstance.close();
                     })
            };


            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        };

    }])

    //
    .controller('privilegeEditCtrl', ['$scope', '$modalInstance', '$filter', 'httpService', 'DtTableParamsFactory', 'blockUI', 'items',
        function ($scope, $modalInstance, $filter, httpService, DtTableParamsFactory, blockUI, items) {
            $scope.modelData = items;
            $scope.selectedChannelList = [];
            init();
            $scope.channelTableParams = DtTableParamsFactory.getTableParams(function ($defer, params) {
                $scope.getChannelPageData($filter, $defer, params);
            },{"counts":[6],"count":6});

            $scope.getChannelPageData = function ($filter, $defer, params) {
                var BlockUIChannel = blockUI.instances.get('blockUIChannel');
                BlockUIChannel.start("数据正在加载中...");
                $scope.isLoading = true;
                var data = {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: '',
                    orderAsc: false
                };

                httpService.getByUrl('/captureChannel/getAllChannelsByPage', data)
                    .then(function (data) {
                        console.log('获取频道列表');
                        console.log(data);
                        params.total(data.totalRows);
                        if (data.totalRows) {
                            if (data.result != null) {
                                $defer.resolve(data.result);
                            } else {
                                params.page(params.page() - 1);
                            }
                        } else {
                            $defer.resolve(data.result);
                        }

                        $scope.currentPage = data.result;
                        BlockUIChannel.stop();
                        $scope.isLoading = false;
                    }, function (e) {
                        BlockUIChannel.stop();
                        $scope.isLoading = false;
                    });
            };

            $scope.getPrivSelectedNum = function(){
                $scope.num = $scope.selectedChannelList.length;
            };

            //初始化
            function init() {
                $scope.newPrivilege = {};
                $scope.isMotify = $scope.modelData.isMotify;
            }

            //初始化页面
            if ($scope.isMotify) {
                $scope.ModalTitle = "修改权限";
                $scope.bIsModifyPage = true;
                $scope.newPrivilege.name = $scope.modelData.privilegeData.privilegeName;
                $scope.newPrivilege.des = $scope.modelData.privilegeData.description;
                $scope.newPrivilege.keyName = $scope.modelData.privilegeData.privilege;
            } else {
                $scope.bIsModifyPage = false;
                $scope.ModalTitle = "添加权限";
            }
            $scope.getPrivSelectedNum();

            // to add selected style
            $scope.checkSelected = function (channel) {
                var selected = false;
                $scope.selectedChannelList.forEach(function (item) {
                    if(channel.channelId == item.channelId){
                        selected = true;
                    }
                });
                return selected;
            };

            // change selected status
            $scope.selectItem = function (item) {
                if($scope.checkSelected(item)){
                    $scope.removeFromSelectedChannel(item);
                }else {
                    $scope.addToSelectedChannel(item);
                };
                $scope.getPrivSelectedNum();
            };

            $scope.addToSelectedChannel = function (channel) {
                $scope.selectedChannelList.push(channel);
            };
            $scope.removeFromSelectedChannel = function (channel) {
                var list = [];
                $scope.selectedChannelList.forEach(function (item) {
                    if(channel.channelId != item.channelId){
                        list.push(item);
                    }
                });
                $scope.selectedChannelList = list;
            };


            $scope.submitted = false;

            $scope.modifyPrivilege = function(data, block){
                httpService.postByJson('/privilege/update',data)
                    .then(function (data) {
                        console.log('修改角色成功');
                        console.log(data);
                        $scope.modelData.tableParams.reload();
                        $modalInstance.close();
                        block.stop();
                    },function () {
                        $modalInstance.close();
                        block.stop();
                        console.log('修改角色失败');
                    })
            };
            $scope.createPrivilege = function(data, block){
                httpService.postByJson('/privilege/create',data)
                    .then(function (data) {
                        console.log('添加权限成功');
                        $scope.modelData.tableParams.reload();
                        $modalInstance.close();
                        block.stop();
                    },function () {
                        $modalInstance.close();
                        block.stop();
                        console.log('添加权限失败');
                    })
            };

            $scope.submitForm = function () {
                var myBlockUIPrivileges = blockUI.instances.get('blockUIPrivileges');
                $scope.submitted = true;
                var channel = '';
                $scope.selectedChannelList.forEach(function (item) {
                    channel += item.channelId + ',';
                 });
                myBlockUIPrivileges.start();
                var privilegeData = {
                    description: $scope.newPrivilege.des,
                    privileges: channel
                };


                // Motify
                 if($scope.isMotify){
                     privilegeData.id = $scope.modelData.privilegeData.id;
                     $scope.modifyPrivilege(privilegeData, myBlockUIPrivileges);
                 }else {// create
                     privilegeData.privilegeName = $scope.newPrivilege.name;
                     privilegeData.privilege = $scope.newPrivilege.keyName;
                     $scope.createPrivilege(privilegeData, myBlockUIPrivileges);
                 }

            };


            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }])
