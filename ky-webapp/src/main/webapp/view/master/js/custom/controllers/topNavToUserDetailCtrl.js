/**
 * Created by dtdream-chenyayun on 2017/8/17.
 */
App.controller('topNavToUserDetailCtrl', ['$scope' ,'$localStorage', '$location',function($scope, $localStorage, $location){
    $scope.toUserDetail = function(){
        if($location.path() != '/app/user-info/base'){
            $localStorage.icsUserPageFrom = {'from':$location.path()};
        }
    };
}])
