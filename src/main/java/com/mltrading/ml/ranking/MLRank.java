package com.mltrading.ml.ranking;

import com.mltrading.config.MLProperties;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.Requester;
import com.mltrading.dao.mongoFile.MongoUtil;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mltrading.ml.*;
import com.mltrading.ml.model.Model;
import com.mltrading.ml.model.ModelType;
import com.mongodb.gridfs.GridFS;

import org.apache.commons.io.FileUtils;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class MLRank implements Serializable {

    private MLModel model;
    private boolean modelImprove;
    private static final Logger log = LoggerFactory.getLogger(MLRank.class);
    double ratio = 1.;

    private HashMap<ModelType,MLStatus> statusMap = new HashMap<>();

    public MLStatus getStatus(ModelType type) {
        return statusMap.get(type);
    }

    public static String path = MLProperties.getProperty("model.path");

    public MLRank() {
        MLStatus statusRF = new MLStatus();
        statusMap.put(ModelType.RANDOMFOREST, statusRF);
        MLStatus statusGBT = new MLStatus();
        statusMap.put(ModelType.GRADIANTBOOSTTREE, statusGBT);
        MLStatus statusENSEMBLE = new MLStatus();
        statusMap.put(ModelType.ENSEMBLE, statusENSEMBLE);
        model = new MLModel();
    }


    public MLModel getModel() {
        return model;
    }

    public Model getModel(ModelType type) {
        return this.model.getModel(type);
    }

    public void setModel(Model model, ModelType type) {
        this.model.setModel(model, type);
    }



    public boolean isModelImprove() {
        return modelImprove;
    }

    public void setModelImprove(boolean modelImprove) {
        this.modelImprove = modelImprove;
    }


    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setStatus(MLStatus status, ModelType type) {
        this.statusMap.put(type,status);
    }

    /**
     * load spark ml model form filesystem
     */
    public void loadModel(ModelType type) {
        distibute(type);
        try {
        this.model.load(path, PredictionPeriodicity.D20, "Ranking", type, CacheMLStock.NO_EXTENDED);
        } catch (Exception e) {
            log.error("Cannot load model: ModelRanking "+ e);
        }
    }


    /**
     * distibute physically model form mongoDB on file system
     */
    public void distibute(ModelType type) {
        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + ModelType.code(type) + PredictionPeriodicity.D20 + "Ranking" +  CacheMLStock.NO_EXTENDED));
            File dir = new File(path+"model/Model" + ModelType.code(type) + PredictionPeriodicity.D20 + "Ranking" +  CacheMLStock.NO_EXTENDED +"/data");
            File dirmeta = new File(path+"model/Model" + ModelType.code(type) + PredictionPeriodicity.D20 + "Ranking" +  CacheMLStock.NO_EXTENDED +"/metadata");
            MongoUtil.distribute(gfsModel, dir, dirmeta);
        } catch (Exception e) {
            log.error("distribute: " + "Ranking" + e);
        }
    }



    /**
     * save model, spark model on file system
     */
    public void saveModel(ModelType type) {
        this.model.save(type ,path, PredictionPeriodicity.D20, "Ranking", CacheMLStock.NO_EXTENDED);
    }



    /**
     * save mllib model on mongoDB
     */
    public void saveModelDB(ModelType type) {

        removeModelDB(type);

        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + ModelType.code(type) + PredictionPeriodicity.D20 + "Ranking" +  CacheMLStock.NO_EXTENDED));
            File dir = new File(path+"model/Model" + ModelType.code(type) + PredictionPeriodicity.D20 + "Ranking" +  CacheMLStock.NO_EXTENDED +"/data");

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
    public void removeModelDB(ModelType type) {
        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + ModelType.code(type) + PredictionPeriodicity.D20.toString() + "Ranking" + CacheMLStock.NO_EXTENDED));

            MongoUtil.removeDB(gfsModel);
        } catch (Exception e) {
            log.error("remove: " + "Ranking" + e);
        }
    }

}
