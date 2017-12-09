App.service('urlService', ['$location', function ($location) {
    var urlService = {};

    urlService.getFullUrl = function (api) {
        return Ics.getFullUrl(api);
    };

    urlService.getHttpFullUrl = function (api) {
        return $location.protocol() + "://" + $location.host() + ":" + $location.port() + Ics.getFullUrl(api);
    };

    urlService.getLoginUrl = function () {
        return Ics.project + Ics.loginHome;
    };


    return urlService;
}]);