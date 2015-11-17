'use strict';

angular.module('mltradingApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
