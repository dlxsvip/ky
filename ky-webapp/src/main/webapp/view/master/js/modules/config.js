/**=========================================================
 * Module: config.js
 * App routes and resources configuration
 =========================================================*/

App.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', 'RouteHelpersProvider',
    function ($stateProvider, $locationProvider, $urlRouterProvider, helper) {
        'use strict';

        // Set the following to true to enable the HTML5 Mode
        // You may have to set <base> tag in index and a routing configuration in your server
        $locationProvider.html5Mode(false);

        // default route
        $urlRouterProvider.otherwise('/app/waiting');

        //
        // Application Routes
        // -----------------------------------
        $stateProvider
            .state('app', {
                url: '/app',
                abstract: true,
                templateUrl: helper.basepath('app.html'),
                controller: 'AppController',
                resolve: helper.resolveFor('modernizr', 'icons')
            })
            .state('app.waiting', {
                url: '/waiting',
                templateUrl: helper.basepath('waiting.html'),
                controller: 'waitingController'
            })
            .state('app.realtime-data', {
                url: '/realtime-data',
                title: '实时数据',
                templateUrl: helper.basepath('realtime-data.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'classyloader', 'blockUI', 'toaster', 'ngWebSocket')
            })
            .state('app.keyword-recognition', {
                url: '/keyword-recognition',
                title: 'GJC分析',
                templateUrl: helper.basepath('keyword-recognition.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.keyPerson-recognition', {
                url: '/keyPerson-recognition',
                title: 'GJR分析',
                templateUrl: helper.basepath('keyPerson-recognition.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.keyword-manage', {
                url: '/keyword-manage',
                title: 'GJC管理',
                templateUrl: helper.basepath('key-word.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.key-person', {
                url: '/key-person',
                title: 'GJR管理',
                templateUrl: helper.basepath('key-person.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.role-manage', {
                url: '/role-manage',
                title: '角色管理',
                templateUrl: helper.basepath('role-manage.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.role-privilege', {
                url: '/role-privilege',
                title: '权限管理',
                templateUrl: helper.basepath('role-privilege.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.user-manage', {
                url: '/user-manage',
                title: '用户管理',
                templateUrl: helper.basepath('user-manage.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.user-info', {
                url: '/user-info?userId&nickName',
                title: '用户信息',
                templateUrl: helper.basepath('user-info.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.user-info.base', {
                url: '/base',
                title: '用户信息-基本信息',
                templateUrl: helper.basepath('user-info-base.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.user-info.updatepwd', {
                url: '/updatepwd',
                title: '用户信息-修改密码',
                templateUrl: helper.basepath('user-info-updatepwd.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster', 'crypto-js')
            })
            .state('app.user-info.resetpwd', {
                url: '/resetpwd',
                title: '用户信息-重置密码',
                templateUrl: helper.basepath('user-info-resetpwd.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster', 'crypto-js')
            })
            .state('app.system-config', {
                url: '/system-config',
                title: '系统配置',
                templateUrl: helper.basepath('system-config/system-config.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.system-config.store', {
                url: '/store',
                title: 'OSS配置',
                templateUrl: helper.basepath('system-config/system-config-store.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.system-config.parameter', {
                url: '/parameter',
                title: '参数配置',
                templateUrl: helper.basepath('system-config/system-config-parameter.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.system-config.notice', {
                url: '/notice',
                title: '通知配置',
                templateUrl: helper.basepath('system-config/system-config-notice.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.collection-service', {
                url: '/collection-service',
                title: 'CJFW管理',
                templateUrl: helper.basepath('collection-service.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')
            })
            .state('app.channel-manage', {
                url: '/channel-manage?hostId?hostName',
                title: 'PD管理',
                templateUrl: helper.basepath('channel-manage.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster'),
                controller: 'channelManageCtrl'
            })
            .state('app.video-keyword', {
                url: '/video-keyword?keywordId?videoId?keywordName',
                title: 'GJC分析详情',
                templateUrl: helper.basepath('video-keyword.html'),
                controller: 'vedioKeywordCtrl',
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')

            })
            .state('app.test', {
                url: '/test',
                title: 'GJC分析详情',
                templateUrl: helper.basepath('test.html'),
                controller: 'testCtrl',
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')

            })
            .state('app.video-keyPerson', {
                url: '/video-keyPerson?personId?videoId?personName',
                title: 'GJR分析详情',
                templateUrl: helper.basepath('video-keyPerson.html'),
                controller: 'vedioKeyPersonCtrl',
                resolve: helper.resolveFor('ngTable', 'ngTableExport', 'datatables', 'datatables-pugins', 'ngDialog', 'ui.select', 'classyloader', 'blockUI', 'jquery-dt-submenu', 'toaster')

            })

            //
            // CUSTOM RESOLVES
            //   Add your own resolves properties
            //   following this object extend
            //   method
            // -----------------------------------
            // .state('app.someroute', {
            //   url: '/some_url',
            //   templateUrl: 'path_to_template.html',
            //   controller: 'someController',
            //   resolve: angular.extend(
            //     helper.resolveFor(), {
            //     // YOUR RESOLVES GO HERE
            //     }
            //   )
            // })
        ;


    }]).config(['$ocLazyLoadProvider', 'APP_REQUIRES', function ($ocLazyLoadProvider, APP_REQUIRES) {
    'use strict';

    // Lazy Load modules configuration
    $ocLazyLoadProvider.config({
        debug: false,
        events: true,
        modules: APP_REQUIRES.modules
    });

}]).config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ($controllerProvider, $compileProvider, $filterProvider, $provide) {
        'use strict';
        // registering components after bootstrap
        App.controller = $controllerProvider.register;
        App.directive = $compileProvider.directive;
        App.filter = $filterProvider.register;
        App.factory = $provide.factory;
        App.service = $provide.service;
        App.constant = $provide.constant;
        App.value = $provide.value;

    }]).config(['$translateProvider', function ($translateProvider) {

    $translateProvider.useStaticFilesLoader({
        prefix: 'app/i18n/',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('en');
    $translateProvider.useLocalStorage();
    $translateProvider.usePostCompiling(true);

}]).config(['cfpLoadingBarProvider', function (cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeBar = true;
    cfpLoadingBarProvider.includeSpinner = false;
    cfpLoadingBarProvider.latencyThreshold = 500;
    cfpLoadingBarProvider.parentSelector = '.wrapper > section';
}]).config(['$tooltipProvider', function ($tooltipProvider) {

    $tooltipProvider.options({appendToBody: true});

}]).config(['$logProvider', '$httpProvider', '$stateProvider', '$urlRouterProvider', function ($logProvider, $httpProvider, $stateProvider, $urlRouterProvider) {
    // 登录超时拦截器
    $httpProvider.interceptors.push(['$rootScope', '$q', '$location', '$timeout', 'urlService',
        function ($rootScope, $q, $location, $timeout, urlService) {
            return {
                'request': function (config) {
                    config.headers['X-Requested-With'] = 'XMLHttpRequest';
                    return config || $q.when(config);
                },
                'requestError': function (rejection) {
                    return rejection;
                },
                'response': function (response) {
                    return response || $q.when(response);
                },
                'responseError': function (response) {
                    console.log('responseError:' + response);
                    if (response.status === 401 || response.status === 403) {
                        //$timeout(function () {
                        window.location = urlService.getLoginUrl();
                        //也可弹出对话框提示
                        //}, 1000);
                        return false;
                    }
                    else if (response.status === 500) {
                        $location.path('/500.html');
                        return false;
                    }
                    else if (response.status === 600) {
                        // 用户退出，跳转到登录页面
                        window.location = urlService.getLoginUrl();
                        return false;
                    }
                    return $q.reject(response);
                }
            };
        }
    ]);
}])
;
