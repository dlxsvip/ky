App.controller('cloudResourceHeadReturnCtl', ['$scope', '$window',
    function($scope, $window ) {
        $scope.goBack = function() {
            $window.history.back();
        };
    }]);