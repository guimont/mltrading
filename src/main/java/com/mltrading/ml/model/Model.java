package com.mltrading.ml.model;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MatrixValidator;
import org.apache.spark.mllib.linalg.Vector;
import scala.Serializable;


public abstract class Model<R> implements Serializable {

    protected MatrixValidator validator;


    public Model() {
        this.validator = new MatrixValidator();
    }

    public Model(String period, String codif, ModelType type, String prefix) {
        this.validator = new MatrixValidator();
        this.validator.loadValidator(codif+ModelType.code(type)+period.toString(), CacheMLStock.guessDbName(prefix));
    }

    public MatrixValidator getValidator() {
        return validator;
    }

    public void setValidator(MatrixValidator validator) {
        this.validator = validator;
    }

    public abstract void setModel(R model);
    public abstract R getModel();
    public abstract void load(String path, String period, String codif, String modelExtendedPrefix);
    public abstract double predict(Vector vector);
    public abstract void save(String s);
}
