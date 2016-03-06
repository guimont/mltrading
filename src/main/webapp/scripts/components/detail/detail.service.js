/**
 * Created by gmo on 05/03/2016.
 */
'use strict';

angular.module('mltradingApp')
    .factory('DetailService', function ($http) {
        return {
            find: function (code) {
                return $http.get('api/rt/detail?key='+code).then(function (response) {
                    return response.data;
                });
            }
        };
    });
