package com.mltrading.ml;



import org.apache.spark.mllib.tree.model.RandomForestModel;

import java.io.Serializable;


public class MLStock  implements Serializable {
    private String codif;
    private PredictionPeriodicity period;
    private RandomForestModel model;
    private MatrixValidator validator;


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



    public void saveModel() {
       this.model.save(CacheMLStock.getJavaSparkContext().sc(), "model/Model" + period.toString() + codif);
    }

    public void loadModel() {
        this.model = RandomForestModel.load(CacheMLStock.getJavaSparkContext().sc(), "model/Model"+period.toString()+codif);
    }


    public void load() {
        loadModel();
        validator.loadValidator(codif+"V"+period.toString());
    }

    public void save() {
        saveModel();
        validator.saveModel(codif+"V"+period.toString());
    }


}
