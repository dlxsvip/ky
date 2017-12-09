/**
 * Created by huaam on 2017/8/2.
 */
App.controller('RoleManageCtrl', ['$scope', '$location', '$filter', '$http', '$state', '$modal', 'httpService', 'DtTableParamsFactory', 'blockUI', 'toaster', 'userService', 'permissions',
    function ($scope, $location, $filter, $http, $state, $modal, httpService, DtTableParamsFactory, blockUI, toaster, userService, permissions){
        //page-pagition
        $scope.roleTableParams = DtTableParamsFactory.getTableParams(function ($defer, params) {
            $scope.getRolePageData($filter, $defer, params);
        });
        $scope.roleRefresh = function(){
            $scope.roleTableParams.reload();
        };

        $scope.getRolePageData = function ($filter, $defer, params) {
            var myBlockUIUser = blockUI.instances.get('myBlockUIRoles');
            myBlockUIUser.start("数据正在加载中...");
            $scope.isLoading = true;
            var data = {
                pageNum: params.page(),
                pageSize: params.count(),
                orderBy: '',
                orderAsc: false,
                roleName:$scope.searchWordName
            };

            httpService.getByUrl('/role/queryByPage',data)
                .then(function (data) {
                    params.total(data.totalRows);
                    if (data.totalRows) {
                        if(data.result != null){
                            $defer.resolve(data.result);
                        } else{
                            params.page(params.page() -1 );
                        }
                    }else{
                        $defer.resolve(data.result);
                    }

                    $scope.currentPage = data.result;
                    myBlockUIUser.stop();
                    $scope.isLoading = false;
                }, function (e) {
                    myBlockUIUser.stop();
                    $scope.isLoading = false;
                });
        };

        $scope.searchRoleEnter = function(e) {
            var keycode = window.event?e.keyCode:e.which;
            if (keycode == 13) {
                $scope.searchRole();
            }
        };

        $scope.searchRole = function () {
            if ($scope.searchWordName == "") {
                $scope.searchWordName = null;
            }
            $scope.roleTableParams.page(1);
            $scope.roleTableParams.reload();
        };

        $scope.checkPermissions = function(role,priviliges){
            //if(!role.isDefault && permissions.hasPermission(priviliges)){
            //    return true;
            //}else{
            //    return false;
            //}
            return role.isDefault;
        };

        //
        httpService.getByUrl('/privilege/queryByPage').then(function(response){
            console.log('权限列表');
            console.log(response);
            $scope.privilegeList = response.result;
        })

        //添加角色
        $scope.openRoleEditDialog = function (isMotify, role) {
            var modalInstance = $modal.open({
                templateUrl: '/RoleConfDialogId.html',
                controller: ModalInstanceCtrl,
                backdrop: false,
                size: 'lg'
            });
            modalInstance.isMotify = isMotify;
            modalInstance.privilegeList = $scope.privilegeList;
            modalInstance.tableParams = $scope.roleTableParams;
            // 是编辑
            if(isMotify){
                modalInstance.roleData = role;
            }
            modalInstance.result.then(function (newRole) {
                $scope.roleRefresh();
                console.log('Canceled' + isMotify ? 'modified' : 'add' + 'role');
                if(!isMotify){
                    console.log('add new role :', newRole);
                }
            }, function () {
                console.log('Canceled' + isMotify ? 'modified' : 'add' + 'role');
            });
        };

        //添加、更新角色的model-ctrl
        var ModalInstanceCtrl = function ($scope, $modalInstance, blockUI) {
            intit();

            //初始化
            function intit(){
                $scope.platforms = [];
                $scope.privileges = [];
                $scope.privilegeList = angular.copy($modalInstance.privilegeList);
                $scope.newRole = {};
                $scope.role = {};
                $scope.isMotify = $modalInstance.isMotify;
                $scope.privilegeList.forEach(function(item){
                    item.selected =  false;
                });

            }

            //计算选择的权限的个数
            $scope.getPrivSelectedNum = function(){
                var num = 0;
                $scope.privilegeList.forEach(function(i){
                    if(i.selected != null) {
                        if (i.selected == true) {
                            num++;
                        }
                    }
                });
                $scope.num = num;
            };

            $scope.selectItem = function(i) {
                i.selected = ! i.selected;
            };


            //检查权限是否被选中
            $scope.checkSelected = function (i) {
                $scope.getPrivSelectedNum();
                return i.selected;
            };

            //修改权限时读取当前角色的权限并展示到页面上
            $scope.getRolePrivs = function () {
                $scope.privilegeList.forEach(function(item){
                    $modalInstance.roleData.privilege.forEach(function(i){
                        if( item.id == i.id){
                            item.selected = true;
                        }
                    });
                });
                $scope.getPrivSelectedNum();
            };

            //初始化页面
            if ($scope.isMotify) {
                console.log('修改');
                $scope.ModalTitle = "修改角色";
                $scope.bIsModifyPage = true;
                $scope.newRole.name = $modalInstance.roleData.roleName;
                $scope.newRole.des = $modalInstance.roleData.description;
                $scope.newRole.keyName = $modalInstance.roleData.role;
                $scope.getRolePrivs();
            }else {
                $scope.bIsModifyPage = false;
                $scope.ModalTitle = "添加角色";
            }

            $scope.submitted = false;

            $scope.submitForm = function () {
                var myBlockUIUser = blockUI.instances.get('blockUIRoles');
                $scope.submitted = true;
                var privileges = '';
                $scope.privilegeList.forEach(function(item) {
                    if (item.selected == true) {
                        privileges += item.id + ',';
                    }
                });
                myBlockUIUser.start();
                var createRoleData = {};

                //向后台提交数据
                if($scope.isMotify){
                    createRoleData = {
                        roleId:$modalInstance.roleData.id,
                        description: $scope.newRole.des,
                        privileges: privileges
                    };
                    httpService.postByJson('/role/update',createRoleData)
                        .then(function (data) {
                            console.log('修改角色成功');
                            $modalInstance.tableParams.reload();
                            $modalInstance.close();
                            myBlockUIUser.stop();
                        },function () {
                            $modalInstance.close();
                            myBlockUIUser.stop();
                            console.log('修改角色失败');
                        })
                }else {
                    createRoleData = {
                        roleName: $scope.newRole.name,
                        description: $scope.newRole.des,
                        privileges: privileges,
                        role: $scope.newRole.keyName
                    };
                    httpService.postByJson('/role/create',createRoleData)
                        .then(function (data) {
                            console.log('添加角色成功');
                            $modalInstance.tableParams.reload();
                            $modalInstance.close();
                            myBlockUIUser.stop();
                        },function () {
                            $modalInstance.close();
                            myBlockUIUser.stop();
                            console.log('添加角色失败');
                        })
                }

            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        // 打开删除model
        $scope.openRoleDelDialog = function (roleIndex) {
            var modalInstance = $modal.open({
                templateUrl: '/RoleMangeDelDialogTmp.html',
                controller: DelRoleModalInstanceCtrl,
                backdrop: false,
            });

            modalInstance.roleData = $scope.currentPage[roleIndex];
            modalInstance.tableParams = $scope.roleTableParams;
            modalInstance.result.then(function () {
                console.log('Deleted role index: ', roleIndex);
                $scope.roleRefresh();
            }, function () {
                console.log('Canceled add role.');
            });
        };
        //删除角色model-ctrl
        var DelRoleModalInstanceCtrl = function ($scope, $modalInstance) {

            $scope.delrole = $modalInstance.roleData.roleName;
            $scope.submitForm = function (roleIndex) {
                var delRoleId = $modalInstance.roleData.id;
                var delRoleBlockUI = blockUI.instances.get('DelRoleBlockUI');
                delRoleBlockUI.start();
                httpService.postByUrl('/role/delete',{'roleId':delRoleId})
                    .then(function (data) {
                        console.log('删除角色成功：');
                        $modalInstance.tableParams.reload();
                        $modalInstance.close();
                    },function () {
                        $modalInstance.close();
                    })
            };


            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        };

}])