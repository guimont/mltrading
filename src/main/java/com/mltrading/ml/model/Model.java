package com.mltrading.ml.model;

import com.mltrading.ml.MatrixValidator;
import org.apache.spark.mllib.linalg.Vector;
import scala.Serializable;


public abstract class Model<R> implements Serializable {

    protected MatrixValidator validator;


    public Model() {
        this.validator = new MatrixValidator();
    }

    public Model(String period, String codif, ModelType type) {
        this.validator = new MatrixValidator();
        this.validator.loadValidator(codif+ModelType.code(type)+period.toString());
    }

    public MatrixValidator getValidator() {
        return validator;
    }

    public void setValidator(MatrixValidator validator) {
        this.validator = validator;
    }

    abstract void setModel(R model);
    public abstract R getModel();
    abstract void load(String path, String period, String codif);
    public abstract double predict(Vector vector);
    public abstract void save(String s);
}
