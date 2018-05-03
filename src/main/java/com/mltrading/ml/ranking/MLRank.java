package com.mltrading.ml.ranking;

import com.mltrading.config.MLProperties;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.Requester;
import com.mltrading.dao.mongoFile.MongoUtil;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLStatus;
import com.mltrading.ml.MLStock;
import com.mltrading.ml.MatrixValidator;
import com.mongodb.gridfs.GridFS;

import org.apache.commons.io.FileUtils;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.io.File;
import java.io.IOException;


public class MLRank implements Serializable {

    private RandomForestModel model;
    private boolean modelImprove;
    private static final Logger log = LoggerFactory.getLogger(MLRank.class);

    private MLStatus status;

    public static String path = MLProperties.getProperty("model.path");

    public MLRank() {
        status = new MLStatus();
    }


    public RandomForestModel getModel() {
        return model;
    }

    public void setModel(RandomForestModel model) {
        this.model = model;
    }

    public boolean isModelImprove() {
        return modelImprove;
    }

    public void setModelImprove(boolean modelImprove) {
        this.modelImprove = modelImprove;
    }


    public MLStatus getStatus() {
        return status;
    }

    public void setStatus(MLStatus status) {
        this.status = status;
    }


    /**
     * load spark ml model form filesystem
     */
    public void loadModel() {
        distibute();
        try {
            this.model = RandomForestModel.load(CacheMLStock.getJavaSparkContext().sc(), path + "model/ModelRanking");
        } catch (Exception e) {
            log.error("Cannot load model: ModelRanking "+ e);
        }
    }

    /**
     * distibute physically model form mongoDB on file system
     */
    public void distibute() {
        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/ModelRanking"));
            File dir = new File(path + "model/ModelRanking"+"/data");
            File dirmeta = new File(path+"model/ModelRanking"+"/metadata");
            MongoUtil.distribute(gfsModel, dir, dirmeta);
        } catch (Exception e) {
            log.error("saveModel: ModelRanking "+ e);
        }
    }



    /**
     * save model, spark model on file system
     */
    public void saveModel() {
        this.model.save(CacheMLStock.getJavaSparkContext().sc(), path + "model/ModelRanking");
    }

    /**
     * save mllib model on mongoDB
     */
    public void saveModelDB() {

        removeModelDB();

        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/ModelRanking"));

            File dir = new File(path + "model/ModelRanking");
            MongoUtil.saveDirectory(gfsModel, dir);
        } catch (Exception e) {
            log.error("saveModel: ModelRanking " + e);
        }

    }

    public static void deleteModel() {
        String path = MLStock.path;

        try {
            FileUtils.deleteDirectory(new File(path + "model"));
        } catch (IOException e) {
            log.error("Cannot remove folder model: " + e);
        }
    }

    /**
     * remove model form mongoDB on file system
     */
    public void removeModelDB() {
        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/ModelRanking"));

            MongoUtil.removeDB(gfsModel);
        } catch (Exception e) {
            log.error("remove: ModelRanking " + e);
        }
    }

}
