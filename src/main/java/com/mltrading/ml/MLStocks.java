package com.mltrading.ml;

/**
 * Created by gmo on 26/01/2016.
 */


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * basic class implementation for machine learning status
 */
public class MLStocks  implements Serializable {
    private String codif;
    private Map<PredictionPeriodicity,MLStock> container;

    private MLStatus status;

    JavaRDD<FeaturesStock> testData;

    public MLStocks(String codif) {
        this.codif = codif;
        container = new HashMap<>();
        container.put(PredictionPeriodicity.D1, new MLStock(codif, PredictionPeriodicity.D1));
        container.put(PredictionPeriodicity.D5, new MLStock(codif, PredictionPeriodicity.D5));
        container.put(PredictionPeriodicity.D20, new MLStock(codif, PredictionPeriodicity.D20));
        container.put(PredictionPeriodicity.D40, new MLStock(codif, PredictionPeriodicity.D40));

        status = new MLStatus();
    }

    public MLStock getSock(PredictionPeriodicity period) {
        return container.get(period);
    }


    public MLStatus getStatus() {
        return status;
    }

    public void setStatus(MLStatus status) {
        this.status = status;
    }

    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }


    public void setTestData(JavaRDD<FeaturesStock> testData) {
        this.testData = testData;
    }


    public void load() {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().load();
        }
    }

    public void save() {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().save();
        }
    }

    public void generateValidator(String methodName) {

        try {
            MatrixValidator validator = new MatrixValidator();
            validator.getClass().getMethod(methodName,null).invoke(validator, null);
            for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
                entry.getValue().setValidator(validator.clone());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }


    /**
     * set validator in container
     * @param p
     * @param validator
     */
    public void setValidator(PredictionPeriodicity p, MatrixValidator validator) {
        container.get(p).setValidator(validator);
    }


    public MatrixValidator getValidator(PredictionPeriodicity p) {
        return container.get(p).getValidator();
    }


    public boolean randomizeModel() {
        boolean checkContinue =true;

        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            checkContinue &= entry.getValue().getValidator().randomizeModel(entry.getValue().getValidator());
        }

        return checkContinue;
    }



    /**
     * set validator in container
     * @param p
     * @param model
     */
    public void setModel(PredictionPeriodicity p, RandomForestModel model) {
        container.get(p).setModel(model);
    }


    public RandomForestModel getModel(PredictionPeriodicity p) {
        return container.get(p).getModel();
    }


    /**
     * recopy mlStock from MLStocks ref for period
     * @param p
     * @param ref
     */
    public void replace(PredictionPeriodicity p,MLStocks ref) {
        this.setModel(p,ref.getModel(p));
        this.setValidator(p, ref.getValidator(p));
    }

    public MLStocks replaceValidator(MLStocks ref) {
        int position = this.getValidator(PredictionPeriodicity.D1).getCol();
        MLStocks copy = new MLStocks(this.getCodif());

        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            copy.setValidator(entry.getKey(),ref.getValidator(entry.getKey()));
            copy.getValidator(entry.getKey()).setCol(position);
        }

        return copy;

    }
}
