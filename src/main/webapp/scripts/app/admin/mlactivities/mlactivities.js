'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mlactivities', {
                parent: 'admin',
                url: '/mlactivities',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'mlactivities.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/mlactivities/mlactivities.html',
                        controller: 'MLActivitiesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mlactivities');
                        return $translate.refresh();
                    }]
                }
            });
    });
