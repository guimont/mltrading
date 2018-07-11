package com.mltrading.dao.dispatcherPool;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mltrading.influxdb.dto.QueryRequest;
import com.mltrading.ml.util.FixedThreadPoolExecutor;

public class DispatcherPool {

    FixedThreadPoolExecutor executorInflux = new FixedThreadPoolExecutor(10,
            "InfluxDaoPool");


    FixedThreadPoolExecutor executorMongo = new FixedThreadPoolExecutor(10,
        "MongoDaoPool");





    public Object dispatch(Object request) {

        final Object[] reponse = new Object[1];

        if (request instanceof QueryRequest)
            return executorInflux.submit(new Runnable() {
                final WorkerInfluxPool workerInfluxPool = new WorkerInfluxPool();
                public void run() {
                    reponse[0] =  workerInfluxPool.process((QueryRequest) request);
                }
        });


        /*if (request instanceof QueryMongoRequest)
            return connector.createGridFS((QueryMongoRequest) request);*/


        return reponse[0];

    }


}
