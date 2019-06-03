package com.mltrading.ml.ranking;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLStatus;
import com.mltrading.ml.model.Model;
import com.mltrading.ml.model.ModelType;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import scala.Serializable;

public class MLGradiantBoostRanking extends Model<GradientBoostedTreesModel> implements Serializable {
    GradientBoostedTreesModel model;

    public MLGradiantBoostRanking(GradientBoostedTreesModel model) {
        super();
        this.model = model;
    }

    public MLGradiantBoostRanking() {
        super();
    }

    public MLGradiantBoostRanking(String path, String period, String codif, String modelExtendedPrefix) {
        super(period, codif, ModelType.GRADIANTBOOSTTREE, modelExtendedPrefix);
        load(path, period, codif, modelExtendedPrefix);
    }



    @Override
    public void setModel(GradientBoostedTreesModel model) {
        this.model = model;
    }

    @Override
    public GradientBoostedTreesModel getModel() {
        return this.model;
    }


    @Override
    public void load(String path, String period, String codif,  String modelExtendedPrefix) {
        this.model = GradientBoostedTreesModel.load(CacheMLStock.getJavaSparkContext().sc(), path + "model/Model" +  ModelType.code(ModelType.GRADIANTBOOSTTREE) + period + codif + modelExtendedPrefix);
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
