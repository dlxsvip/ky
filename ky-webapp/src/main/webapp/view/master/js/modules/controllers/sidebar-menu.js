/**=========================================================
 * Module: sidebar-menu.js
 * Handle sidebar collapsible elements
 =========================================================*/

App.controller('SidebarController', ['$rootScope', '$scope', '$state', '$http', '$timeout', '$localStorage', 'Utils', 'sidebarService',
    function ($rootScope, $scope, $state, $http, $timeout, $localStorage, Utils, sidebarService) {

        var collapseList = [];

        // demo: when switch from collapse to hover, close all items
        $rootScope.$watch('app.layout.asideHover', function (oldVal, newVal) {
            if (newVal === false && oldVal === true) {
                closeAllBut(-1);
            }
        });

        // Check item and children active state
        var isActive = function (item) {

            if (!item) {
                return;
            }

            if (!item.sref || item.sref == '#') {
                var foundActive = false;
                angular.forEach(item.submenu, function (value, key) {
                    if (isActive(value)) {
                        foundActive = true;
                    }
                });
                return foundActive;
            }
            else {
                return $state.is(item.sref) || $state.includes(item.sref);
            }
        };

        // Load menu from json file
        // -----------------------------------

        $scope.getMenuItemPropClasses = function (item) {
            return (item.heading ? 'nav-heading' : '') + (isActive(item) ? ' active' : '');
        };

        $rootScope.$on("registerChanged", function () {
            $scope.loadSidebarMenu();
        });

        $scope.loadSidebarMenu = function () {

            var menuJson = 'server/sidebar-menu.json';
            var menuURL = menuJson + '?v=' + (new Date().getTime()); // jumps cache
            $http.get(menuURL)
                .success(function (items) {
                    //$scope.menuItems = hideMenu(items);
                    $scope.menuItems = filterMenu(hideMenu(items));
                })
                .error(function (data, status, headers, config) {
                    alert('Failure loading menu');
                });
        };

        $scope.loadSidebarMenu();

        // Handle sidebar collapse items
        // -----------------------------------

        $scope.addCollapse = function ($index, item) {

            collapseList[$index] = $rootScope.app.layout.asideHover ? true : !isActive(item);
            if(!$index && $localStorage.icsIsFirstInPage.icsIsFirstInPage){
                collapseList[$index] = !$localStorage.icsIsFirstInPage.icsIsFirstInPage;
                $localStorage.icsIsFirstInPage = {'icsIsFirstInPage':false};
            }
        };

        $scope.isCollapse = function ($index) {
            return (collapseList[$index]);
        };

        $scope.toggleCollapse = function ($index, isParentItem) {
            console.log('zouzouzou');
            // collapsed sidebar doesn't toggle drodopwn
            if (Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover) return true;
            // make sure the item index exists
            if (angular.isDefined(collapseList[$index])) {
                if (!$scope.lastEventFromChild) {
                    collapseList[$index] = !collapseList[$index];
                    closeAllBut($index);
                }
            }
            else if (isParentItem) {
                closeAllBut(-1);
            }
            $scope.lastEventFromChild = isChild($index);

            return true;

        };

        function closeAllBut(index) {
            index += '';
            for (var i in collapseList) {
                if (index < 0 || index.indexOf(i) < 0)
                    collapseList[i] = true;
            }
        }

        function isChild($index) {
            return (typeof $index === 'string') && !($index.indexOf('-') < 0);
        }

        function isTextInJson(obj, text) {
            for (var key in obj) {
                var val = obj[key];
                if (text == key) {
                    return true;
                } else {
                    if (val) {
                        if (isTextInJson(val, text)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        // 过滤拥有权限的导航
        function filterMenu(items) {
            var menuItems = [];

            // 当前用户所有的权限
            var userPermission = $rootScope.user.privileges;

            // 过滤拥有权限的导航
            for (var i = 0; i < items.length; i++) {
                var permission = items[i].permission;
                var permissionArr = permission.split(",");

                for(var j = 0; j < permissionArr.length; j++){
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


        // 隐藏子导航
        function hideSubMenu(item) {
            var subMenuArr = item.submenu;
            if (subMenuArr) {
                var newSubMenuArr = [];
                for (var j = 0; j < subMenuArr.length; j++) {
                    if (subMenuArr[j].hide) {
                        continue;
                    }

                    // 递归 隐藏子导航的 子导航
                    subMenuArr[j] = hideSubMenu(subMenuArr[j]);

                    newSubMenuArr.push(subMenuArr[j]);
                }

                item.submenu = newSubMenuArr;
            }

            return item;
        }

        // 隐藏导航
        function hideMenu(items) {
            var menuItems = [];

            // 去掉隐藏的导航
            for (var i = 0; i < items.length; i++) {
                if (items[i].hide) {
                    continue;
                }

                var newItem = hideSubMenu(items[i]);

                menuItems.push(newItem);
            }

            return menuItems;
        }
    }
]);
