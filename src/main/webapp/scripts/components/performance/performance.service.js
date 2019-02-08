'use strict';

angular.module('mltradingApp')
    .factory('PerformanceService', function ($http) {
        return {
            getPerformance: function (code,model) {
                return $http.get('api/ml/getPerformance?key='+code+'&model='+model).then(function (response) {
                    return response.data;
                });
            },

            getPerformanceShort: function (code,model) {
                return $http.get('api/ml/getPerformanceShort?key='+code+'&model='+model).then(function (response) {
                    return response.data;
                });
            },

            getValidator: function (code) {
                return $http.get('api/ml/getValidator?key='+code).then(function (response) {
                    return response.data;
                });
            }
        };
    });
