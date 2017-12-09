App.controller('userManageCtrl', ['$scope', '$location', '$localStorage', '$http', '$rootScope', '$resource', '$modal', '$state', 'ngTableParams', 'blockUI', 'toaster', 'userService', 'responseHandler', 'roleService', 'httpService',
    function ($scope, $location, $localStorage, $http, $rootScope, $resource, $modal, $state, ngTableParams, blockUI, toaster, userService, responseHandler, roleService,httpService) {
        'use strict';
        $scope.localUser = $rootScope.user;
        $scope.userTableParams = new ngTableParams({
            page: 1,             // show first page
            count: 10           // count per page
        }, {
            getData: function ($defer, params) {
                var keywordRecognitionBlockUI = blockUI.instances.get('keywordRecognitionBlockUI');
                keywordRecognitionBlockUI.start();

                $scope.isLoading = true;

                var data = {
                    loginName: $scope.searchUserName,
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: 'createTime',
                    orderAsc: false
                };

                userService.queryUser(data).then(function (response) {
                    $scope.isTimeout = false;
                    keywordRecognitionBlockUI.stop();

                    if (!responseHandler.isSuccess(response)) {
                        toaster.pop("error", "查询用户", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                    }

                    var data = response.data;
                    console.log('用戶列表');
                    console.log(data.result);
                    $defer.resolve(data.result);
                    params.total(data.totalRows);

                }, function () {
                    $scope.isLoading = false;
                    keywordRecognitionBlockUI.stop();
                    toaster.pop("error", "查询用户请求", "失败", error_timeout);
                });
            }
        });

        /* -- 按键查询--*/
        $scope.searchUserEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.searchUser();
            }
        };

        $scope.searchUser = function () {
            if ($scope.searchUserName == "") {
                $scope.searchUserName = null;
            }

            if (1 == $scope.userTableParams.page()) {
                $scope.userTableParams.reload();
            } else {
                $scope.userTableParams.page(1);
            }
        };

        $scope.getNullCondition = function () {
            if ($scope.searchUserName.length == 0) {
                $scope.userTableParams.reload();
            }
        };

        $scope.checkCanOpUser = function(user){
           return $scope.localUser.userId == user.userId;
        };

        $scope.allsetlist = {allset:false};
        $scope.setAllSelected = function() {
            $scope.userTableParams.data.forEach(function (value, index) {
                value.selected = $scope.allsetlist.allset;
            });

            if ($scope.allsetlist.allset) {
                $scope.sumChose = $scope.userTableParams.data.length;
            } else {
                $scope.sumChose = 0;
            }
        };

        $scope.sumChose = 0;
        $scope.choseOne = function(choseIndex) {
            $scope.userTableParams.data.forEach(function (value, index) {
                if (choseIndex == index) {
                    if (value.selected == true) {
                        $scope.sumChose = $scope.sumChose + 1;
                        if ($scope.sumChose == $scope.userTableParams.data.length) {
                            $scope.allsetlist.allset = true;
                        }
                    } else {
                        $scope.sumChose = $scope.sumChose - 1;
                        if ($scope.sumChose < $scope.userTableParams.data.length) {
                            $scope.allsetlist.allset = false;
                        }
                    }
                }
            });
        };

        $scope.cancelSelect = function(){
            $scope.userTableParams.data.forEach(function (value) {
                value.selected = false;
            })
            $scope.allsetlist.allset = false;
        }

        $scope.exportSystemInitPasswords = function() {
            var ids = [];
            for (var i = 0; i <= $scope.userTableParams.data.length - 1; i++) {
                if ($scope.userTableParams.data[i].selected) {
                    ids.push($scope.userTableParams.data[i].userId);
                }
            }

            userService.exportSystemInitPasswords(ids);
            $scope.cancelSelect();
        };

        $scope.openUserAddDialog = function () {
            var modalInstance = $modal.open({
                templateUrl: '/UserConfDialogId.html',
                controller: EditUserModalInstanceCtrl,
                backdrop: false
            });
            modalInstance.userTableParams = $scope.userTableParams;
            modalInstance.result.then(function (newUser) {
                console.log('Add new user: ', newUser);
            }, function () {
                console.log('Canceled add user.');
            });
        };


        // 删除
        $scope.openUserDelDialog = function (row) {
            var modalInstance = $modal.open({
                templateUrl: '/UserConfDelDialogTmp.html',
                controller: DelUserModalInstanceCtrl,
                backdrop: false
            });
            modalInstance.userTableParams = $scope.userTableParams;
            modalInstance.row = row;
        };

        var DelUserModalInstanceCtrl = function ($scope, $modalInstance) {
            var myBlockUIUser = blockUI.instances.get('myBlockUIDelUser');
            $scope.loginName = $modalInstance.row.loginName;
            $scope.submitForm = function() {
                $scope.delUser();
            };

            $scope.delUser = function() {
                myBlockUIUser.start("数据正在加载中...");
                $scope.isTimeout=true;

                var data = {
                    userId:$modalInstance.row.userId
                };
                userService.deleteUser(data).then(function (response) {
                    $scope.isTimeout=false;

                    myBlockUIUser.stop();
                    if(!responseHandler.isSuccess(response)){
                        toaster.pop("error", "删除用户","失败："+responseHandler.getErrorMsg(response), error_timeout);
                    } else {
                        toaster.pop("success", "删除用户","成功", success_timeout);

                    }
                    $modalInstance.close('closed');
                    $modalInstance.userTableParams.reload();
                },function(){
                    myBlockUIUser.stop();
                    toaster.pop("error", "删除用户请求", "失败", error_timeout);
                    $modalInstance.close('closed');
                });
            };



            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        };



        // 查看详情
        $scope.goToUserInfo = function (row) {
            $localStorage.icsUserPageFrom = {'from':$location.path()};
            $state.go('app.user-info', {userId: row.userId, nickName: row.nickName});
        };


        // 编辑
        $scope.openUserModifyDialog = function (row) {
            var modalInstance = $modal.open({
                templateUrl: '/UserConfDialogId.html',
                controller: EditUserModalInstanceCtrl,
                backdrop: false
            });

            modalInstance.userTableParams = $scope.userTableParams;
            modalInstance.row = row;
        };

        var EditUserModalInstanceCtrl = function ($scope, $modalInstance) {

            $scope.availableRoles = [];
            $scope.authorization = {selectedRoleIds: []};

            var myBlockUIUser = blockUI.instances.get('myBlockUIEditUser');

            // 编辑页面 初始化角色信息
            var funcInitPageRoleInfo = function (roleNames) {

                // 已选择的权限
                var oldRoles = roleNames.split(",");
                if (!oldRoles) {
                    return;
                }

                // 所有可用的权限
                if (!$scope.availableRoles) {
                    return;
                }

                // 选中的权限
                var selectedRoleIds=[];
                for (var i = 0; i < oldRoles.length; i++) {
                    var oldName = oldRoles[i];
                    for (var j = 0; j < $scope.availableRoles.length; j++) {
                        var role = $scope.availableRoles[j];
                        if (oldName === role.roleName) {
                            selectedRoleIds.push(role.id);
                            continue;
                        }
                    }
                }

                $scope.authorization.selectedRoleIds = selectedRoleIds;
            };

            //获取平台的角色信息
            $scope.getRolesReturn = false;
            var getRoles = function (roleNames) {
                roleService.queryRoles().then(function (response) {
                    if (responseHandler.isSuccess(response)) {
                        console.log(response.data.result);
                        $scope.availableRoles = response.data.result;
                        if ($scope.bIsModifyPage) {
                            funcInitPageRoleInfo(roleNames)
                        }
                    } else {
                        toaster.pop("error", "查询角色", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                    }
                    $scope.getRolesReturn = true;
                }, function () {
                    toaster.pop("error", "查询角色请求", "失败", error_timeout);
                });
            };

            //getRoles();

            //获取用户信息，并尝试初始化页面信息
            var getUserDetail = function(userId, funcProcData){
                return $resource('../control/users/:userId').get({userId:userId},function(response){
                    var data = responseHandler.getData(response);
                    $scope.newUser = data;
                    if (funcProcData) {
                        funcProcData();
                    }
                });
            };

            var getPolicyList = function () {
                $resource('../control/loginPolicy/getLoginPolicyList').get({}, function(response) {
                    $scope.policyList=response.data.rows;
                    for(var i=0 ; i< $scope.policyList.length ; i++){
                        if($scope.policyList[i].policyName == "默认策略"){
                            $scope.loginPolicy.selected = $scope.policyList[i];
                            return;
                        }
                    }
                });
            };

            var checkSelectedRoleIds = function () {
                return $scope.authorization.selectedRoleIds.length;
            };

            $scope.validateRolesInput = function () {
                return (($scope.UserAddForm.roles.$dirty || $scope.submitted) && !checkSelectedRoleIds());
            };


            //初始化用户添加和修改页面信息
            if ($modalInstance.row) {
                $scope.bIsModifyPage = true;
                $scope.ModalTitle = "修改用户";

                $scope.newUser = {
                    userId: $modalInstance.row.userId,
                    loginName: $modalInstance.row.loginName,
                    fullName: $modalInstance.row.nickName,
                    realName: '',
                    cellphoneNum: $modalInstance.row.cellphoneNum,
                    telephoneNum: $modalInstance.row.telephoneNum,
                    userEmail: $modalInstance.row.email,
                    roleIds: getRoles($modalInstance.row.roles),
                    policyId: '',
                    description: ''
                };
            } else {
                $scope.bIsModifyPage = false;
                $scope.ModalTitle = "添加用户";
                getRoles();
            }

            $scope.submitted = false;
            $scope.submitForm = function(newUser) {
                if ($scope.bIsModifyPage) {
                    $scope.modifyUser(newUser, $modalInstance);
                } else {
                    $scope.addUser(newUser, $modalInstance);
                }

            };

            $scope.addUser = function(newUser,$modalInstance) {
                myBlockUIUser.start("数据正在加载中...");
                $scope.isTimeout=true;

                var roleIdList = $scope.authorization.selectedRoleIds.toString();
                var data = {
                    loginName: newUser.loginName,
                    nickName: newUser.fullName,
                    realName: '',
                    cellphoneNum: newUser.cellphoneNum,
                    telephoneNum: newUser.telephoneNum,
                    email: newUser.userEmail,
                    roleIds: roleIdList,
                    policyId: '',
                    description: ''
                };
                userService.addUser(data).then(function (response) {
                    $scope.isTimeout=false;

                    myBlockUIUser.stop();
                    if(!responseHandler.isSuccess(response)){
                        toaster.pop("error", "创建用户","失败："+responseHandler.getErrorMsg(response), error_timeout);
                    } else {
                        toaster.pop("success", "创建用户","成功", success_timeout);
                        $modalInstance.close(newUser);
                    }

                    $modalInstance.userTableParams.reload();
                },function(){
                    myBlockUIUser.stop();
                    toaster.pop("error", "创建用户请求", "失败", error_timeout);
                    $modalInstance.close(newUser);
                });
            };

            $scope.modifyUser = function (newUser,$modalInstance) {
                myBlockUIUser.start("数据正在加载中...");
                $scope.isTimeout=true;

                //修改角色
                var roleIdList = $scope.authorization.selectedRoleIds.toString();
                var data = {
                    loginName: newUser.loginName,
                    nickName: newUser.fullName,
                    realName: '',
                    cellphoneNum: newUser.cellphoneNum,
                    telephoneNum: newUser.telephoneNum,
                    email: newUser.userEmail,
                    roleIds: roleIdList,
                    policyId: '',
                    description: ''
                };

                userService.updateUser(data).then(function (response) {
                    $scope.isTimeout=false;

                    myBlockUIUser.stop();
                    if(!responseHandler.isSuccess(response)){
                        toaster.pop("error", "修改用户", "失败：" + responseHandler.getErrorMsg(response), error_timeout);
                    } else {
                        toaster.pop("success", "修改用户", "成功", success_timeout);
                        $modalInstance.close(newUser);
                    }

                    $modalInstance.userTableParams.reload();
                },function(){
                    myBlockUIUser.stop();
                    toaster.pop("error", "修改用户请求", "失败", error_timeout);
                    $modalInstance.close(newUser);
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        };

        $scope.openForbidDialog = function(row){
            var modalInstance = $modal.open({
                templateUrl: '/UserForbidDialog.html',
                controller: UseForbidInstanceCtrl,
                backdrop: false
            });
            modalInstance.userTableParams = $scope.userTableParams;
            modalInstance.row = row;
        };
        var UseForbidInstanceCtrl = function($scope, $modalInstance){
            var myBlockUIUserForbid = blockUI.instances.get('myBlockUIUserForbid');
            $scope.loginName = $modalInstance.row.loginName;
            $scope.confirm = function() {
                myBlockUIUserForbid.start("数据正在加载中...");
                var data = {
                    userId:$modalInstance.row.userId
                };
                httpService.postByUrl('/user/closeForbidTime',data).then(function(response){
                    $modalInstance.userTableParams.reload();
                    myBlockUIUserForbid.stop();
                    $modalInstance.close();
                },function(){
                    toaster.pop("error", "关掉禁闭失败","请重试", error_timeout);
                    myBlockUIUserForbid.stop();
                })

            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }

    }]);