'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('extraction', {
                parent: 'admin',
                url: '/extraction',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'extraction.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/extraction/extraction.html',
                        controller: 'ExtractionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('extraction');
                        return $translate.refresh();
                    }]
                }
            });
    });
