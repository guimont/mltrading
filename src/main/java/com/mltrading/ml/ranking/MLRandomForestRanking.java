package com.mltrading.ml.ranking;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLStatus;

import com.mltrading.ml.model.Model;
import com.mltrading.ml.model.ModelType;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import scala.Serializable;

public class MLRandomForestRanking extends Model<RandomForestModel> implements Serializable {
    RandomForestModel model;

    public MLRandomForestRanking(RandomForestModel model) {
        super();
        this.model = model;
    }

    public  MLRandomForestRanking() {
        super();
    }

    public MLRandomForestRanking(String path, String period, String codif, String modelExtendedPrefix) {
        super(period, codif, ModelType.RANDOMFOREST, modelExtendedPrefix);
        load(path, period, codif, modelExtendedPrefix);
    }



    @Override
    public void setModel(RandomForestModel model) {
        this.model = model;
    }

    @Override
    public RandomForestModel getModel() {
        return this.model;
    }


    @Override
    public void load(String path, String period, String codif,  String modelExtendedPrefix) {
        this.model = RandomForestModel.load(CacheMLStock.getJavaSparkContext().sc(), path + "model/Model" +  ModelType.code(ModelType.RANDOMFOREST) + period + codif + modelExtendedPrefix);
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
