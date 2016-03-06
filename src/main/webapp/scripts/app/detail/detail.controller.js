/**
 * Created by gmo on 05/03/2016.
 */

'use strict';

angular.module('mltradingApp')
    .controller('DetailController', function ($scope, $state, $stateParams, $http, DetailService) {

        var code = $stateParams.code; //getting fooVal
        $scope.state = $state.current
        $scope.params = $stateParams;

        $scope.showDetail = function(code) {
            DetailService.find(code).then(function (data) {
                $scope.detail = data;
            });
        };

        $scope.showDetail(code);

    });
