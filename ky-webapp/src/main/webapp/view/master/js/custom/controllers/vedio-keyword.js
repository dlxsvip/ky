App.controller('vedioKeywordCtrl', ['$scope', '$location', '$http', '$rootScope' , '$resource', 'ngTableParams', 'blockUI', 'toaster', '$modal','$stateParams','fileUploadService','$timeout','urlService','formatTime',
    function($scope, $location, $http, $rootScope, $resource, ngTableParams, blockUI, toaster, $modal, $stateParams, fileUploadService, $timeout, urlService,formatTime) {
        'use strict';

        $scope.detail = $stateParams.keywordName;
        $scope.videoId = $stateParams.videoId;

        var getUrl = urlService.getFullUrl('/keywordAnalysis/query?&keywordId=' + $stateParams.keywordId + '&videoId=' + $stateParams.videoId);

        $http.get(getUrl)
            .then(function (response) {
                var data = response.data.data;
                $scope.keyword = data.keyword;
                //document.getElementById('keywordVideo').src = data.video.remoteAddress
                $scope.remoteAddress = data.video.remoteAddress;
                var timeList = data.timeBucket;
                $scope.timeList = formatTime.formatSecondsList(timeList);

            }, function () {
                toaster.pop('error', '失败： ' + response.data.errorMsg, error_timeout);
            });


    }]);