package com.mltrading.ml;

import com.mltrading.ml.model.MLGradiantBoostStockModel;
import com.mltrading.ml.model.MLRandomForestModel;
import com.mltrading.ml.model.Model;
import com.mltrading.ml.model.ModelType;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by gmo on 01/03/2017.
 */
public class MLModel {

    private static final Logger log = LoggerFactory.getLogger(MLPredictor.class);

    HashMap<ModelType,Model> modelSet = new HashMap<>();

    public MLModel() {
        this.modelSet.put(ModelType.RANDOMFOREST, new MLRandomForestModel());
        this.modelSet.put(ModelType.GRADIANTBOOSTTREE, new MLGradiantBoostStockModel());
    }

    public void setModel(Model model, ModelType type) {
        modelSet.put(type, model);
    }

    public Model getModel(ModelType type) {
        return modelSet.get(type);
    }

    public MatrixValidator getValidator(ModelType type) {
        return modelSet.get(type).getValidator();
    }

    public void setValidator(ModelType type, MatrixValidator validator) {
        modelSet.get(type).setValidator(validator);
    }



    public void load(String path, PredictionPeriodicity period, String codif, ModelType type) {
        Model model = null;
        if (type == ModelType.RANDOMFOREST)
            model = new MLRandomForestModel(path , period.toString(), codif);
        else if (type == ModelType.GRADIANTBOOSTTREE)
            model = new MLGradiantBoostStockModel(path , period.toString(), codif);
        modelSet.put(type, model);
    }

    public void save(ModelType type, String path, PredictionPeriodicity period, String codif) {
        Model rfModel = modelSet.get(type);
        rfModel.save(path + "model/Model" +ModelType.code(type)+ period.toString() + codif);
    }

    public double aggregate(MLStocks s, String date) {
        FeaturesStock fs = FeaturesStock.createRT(s.getCodif(), getValidator(ModelType.RANDOMFOREST), date);
        if (fs.currentVectorPos != getValidator(ModelType.RANDOMFOREST).getVectorSize()) {
            log.error("model broken!!!!!: " + fs.currentVectorPos +" not equal " +getValidator(ModelType.RANDOMFOREST).getVectorSize());
            System.exit(100);
        }
        return modelSet.get(ModelType.RANDOMFOREST).predict(Vectors.dense(fs.vectorize()));
    }

    public void saveModel(ModelType type, PredictionPeriodicity period, String codif) {
        Model rfModel = modelSet.get(type);
        if (rfModel.getModel() != null)
            rfModel.getValidator().saveModel( codif + ModelType.code(type) + period.toString());
    }
}
