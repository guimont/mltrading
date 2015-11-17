'use strict';

angular.module('mltradingApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


