package com.mltrading.ml;

/**
 * Created by gmo on 26/01/2016.
 */


import com.mltrading.ml.model.MLGradiantBoostStockModel;
import com.mltrading.ml.model.MLRandomForestModel;
import com.mltrading.ml.model.Model;
import com.mltrading.ml.model.ModelType;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;



/**
 * basic class implementation for machine learning status
 * container of MLStock and MLStatus for result
 */
public class MLStocks  implements Serializable {
    private String codif;
    private Map<PredictionPeriodicity,MLStock> container;

    private HashMap<ModelType,MLStatus> statusMap = new HashMap<>();

    double ratio = 1.;

    JavaRDD<FeaturesStock> testData;

    public boolean isEmtpyModel() {
        if (container.get(PredictionPeriodicity.D20).getModel(ModelType.RANDOMFOREST).getModel() == null &&
            container.get(PredictionPeriodicity.D20).getModel(ModelType.GRADIANTBOOSTTREE).getModel() == null)
            return true;

        return false;
    }


    public MLStocks(String codif) {
        this.codif = codif;
        container = new HashMap<>();
        container.put(PredictionPeriodicity.D1, new MLStock(codif, PredictionPeriodicity.D1));
        container.put(PredictionPeriodicity.D5, new MLStock(codif, PredictionPeriodicity.D5));
        container.put(PredictionPeriodicity.D20, new MLStock(codif, PredictionPeriodicity.D20));
        container.put(PredictionPeriodicity.D40, new MLStock(codif, PredictionPeriodicity.D40));

        MLStatus statusRF = new MLStatus();
        statusMap.put(ModelType.RANDOMFOREST, statusRF);
        MLStatus statusGBT = new MLStatus();
        statusMap.put(ModelType.GRADIANTBOOSTTREE, statusGBT);
        MLStatus statusENSEMBLE = new MLStatus();
        statusMap.put(ModelType.ENSEMBLE, statusENSEMBLE);
    }

    public MLStock getSock(PredictionPeriodicity period) {
        return container.get(period);
    }


    public MLStatus getStatus(ModelType type) {
        return statusMap.get(type);
    }

