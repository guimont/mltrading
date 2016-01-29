'use strict';

angular.module('mltradingApp')
    .controller('RealtimeController', function ($scope, $http, RealtimeService) {

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
