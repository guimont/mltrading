package com.mltrading.dao.connector;
import akka.actor.ActorRef;
import akka.actor.Props;

import akka.routing.SmallestMailboxPool;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mltrading.influxdb.dto.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gmo on 08/09/2016.
 */
public class MainConnector extends Supervisor{
    private final ActorRef worker,workerMongo ;
    private static final Logger log = LoggerFactory.getLogger(MainConnector.class);


    /**
     * Create Main connector
     * Create worker with WORKER_NB routes (5 by default)
     */
    public MainConnector() {
        //create the worker dispatcher
        int WORKER_NB = 5;
        worker = this.getContext().actorOf(new SmallestMailboxPool(WORKER_NB).props(Props.create(WorkerConnector.class)), "workerRouter");
        workerMongo = this.getContext().actorOf(new SmallestMailboxPool(WORKER_NB).props(Props.create(WorkerMongoConnector.class)), "workerMongoRouter");

    }

    /**
     * flow management
     * @param message: message to dispatch
     */
    @Override
    public void onReceive(Object message) {
        if(message instanceof Start){
            log.debug("Start akka main connector for "+self().path().name());
        }
        else if (message instanceof QueryRequest) {
            worker.tell(message, getSender());
        }
        else if (message instanceof QueryMongoRequest) {
            workerMongo.tell(message, getSender());
        }
        else  {
            log.debug("Akka main connector message unknown");
        }

    }

    /**
     * class to start dispatcher
     */
    public static class Start{}
}
