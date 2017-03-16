package com.mltrading.ml;



import com.mltrading.config.MLProperties;
import com.mltrading.dao.Requester;
import com.mltrading.dao.mongoFile.QueryMongoRequest;
import com.mongodb.DBCursor;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.io.FileUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;


public class MLStock  implements Serializable {
    private String codif;
    private PredictionPeriodicity period;
    private RandomForestModel model;
    private MatrixValidator validator;
    private static final Logger log = LoggerFactory.getLogger(MLStock.class);

    public MLStock(String codif, PredictionPeriodicity period) {
        this.period = period;
        this.codif = codif;
        validator = new MatrixValidator();
    }

    public MatrixValidator getValidator() {
        return validator;
    }

    public void setValidator(MatrixValidator validator) {
        this.validator = validator;
    }

    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public RandomForestModel getModel() {
        return this.model;
    }

    public void setModel(RandomForestModel model) {
        this.model = model;
    }

    public void saveModelDB() {

        try {
            if (System.getProperty("os.name").contains("Windows"))
                path = "/";
            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + period.toString() + codif));
            File dir = new File(path+"model/Model" + period.toString() + codif);
            Collection<File> files = FileUtils.listFiles(dir , null, true);
            for (File f:files) {
                GridFSInputFile gfsFile = gfsModel.createFile(f);
                if (f.getPath().contains("_temporary"))
                    gfsFile.setFilename(f.getPath().split("_temporary")[0]+f.getName());
                else
                    gfsFile.setFilename(f.getPath());
                gfsFile.save();
            }
        } catch (Exception e) {
            log.error("saveModel: " + codif + e);
        }
    }


    public void saveModel() {
        this.model.save(CacheMLStock.getJavaSparkContext().sc(), path + "model/Model" + period.toString() + codif);
    }

    public static String path= MLProperties.getProperty("model.path");

    public void loadModel() {
        this.model = RandomForestModel.load(CacheMLStock.getJavaSparkContext().sc(), path + "model/Model" + period.toString() + codif);
    }


    public void load() {
        loadModel();
        validator.loadValidator(codif+"V"+period.toString());
    }

    public void save() {
        saveModel();
        validator.saveModel(codif+"V"+period.toString());
    }


    public void distibute() {
        //MongoClient mongoClient = null;
        try {
            if (!System.getProperty("os.name").contains("Windows"))
                path = "/";
            //mongoClient = new MongoClient( "172.22.30.111" , 27017 );


            GridFS gfsModel = (GridFS) Requester.sendRequest(new QueryMongoRequest("model/Model" + period.toString() + codif));


            DBCursor cursor = gfsModel.getFileList();
            while (cursor.hasNext()) {
                File dir = new File(path+"model/Model" + period.toString() + codif+"/data");
                if (!dir.exists())
                    FileUtils.forceMkdir(dir);
                File dirmeta = new File(path+"model/Model" + period.toString() + codif+"/metadata");
                if (!dirmeta.exists())
                    FileUtils.forceMkdir(dirmeta);
                GridFSDBFile f = gfsModel.findOne(cursor.next());
                f.writeTo(f.getFilename());
            }
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

    public void loadModelDB() {
        distibute();
    }
}
