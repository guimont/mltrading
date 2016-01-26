package com.mltrading.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import java.util.List;

/**
 * Created by gmo on 26/01/2016.
 */
public class MLStock {
    private String codif;
    private RandomForestModel model;
    private List<MLPerformance> perfList;
    private List<Double> previsionList;
    JavaRDD<FeaturesStock> testData;


    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public RandomForestModel getModel() {
        return model;
    }

    public void setModel(RandomForestModel model) {
        this.model = model;
    }

    public List<MLPerformance> getPerfList() {
        return perfList;
    }

    public void setPerfList(List<MLPerformance> perfList) {
        this.perfList = perfList;
    }

    public List<Double> getPrevisionList() {
        return previsionList;
    }

    public void setPrevisionList(List<Double> previsionList) {
        this.previsionList = previsionList;
    }

    public JavaRDD<FeaturesStock> getTestData() {
        return testData;
    }

    public void setTestData(JavaRDD<FeaturesStock> testData) {
        this.testData = testData;
    }


    public void save() {
        this.model.save(CacheMLStock.getJavaSparkContext().sc(),"Model"+codif);

    }
}
