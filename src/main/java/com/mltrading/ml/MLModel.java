package com.mltrading.ml;

import org.apache.spark.mllib.linalg.Vectors;

import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;

/**
 * Created by gmo on 01/03/2017.
 */
public class MLModel {

    private RandomForestModel modelRF = null;
    private GradientBoostedTreesModel modelGbt = null;


    public void setmodel(RandomForestModel model) {this.modelRF = model;}
    public void setmodel(GradientBoostedTreesModel model) {this.modelGbt = model;}

    public double  predict(double values[]) {
        if (modelRF != null) return modelRF.predict(Vectors.dense(values));
        if (modelGbt != null) return modelGbt.predict(Vectors.dense(values));

        return 0;
    }


    public RandomForestModel getModel() {
        return modelRF;
    }
}
