package com.mltrading.dao;

import akka.pattern.Patterns;
import akka.util.Timeout;
import com.mltrading.dao.connector.Dispatcher;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * Created by gmo on 08/09/2016.
 */
public class Requester {

    final static Timeout timeout = new Timeout((FiniteDuration) Duration.apply("30 seconds"));

    static public Object sendRequest(Object request) {
        Future<?> future = Patterns.ask(Dispatcher.getDispatcher(), request, timeout);
        try {
            return Await.result(future, timeout.duration());
        } catch (Exception e) {
            return null;
        }
    }
}