    public void setStatus(MLStatus status, ModelType type) {
        this.statusMap.put(type,status);
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


    public void load(ModelType type) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().load(type);
        }
    }


    public void saveValidator(ModelType type) throws InterruptedException {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().saveValidator(type);
        }
    }

    /**
     * saveValidator for specific perdiod
     * @param p
     * @param type
     */
    public void saveModel(PredictionPeriodicity p, ModelType type) {
        container.get(p).saveModel(type);
    }

    public void generateValidator(String methodName, int sector, ModelType type) {

        try {
            Class[] cArg = new Class[1];
            cArg[0] = Integer.class;
            MatrixValidator validator = new MatrixValidator();
            validator.getClass().getMethod(methodName,cArg).invoke(validator, sector);
            for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
                entry.getValue().getModel(type).setValidator(validator.clone());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public void setValidators(MatrixValidator validator,  ModelType type) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().getModel(type).setValidator(validator);
        }
    }


    /**
     * This function is use by importModel
     * Create model and empty validator
     * @param p
     * @param validator
     * @param type
     */
    public void createValidator(PredictionPeriodicity p, MatrixValidator validator,  ModelType type) {
        if (type == ModelType.RANDOMFOREST ) container.get(p).setModel(new MLRandomForestModel(),ModelType.RANDOMFOREST);
        if (type == ModelType.GRADIANTBOOSTTREE )container.get(p).setModel(new MLGradiantBoostStockModel(),ModelType.GRADIANTBOOSTTREE);
        container.get(p).getModel(type).setValidator(validator);
    }


    /**
     * set validator in container
     * @param p
     * @param validator
     */
    public void setValidator(PredictionPeriodicity p, MatrixValidator validator,  ModelType type) {
        container.get(p).getModel(type).setValidator(validator);
    }


    public MatrixValidator getValidator(PredictionPeriodicity p,  ModelType type) {
        return container.get(p).getModel(type).getValidator();
    }


    public boolean randomizeModel( ModelType type) {
        boolean checkContinue =true;

        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            checkContinue &= entry.getValue().getModel(type).getValidator().optimizeModel(entry.getValue().getModel(type).getValidator());
        }

        return checkContinue;
    }



    /**
     * set validator in container
     * @param p
     * @param model
     */
    public void setModel(PredictionPeriodicity p, Model model, ModelType type) {
        container.get(p).setModel(model, type);
    }


    public Model getModel(PredictionPeriodicity p, ModelType type) {
        return container.get(p).getModel(type);
    }

    public MLModel getModel(PredictionPeriodicity p) {
        return container.get(p).getModel();
    }



    /**
     * recopy mlStock from MLStocks ref for period
     * @param p
     * @param ref
     */
    public void replace(PredictionPeriodicity p,MLStocks ref, ModelType type) {
        this.setModel(p,ref.getModel(p, type), type);
        this.getModel(p).setValidator(type, ref.getModel(p).getValidator(type));
    }


    /**
     * recopy mlStock from MLStocks ref for period
     * @param p
     * @param ref
     */
    public void insert(PredictionPeriodicity p,MLStocks ref,ModelType type) {
        this.setModel(p,ref.getModel(p, type), type);
        this.getModel(p).getValidator(type).replace(ref.getModel(p).getValidator(type));
    }






    /**
     *
     * @param ref
     * @return
     */
    @Deprecated
    public MLStocks replaceValidator(MLStocks ref, ModelType type) {
        int position = this.getModel(PredictionPeriodicity.D1).getValidator(type).getCol();
        MLStocks copy = new MLStocks(this.getCodif());

        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            copy.getModel(entry.getKey()).setValidator(type,ref.getModel(entry.getKey()).getValidator(type).clone());
            copy.getModel(entry.getKey()).getValidator(type).setCol(position);
        }

        return copy;
    }


    public void resetScoring() {
        setScoring(false);
    }


    public void setScoring(boolean scoring) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().setModelImprove(scoring);
        }
    }


    /**
     *
     * @return
     */
    @Override
    public MLStocks clone() {

        /*MLStocks copy = new MLStocks(this.getCodif());

        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            copy.setValidator(entry.getKey(), this.getValidator(entry.getKey()).clone());
        }

        copy.setStatus(this.getStatus().clone());

        return copy;*/
        throw  new NotImplementedException();
    }



    public void distibute(ModelType type) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().distibute(type);
        }
    }

    public void send(JavaSparkContext sc) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().send(sc);
        }
    }

    public void saveDB(ModelType type) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().saveModelDB(type);
        }
    }


    public void saveDB(PredictionPeriodicity p, ModelType type) {
        container.get(p).saveModelDB(type);
    }

    public void loadDB(ModelType type) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().loadModelDB(type);
        }
    }

    public void updateColValidator(int col, ModelType type) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().getModel().getValidator(type).setCol(col);
        }
    }

    public Map<PredictionPeriodicity,MatrixValidator> getValidators(ModelType type) {
        Map<PredictionPeriodicity,MatrixValidator> mapValidator = new HashMap<>();
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            mapValidator.put(entry.getKey(),entry.getValue().getModel(type).getValidator());
        }
        return mapValidator;
    }

    public void mergeValidator(MLStocks ref,ModelType type) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().mergetValidator(ref.getModel(entry.getKey()).getValidator(type));
        }
    }

    public void setModels(ModelType type, MLStocks mls) {
        for (Map.Entry<PredictionPeriodicity, MLStock> entry : container.entrySet()) {
            entry.getValue().setModel(mls.getModel(entry.getKey(),type),type);
        }
    }

    public double getRatio() {
        return this.ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
