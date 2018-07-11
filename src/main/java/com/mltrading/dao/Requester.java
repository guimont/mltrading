package com.mltrading.dao;

import akka.pattern.Patterns;
import akka.util.Timeout;
import com.mltrading.dao.connector.Dispatcher;

import com.mltrading.dao.dispatcherPool.DispatcherPool;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mltrading.influxdb.dto.QueryRequest;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * Created by gmo on 08/09/2016.
 */
public class Requester {

    final static Timeout timeout = new Timeout((FiniteDuration) Duration.apply("120 seconds"));
    final static MongoDaoConnector connector = new MongoDaoConnector();

    static DispatcherPool dispatcherPool = new DispatcherPool();

    static public Object sendRequest(Object request) {

        /*if (request instanceof QueryRequest)
            return dispatcherPool.dispatch(request);*/

        if (request instanceof QueryRequest)
            return InfluxDaoConnector.getPoints(((QueryRequest)request).getQuery(),((QueryRequest)request).getName());

        if (request instanceof QueryMongoRequest)
            return connector.createGridFS((QueryMongoRequest) request);

        return null;

        /**
         *probleme with akka dont use it
         *
        Future<?> future = Patterns.ask(Dispatcher.getDispatcher(), request, timeout);
        try {
            return Await.result(future, timeout.duration());
        } catch (Exception e) {
            return null;
        }*/
    }



}
