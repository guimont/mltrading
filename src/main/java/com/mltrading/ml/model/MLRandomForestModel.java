package com.mltrading.ml.model;

import com.mltrading.ml.CacheMLStock;
import org.apache.spark.mllib.linalg.Vector;


import org.apache.spark.mllib.tree.model.RandomForestModel;
import scala.Serializable;

public class MLRandomForestModel extends Model<RandomForestModel> implements Serializable {
    RandomForestModel model;

    public MLRandomForestModel(RandomForestModel model) {
        super();
        this.model = model;
    }

    public MLRandomForestModel() {
        super();
    }

    public MLRandomForestModel(String path, String period, String codif, String modelExtendedPrefix) {
        super(period, codif, ModelType.RANDOMFOREST, modelExtendedPrefix);
        load(path, period, codif, modelExtendedPrefix);
    }


    @Override
    void setModel(RandomForestModel model) {
        this.model = model;
    }

    @Override
    public RandomForestModel getModel() {
        return this.model;
    }


    @Override
    void load(String path, String period, String codif,  String modelExtendedPrefix) {
        this.model = RandomForestModel.load(CacheMLStock.getJavaSparkContext().sc(), path + "model/Model" +  ModelType.code(ModelType.RANDOMFOREST) + period + codif + modelExtendedPrefix);
        //this.validator.loadValidator(codif +  ModelType.code(ModelType.RANDOMFOREST) + period);
    }

    @Override
    public double predict(Vector vector) {
        return  model.predict(vector);
    }

    @Override
    public void save(String s) {
        this.model.save(CacheMLStock.getJavaSparkContext().sc(), s);
    }
}
