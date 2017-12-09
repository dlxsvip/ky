App.controller('vedioKeyPersonCtrl', ['$scope', '$location', '$http', '$rootScope' , '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal','$stateParams','fileUploadService','$timeout','urlService','formatTime',
    function($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, $stateParams, fileUploadService, $timeout, urlService,formatTime) {
        'use strict';

        $scope.videoId = $stateParams.videoId;
        $scope.detail = $stateParams.personName;
        var getUrl = urlService.getFullUrl('/keyPersonAnalysis/query?&personId=' + $stateParams.personId + '&videoId=' + $stateParams.videoId);

        $http.get(getUrl)
            .then(function (response) {
                var data = response.data.data;
                $scope.person = data.person;
                $scope.person.sex = 'MALE' == $scope.person.sex?'男':'女';
                $scope.remoteAddress = data.video.remoteAddress;
                var timeList = data.timeBucket;
                $scope.timeList = formatTime.formatSecondsList(timeList);

            }, function () {
                toaster.pop('error', '失败： ' + response.data.errorMsg, error_timeout);
            });


    }]);