package com.mltrading.ml;



import com.mltrading.config.MLProperties;
import com.mltrading.dao.Requester;
import com.mltrading.dao.mongoFile.MongoUtil;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mltrading.ml.model.Model;
import com.mltrading.ml.model.ModelType;
import com.mongodb.DBCursor;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.io.FileUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;


/**
 * Machine learning stock
 * class to define object for ml
 */
public class MLStock  implements Serializable {
    private String codif;
    private PredictionPeriodicity period;
    private MLModel model;
    private  String modelExtendedPrefix;

    private boolean modelImprove;
    private static final Logger log = LoggerFactory.getLogger(MLStock.class);

    public MLStock(String codif, PredictionPeriodicity period,  String modelExtendedPrefix) {
        this.period = period;
        this.codif = codif;
        this.modelExtendedPrefix = modelExtendedPrefix;

        model = new MLModel();
    }

    /**
     * check is improve model to know if model could be save
     * save is a long process so have to optimize it
     * @return
     */
    public boolean isModelImprove() {
        return modelImprove;
    }

    public void setModelImprove(boolean modelImprove) {
        this.modelImprove = modelImprove;
    }



    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public Model getModel(ModelType type) {
        return this.model.getModel(type);
    }


    public MLModel getModel() {
        return this.model;
    }

    public void setModel(Model model, ModelType type) {
        this.model.setModel(model, type);
    }


    /**
     * remove model form mongoDB on file system
     */
    public void removeModelDB(ModelType type) {
        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + ModelType.code(type) + period.toString() + codif + modelExtendedPrefix));

            MongoUtil.removeDB(gfsModel);
        } catch (Exception e) {
            log.error("remove: " + codif + e);
        }
    }


    /**
     * save mllib model on mongoDB
     */
    public void saveModelDB(ModelType type) {
        if (isModelImprove()) {
            removeModelDB(type);

            try {
                GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + ModelType.code(type)+ period.toString() + codif + modelExtendedPrefix));

                File dir = new File(path + "model/Model" + ModelType.code(type)+ period.toString() + codif + modelExtendedPrefix);
                MongoUtil.saveDirectory(gfsModel, dir);
            } catch (Exception e) {
                log.error("saveModel: " + codif + e);
            }
        }
    }



    public static String path= MLProperties.getProperty("model.path");

    /**
     * load spark ml model form filesystem
     */
    public void loadModel(ModelType type) {
        this.model.load(path, period, codif, type, modelExtendedPrefix);
    }


    /**
     * loader
     */
    public void load(ModelType type) {
        loadModel(type);
    }


    /**
     * save model, spark model on file system
     */
    public void saveModel(ModelType type) {
        //this.model.save(CacheMLStock.getJavaSparkContext().sc(), path + "model/Model" + period.toString() + codif);
        this.model.save(type ,path, period, codif, modelExtendedPrefix);
    }

    /**
     * save validator in influxdb modelNote database
     * fomat is 'codif''V''period'. Example ORAVD5
     * @param type
     */
    public void saveValidator(ModelType type) throws InterruptedException {
        this.model.saveModel(type, period, codif, modelExtendedPrefix);
    }


    /**
     * distibute physically model form mongoDB on file system
     */
    public void distibute(ModelType type) {
        try {
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + ModelType.code(type) + period.toString() + codif + modelExtendedPrefix));
            File dir = new File(path+"model/Model" + ModelType.code(type) + period.toString() + codif + modelExtendedPrefix +"/data");
            File dirmeta = new File(path+"model/Model" + ModelType.code(type) + period.toString() + codif + modelExtendedPrefix +"/metadata");
            MongoUtil.distribute(gfsModel, dir, dirmeta);
        } catch (Exception e) {
            log.error("saveModel: " + codif + e);
        }
    }

    public void send(JavaSparkContext sc) {
        try {
            File dir = new File(path+"model/Model" + period.toString() + codif);
            Collection<File> files = FileUtils.listFiles(dir , null, true);
            for (File f:files) {
                String pathFull = f.getPath().replaceAll("\\\\","/");
                sc.addFile(pathFull);
            }
        }catch (Exception e) {
            log.error("send: " + codif + e);
        }
    }

    public void loadModelDB(ModelType type) {
        distibute(type);
    }

    public void mergetValidator(MatrixValidator validator) {
       throw new NotImplementedException();
    }
}
