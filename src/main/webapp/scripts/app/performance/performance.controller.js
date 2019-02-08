'use strict';

angular.module('mltradingApp')
    .controller('PerformanceController', function ($scope, $state, $stateParams, $http, PerformanceService) {

        var code = $stateParams.code; //getting fooVal
        $scope.state = $state.current
        $scope.params = $stateParams;


        $scope.showPrediction = function(codif) {
            PerformanceService.getPerformance(codif, "RANDOMFOREST").then(function (data) {
                $scope.perfs =  data
                load($scope.perfs,'kinetic');
            });
        };

        $scope.showPredictionGBT = function(codif) {
            PerformanceService.getPerformance(codif,"GRADIANTBOOSTTREE").then(function (data) {
                $scope.perfsGBT =  data
                loadGBT($scope.perfsGBT, 'kinetic');
            });
        };

        $scope.showPredictionEnsemble = function(codif) {
                    PerformanceService.getPerformance(codif,"ENSEMBLE").then(function (data) {
                        $scope.perfsEnsemble =  data
                        loadEnsemble($scope.perfsEnsemble, 'kinetic');
                    });
                };


        $scope.showPredictionShort = function(codif) {
            PerformanceService.getPerformanceShort(codif, "RANDOMFOREST").then(function (data) {
                $scope.perfsShort =  data
                load($scope.perfsShort,'kineticShort');
            });
        };

        $scope.showPredictionShortGBT = function(codif) {
            PerformanceService.getPerformanceShort(codif,"GRADIANTBOOSTTREE").then(function (data) {
                $scope.perfsShortGBT =  data
                loadGBT($scope.perfsShortGBT,'kineticShort');
            });
        };

        $scope.showPredictionShortEnsemble = function(codif) {
            PerformanceService.getPerformanceShort(codif,"ENSEMBLE").then(function (data) {
                $scope.perfsShortEnsemble =  data
                loadEnsemble($scope.perfsShortEnsemble,'kineticShort');
            });
        };


        $scope.showPrediction( code);
        $scope.showPredictionGBT( code);
        $scope.showPredictionEnsemble( code);

        $scope.showPredictionShort( code);
        $scope.showPredictionShortGBT( code);
        $scope.showPredictionShortEnsemble( code);


        $scope.showValidator = function(codif) {
            PerformanceService.getValidator(codif).then(function (data) {
                $scope.validator = data;
                loadValidator($scope.validator);
            });
        };

        $scope.showValidator(code);

    });
