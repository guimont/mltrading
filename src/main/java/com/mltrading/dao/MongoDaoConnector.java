package com.mltrading.dao;

import com.mltrading.config.MLProperties;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by gmo on 14/02/2017.
 */
public class MongoDaoConnector {

    MongoClient mongoClient = null;
    private static final Logger log = LoggerFactory.getLogger(MongoDaoConnector.class);

    public MongoDaoConnector(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }


    /**
     * create
     */
    public MongoDaoConnector() {
        try {
            String uri = MLProperties.getProperty("mongoDB");
            Integer port = new Integer(MLProperties.getProperty("mongoDBPort"));
            mongoClient = new MongoClient( uri , port );
        } catch (UnknownHostException e) {
            log.error("MongoDaoConnector error:" + e);
        }
    }


    /**
     * close current connection
     */
    public void close() {
        if (mongoClient != null)
            mongoClient.close();
    }

    public GridFS createGridFS(QueryMongoRequest request) {
        GridFS gfsModel = new GridFS(mongoClient.getDB("model"), request.getPath());
        return gfsModel;
    }

}
