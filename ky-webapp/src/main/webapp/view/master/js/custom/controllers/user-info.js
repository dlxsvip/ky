App.controller('userInfoCtrl', ['$scope', '$location', '$state', '$stateParams', '$localStorage', 'permissions',
    function ($scope, $location, $state, $stateParams, $localStorage, permissions) {

        // 是否管理员
        var isAdmin = permissions.hasPermission("USER_CONFIG");

        $scope.userId = $stateParams.userId;
        $scope.nickName = $stateParams.nickName;

        $scope.tabList = [
            {CN: "基本信息", name: "basicInfo", route: "app.user-info.base", active: true}
        ];

        if (isAdmin && $scope.userId && $scope.nickName) {
            // 重置他人的密码
            $scope.tabList.push({CN: "重置密码", name: "modifyPassword", route: "app.user-info.resetpwd", active: false})
        } else {
            // 修改自己的密码
            $scope.tabList.push({CN: "修改密码", name: "modifyPassword", route: "app.user-info.updatepwd", active: false})
        }

        $scope.setActiveTab = function(tab){
            $scope.tabList.forEach(function(t){
                t.active = false;
            });

            tab.active = true;
            $state.go(tab.route, {userId: $scope.userId});
        };

        $scope.active = function(tab){
            var activeValue = false;
            if($state.includes(tab.route)){
                activeValue = true;
            }
            return activeValue;
        };
        $scope.goBack = function() {
            $location.path($localStorage.icsUserPageFrom.from);
        };



        $scope.setActiveTab($scope.tabList[0]);

    }
]);