package com.mltrading.ml.model;

import com.mltrading.ml.CacheMLStock;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import scala.Serializable;

public class MLGradiantBoostStockModel extends Model<GradientBoostedTreesModel> implements Serializable {
    GradientBoostedTreesModel model;

    public MLGradiantBoostStockModel(GradientBoostedTreesModel model) {
        super();
        this.model = model;
    }

    public MLGradiantBoostStockModel() {
        super();
    }

    public MLGradiantBoostStockModel(String path, String period, String codif) {
        super(period, codif, ModelType.GRADIANTBOOSTTREE);
        load(path, period, codif);
    }


    @Override
    void setModel(GradientBoostedTreesModel model) {
        this.model = model;
    }

    @Override
    public GradientBoostedTreesModel getModel() {
        return this.model;
    }


    @Override
    void load(String path, String period, String codif) {
        this.model = GradientBoostedTreesModel.load(CacheMLStock.getJavaSparkContext().sc(), path + "model/Model" + ModelType.code(ModelType.GRADIANTBOOSTTREE) + period.toString() + codif);
        this.validator.loadValidator(codif + ModelType.code(ModelType.GRADIANTBOOSTTREE) + period.toString());
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
