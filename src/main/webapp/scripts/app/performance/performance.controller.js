'use strict';

angular.module('mltradingApp')
    .controller('PerformanceController', function ($scope, $state, $stateParams, $http, PerformanceService) {

        var code = $stateParams.code; //getting fooVal
        $scope.state = $state.current
        $scope.params = $stateParams;


        $scope.showPrediction = function(codif) {
            PerformanceService.find(codif).then(function (data) {
                $scope.preds = data;
                $scope.avgD1 = data[0].mlD1.avg;
                load($scope.preds);
            });
        };

        $scope.showPrediction(code);

    });
