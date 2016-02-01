'use strict';

angular.module('mltradingApp')
    .controller('PerformanceController', function ($scope, $http, PerformanceService) {

        $scope.onChangeDate = function () {
            var dateFormat = 'yyyy-MM-dd';


            RealtimeService.findAll().then(function (data) {
                $scope.rts = data;
            });
        };

        $scope.onChangeDate();


        $scope.showPrediction = function(codif) {
            $('#showHealthModal').modal('show');
        };



    });
