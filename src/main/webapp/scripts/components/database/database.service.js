'use strict';

angular.module('mltradingApp')
    .factory('DatabaseService', function ($http) {
        return {
            findAll: function () {
                return $http.get('api/database/all').then(function (response) {
                    return response.data;
                });
            }
        };
    });
