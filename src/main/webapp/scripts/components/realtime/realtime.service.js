'use strict';

angular.module('mltradingApp')
    .factory('RealtimeService', function ($rootScope, $cookies, $http, $q) {
        var stompClient = null;
        var subscriber = null;
        var listener = $q.defer();
        var connected = $q.defer();
        var alreadyConnectedOnce = false;
        function sendActivity() {
            if (stompClient != null && stompClient.connected) {
                stompClient
                    .send('/topic/rtevent',
                    {},
                    JSON.stringify({'page': $rootScope.toState.name}));
            }
        }
        return {
            findAll: function () {
                return $http.get('api/rt/all').then(function (response) {
                    return response.data;
                });

            },

            findSector: function () {
                return $http.get('api/rt/sector').then(function (response) {
                    return response.data;
                });
            },

            findIndice: function () {
                return $http.get('api/rt/px1').then(function (response) {
                    return response.data;
                });
            },
            connect: function () {
                //building absolute path so that websocket doesnt fail when deploying with a context path
                var loc = window.location;
                var url = '//' + loc.host + loc.pathname + 'websocket/sectors';
                var socket = new SockJS(url);
                stompClient = Stomp.over(socket);
                var headers = {};
                headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
                stompClient.connect(headers, function(frame) {
                    connected.resolve("success");
                    sendActivity();
                    if (!alreadyConnectedOnce) {
                        $rootScope.$on('$stateChangeStart', function (event) {
                            sendActivity();
                        });
                        alreadyConnectedOnce = true;
                    }
                });
            },
            subscribe: function() {
                connected.promise.then(function() {
                    subscriber = stompClient.subscribe("/topic/sectors", function(data) {
                        listener.notify(JSON.parse(data.body));
                    });
                }, null, null);
            },
            unsubscribe: function() {
                if (subscriber != null) {
                    subscriber.unsubscribe();
                }
            },
            receive: function() {
                return listener.promise;
            },
            sendActivity: function () {
                if (stompClient != null) {
                    sendActivity();
                }
            },
            disconnect: function() {
                if (stompClient != null) {
                    stompClient.disconnect();
                    stompClient = null;
                }
            }
        };
    });

