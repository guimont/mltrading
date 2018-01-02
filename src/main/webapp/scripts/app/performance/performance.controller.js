'use strict';

angular.module('mltradingApp')
    .controller('PerformanceController', function ($scope, $state, $stateParams, $http, PerformanceService) {

        var code = $stateParams.code; //getting fooVal
        $scope.state = $state.current
        $scope.params = $stateParams;


        $scope.showPrediction = function(codif) {
            PerformanceService.getPerformance(codif, "RANDOMFOREST").then(function (data) {
                $scope.perfs =  data
                load($scope.perfs);
            });
        };

        $scope.showPredictionGBT = function(codif) {
            PerformanceService.getPerformance(codif,"GRADIANTBOOSTTREE").then(function (data) {
                $scope.perfsGBT =  data
                loadGBT($scope.perfsGBT);
            });
        };


        $scope.showPrediction( code);
        $scope.showPredictionGBT( code);


        $scope.showValidator = function(codif) {
            PerformanceService.getValidator(codif).then(function (data) {
                $scope.validator = data;
                loadValidator($scope.validator);
            });
        };

        $scope.showValidator(code);

    });
