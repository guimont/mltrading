'use strict';

angular.module('mltradingApp')
    .factory('RealtimeService', function ($http) {
        return {
            findAll: function () {
                return $http.get('api/rt/all').then(function (response) {
                    return response.data;
                });
            }
        };
    });
