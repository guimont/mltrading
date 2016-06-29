'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('database', {
                parent: 'admin',
                url: '/database',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'database.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/database/database.html',
                        controller: 'DatabaseController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('database');
                        return $translate.refresh();
                    }]
                }
            });
    });
