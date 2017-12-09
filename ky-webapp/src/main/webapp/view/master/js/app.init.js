/*!
 * 
 * Angle - Bootstrap Admin App + AngularJS
 * 
 * Author: @themicon_co
 * Website: http://themicon.co
 * License: http://support.wrapbootstrap.com/knowledge_base/topics/usage-licenses
 * 
 */

if (typeof $ === 'undefined') { throw new Error('This application\'s JavaScript requires jQuery'); }

// APP START
// ----------------------------------- 
var model = Ics.name;
var App = angular.module(model, [
    'ngRoute',
    'ngAnimate',
    'ngStorage',
    'ngCookies',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',
    'oc.lazyLoad',
    'cfp.loadingBar',
    'ngSanitize',
    'ngResource',
    'ui.utils'
  ]);

var error_timeout = 0;
var success_timeout = 3000;

App.factory('$theme', ['$window', '$rootScope', function ($window, $rootScope) {



    // WEB UI Theme version and config
    var uiConfig = {};
    function getUiConfig() {
        return uiConfig;
    }
    function initUiThemeVersion() {
        var url = "server/persistence-web-ui.json";
        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            async: false,
            success: function (response) {
                uiConfig.themeName = response.themeName;
            }
        });
    }

    return {
        initUiThemeVersion: initUiThemeVersion,
        getUiConfig: getUiConfig
    }

}])

App.run(["$rootScope", "$state", "$stateParams", '$window', '$templateCache', '$theme', '$http',  function ($rootScope, $state, $stateParams, $window, $templateCache, $theme, $http) {
    // Set reference to access them from any scope
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$storage = $window.localStorage;

    // Uncomment this to disable template cache
    /*$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
        if (typeof(toState) !== 'undefined'){
          $templateCache.remove(toState.templateUrl);
        }
    });*/

    $theme.initUiThemeVersion();
    var uiConfig = $theme.getUiConfig();

    // Scope Globals
    // ----------------------------------- 
    $rootScope.app = {
      name: model,
      description: 'Angular Bootstrap Admin Template',
      year: ((new Date()).getFullYear()),
      layout: {
          isFixed: true,
          isCollapsed: false,
          isBoxed: false,
          isRTL: false,
          horizontal: false,
          isFloat: false,
          asideHover: false,
          theme: uiConfig.themeName
      },
      useFullLayout: false,
      hiddenFooter: false,
      viewAnimation: 'ng-fadeInUp'
    };
    $rootScope.user = {
        userId: '',
        loginName: '',
        nickName:'',
        email: '',
        description: '',
        picture: 'app/img/user/02.jpg',
        roles: [],
        privileges: []
    };


    var userUrl = Ics.getFullUrl("/user/getConcurrentUserInfo");
    //同步获取登录用户信息
    $.ajax({
        type: "GET",
        url: userUrl,
        dataType: "json",
        async: false,
        success: function (data) {
            $rootScope.user.userId = data.userId;
            $rootScope.user.loginName = data.loginName;
            $rootScope.user.nickName = data.nickName;
            $rootScope.user.email = data.email;
            $rootScope.user.roles = data.roles;
            $rootScope.user.privileges = data.privileges;
            $rootScope.$broadcast('permissionsChanged');
        }
    });

    $rootScope.logout = function () {
        delete $rootScope.user;
        //delete $rootScope.authToken;
        $cookieStore.remove('authToken');
        $location.path("/login");
    };


    // 禁用右键
    Ics.doProhibit();
}]);


App.factory('permissions', function ($rootScope) {
    return {
        hasPermission: function (permission) {
            permission = permission.trim();
            var permissions = permission.split(',');

            if (permissions.length == 0) {
                return true;
            }

            var bNotHasPermission = false;
            var findHasPermission = false;

            var find = false;
            var ret = false;

            for (var i = 0; i < permissions.length; i++) {
                var currentPermission = permissions[i].trim();
                var notPermissionFlag = ( currentPermission[0] === '!' );

                if (notPermissionFlag) {
                    currentPermission = currentPermission.slice(1).trim();
                    bNotHasPermission = true;
                } else {
                    findHasPermission = true;
                }


                if ((typeof $rootScope.user.privileges != "undefined") && ($rootScope.user.privileges.indexOf(currentPermission) > -1)) {
                    if (notPermissionFlag) {
                        ret = false;
                    } else {
                        ret = true;
                    }

                    find = true;
                    break;
                }
            }

            //permissions中有"!",并且当前用户权限列表中没有permissions
            if (bNotHasPermission && !find && !findHasPermission) {
                ret = true;
            }
            return ret;
        }
    }
});


App.factory('$currentLoginUser', function ($rootScope) {
    //console.log("当前用户");
    //console.log($rootScope.user);
    return $rootScope.user;
});