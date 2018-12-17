'use strict';

angular.module('mltradingApp')
    .controller('AssetController', function ($scope, $state, $stateParams, $http, AssetService) {


        $scope.state = $state.current
        $scope.params = $stateParams;

        $scope.$doInit = false;


        $scope.loadAsset = function() {
            AssetService.getAsset().then(function (data) {

                    $scope.asset = data
                    loadAssetHistory($scope.asset);

            });
        };


        $scope.showAsset = function() {
            loadAssetHistory($scope.asset);
        }


        setTimeout(function(){ $scope.showAsset() }, 200);
        $scope.loadAsset();


    })


;
