'use strict';

angular.module('mltradingApp')
    .factory('PerformanceService', function ($http) {
        return {
            find: function (code) {
                return $http.get('api//ml/stat?key='+code).then(function (response) {
                    return response.data;
                });
            }
        };
    });
