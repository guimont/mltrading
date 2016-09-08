package com.mltrading.dao.connector;

import akka.actor.ActorRef;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import akka.actor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gmo on 08/09/2016.
 */
public class Dispatcher {
    private final ActorSystem system;
    private Map<String, ActorRef> dispatchers = new TreeMap<>();
    public static final String BASIC_DISPATCHER = "mltrading";

    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * Create ActorRef
     * Start bus message
     */
    private Dispatcher() {
        // declare master
        system = ActorSystem.create("mySystem");
        startDispatcher(BASIC_DISPATCHER);
    }



    private void startDispatcher(String name) {
        ActorRef dispatcher = system.actorOf(Props.create(MainConnector.class), name);
        MainConnector.Start startMessage =  new MainConnector.Start();
        dispatcher.tell(startMessage,null);
        dispatchers.put(name, dispatcher);
    }

    private static class DispatcherHolder {
        /** Instance unique non pre-initialise */
        private final static Dispatcher instance = new Dispatcher();
    }


    /** Singleton access point */
    public static ActorRef getDispatcher() {
        if (null == DispatcherHolder.instance)
            return null;

        ActorRef actor = DispatcherHolder.instance.dispatchers.get(BASIC_DISPATCHER);

        return actor;
    }


    /**
     * Send stop message on bus to stop all actors clients
     */
    public static void endDispatcher() {
        log.info("Stop dispatchers and close all ES connections");
        for (ActorRef dispatcher : DispatcherHolder.instance.dispatchers.values()) {
            dispatcher.tell(akka.actor.PoisonPill.getInstance(),null);
        }
    }


}
