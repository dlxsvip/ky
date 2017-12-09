App.service('urlWs', ['$location', function ($location) {
    var urlWs = {};

    urlWs.getWsUrl = function () {
        return 'ws://' + $location.host() + ":" + Ics.wsPort + "/websocket";
    };


    // 测试地址
    urlWs.getWsUrlTest = function () {
        return 'ws://10.160.1.145:7080/websocket';
    };

    return urlWs;
}]);