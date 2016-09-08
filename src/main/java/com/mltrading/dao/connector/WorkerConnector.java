package com.mltrading.dao.connector;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gmo on 08/09/2016.
 */
public class WorkerConnector extends Supervisor{

    private InfluxDaoConnector connector;
    private static final Logger log = LoggerFactory.getLogger(MainConnector.class);

    /**
     * create elasticsearch connection
     */
    public WorkerConnector() {
        log.debug("Start akka reader connector");
        connector = new InfluxDaoConnector();
    }

    /**
     * Stop actor and close elasticsearch connection
     */
    @Override
    public void postStop(){
        log.debug("Stop akka reader connector");
        this.connector.close();
    }

    /**
     * flow management
     * @param message: ESRequest to dispatch
     */
    @Override
    public void onReceive(Object message) {
        if(message instanceof QueryRequest){
            log.debug("Processing reader message");

            getSender().tell(connector.getAkkaPoints((QueryRequest)message),self());
        }
    }
}
