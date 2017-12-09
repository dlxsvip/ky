/**
 * Created by yl on 2017/8/7.
 */
App.controller('waitingController', ['$rootScope', '$scope', '$state', '$localStorage', 'sidebarService',
    function ($rootScope, $scope, $state, $localStorage, sidebarService) {

        sidebarService.loadSidebarMenu(false).then(function (response) {
            var mean = response;
            mean = sidebarService.screenMean(mean);

            // mean定位到第一个菜单
            goFirstMenu(mean);
            $localStorage.icsIsFirstInPage = {'icsIsFirstInPage':true};
        }, function () {
            //toaster.pop("error", "查询用户请求", "失败", error_timeout);
        });

        // mean定位到第一个菜单
        function goFirstMenu(meanArr) {
            if (meanArr[0]) {
                if (meanArr[0].submenu) {
                    return goFirstMenu(meanArr[0].submenu)
                }
                return $state.go(meanArr[0].sref);
            }
        }
    }
]);