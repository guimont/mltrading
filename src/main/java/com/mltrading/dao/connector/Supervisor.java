package com.mltrading.dao.connector;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import akka.japi.Function;
/**
 * Created by gmo on 08/09/2016.
 */
public class Supervisor extends UntypedActor {


    @Override
    public void onReceive(Object message) throws Throwable {

    }
}
