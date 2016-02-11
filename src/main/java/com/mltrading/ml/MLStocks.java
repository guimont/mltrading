package com.mltrading.ml;

/**
 * Created by gmo on 26/01/2016.
 */

import com.mltrading.ml.FeaturesStock.PredictionPeriodicity;
import org.apache.spark.api.java.JavaRDD;

import java.util.List;


public class MLStocks {
    private String codif;
    private MLStock mlD1;
    private MLStock mlD5;
    private MLStock mlD20;

    private List<MLPerformances> perfList;

    JavaRDD<FeaturesStock> testData;

    public MLStocks(String codif) {
        this.codif = codif;
        mlD1 = new MLStock(codif, PredictionPeriodicity.D1);
        mlD5 = new MLStock(codif, PredictionPeriodicity.D5);
        mlD20 = new MLStock(codif, PredictionPeriodicity.D20);
    }

    public List<MLPerformances> getPerfList() {
        return perfList;
    }

    public void setPerfList(List<MLPerformances> perfList) {
        this.perfList = perfList;
    }

    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public MLStock getMlD1() {
        return mlD1;
    }

    public void setMlD1(MLStock mlD1) {
        this.mlD1 = mlD1;
    }

    public MLStock getMlD5() {
        return mlD5;
    }

    public void setMlD5(MLStock mlD5) {
        this.mlD5 = mlD5;
    }

    public MLStock getMlD20() {
        return mlD20;
    }

    public void setMlD20(MLStock mlD20) {
        this.mlD20 = mlD20;
    }

    public JavaRDD<FeaturesStock> getTestData() {
        return testData;
    }

    public void setTestData(JavaRDD<FeaturesStock> testData) {
        this.testData = testData;
    }


}
