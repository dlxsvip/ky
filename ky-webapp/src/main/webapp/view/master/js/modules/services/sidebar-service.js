/**
 * Created by yl on 2017/8/7.
 */
App.service('sidebarService', ['$rootScope', '$qService', function ($rootScope, $qService) {
    var sidebarService = {};

    /**
     * 加载全部菜单
     * @param refresh
     * @returns {*}
     */
    sidebarService.loadSidebarMenu = function (refresh) {
        var menuURL = 'server/sidebar-menu.json';

        if (refresh) {
            // 请求最新的
            menuURL = menuURL + '?v=' + (new Date().getTime());
        }

        return $qService.getByUrl(menuURL, {});
    };


    /**
     * 筛选 菜单
     * @param items 菜单
     * @returns {*}
     */
    sidebarService.screenMean = function (items) {
        return filterMenu(hideMenu(items));
    };


    // 过滤拥有权限的菜单
    function filterMenu(items) {
        var menuItems = [];

        // 当前用户所有的权限
        var userPermission = $rootScope.user.privileges;

        // 过滤拥有权限的菜单
        for (var i = 0; i < items.length; i++) {
            var permission = items[i].permission;
            var permissionArr = permission.split(",");

            for (var j = 0; j < permissionArr.length; j++) {
                if (userPermission.indexOf(permissionArr[j]) > -1) {
                    //var newItem = filterSubMenu(items[i]);
                    var newItem = items[i];
                    menuItems.push(newItem);
                    break;
                }
            }
        }

        return menuItems;
    }


    // 隐藏子菜单
    function hideSubMenu(item) {
        var subMenuArr = item.submenu;
        if (subMenuArr) {
            var newSubMenuArr = [];
            for (var j = 0; j < subMenuArr.length; j++) {
                if (subMenuArr[j].hide) {
                    continue;
                }

                // 递归 隐藏子菜单的 子菜单
                subMenuArr[j] = hideSubMenu(subMenuArr[j]);

                newSubMenuArr.push(subMenuArr[j]);
            }

            item.submenu = newSubMenuArr;
        }

        return item;
    }

    // 隐藏菜单
    function hideMenu(items) {
        var menuItems = [];

        // 去掉拥有隐藏属性的菜单
        for (var i = 0; i < items.length; i++) {
            if (items[i].hide) {
                continue;
            }

            var newItem = hideSubMenu(items[i]);

            menuItems.push(newItem);
        }

        return menuItems;
    }

    return sidebarService;
}]);