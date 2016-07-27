package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.CacheRawMaterial;
import com.mltrading.models.stock.CacheStockIndice;
import com.mltrading.models.stock.CacheStockSector;
import com.mltrading.models.stock.StockAnalyse;

import java.io.Serializable;

/**
 * Created by gmo on 21/07/2016.
 */
public class MatrixValidator implements Serializable,Cloneable {

    Integer maxDepth = 5;
    Integer maxBins = 32;
    Integer numTrees = 100; // Use more in practice.
    Integer seed = 12345;

    public int vectorSize;

    public static String dbName = "perf";

    Integer matrix[][];

    static private int HS_POS = 0;
    static private int N_HS = 1;

    static private int HS_COL = 0;
    static private int HS_PERIOD_COL = 1;
    static private int N_HS_COL = 2;

    private int globalROW = CacheStockSector.N_SECTOR+ CacheStockIndice.N_INDICE+ CacheRawMaterial.N_RAW+ N_HS;
    private int globalCOL = N_HS_COL+StockAnalyse.N_AT;

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Integer getMaxBins() {
        return maxBins;
    }

    public void setMaxBins(Integer maxBins) {
        this.maxBins = maxBins;
    }

    public Integer getNumTrees() {
        return numTrees;
    }

    public void setNumTrees(Integer numTrees) {
        this.numTrees = numTrees;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public int getVectorSize() {
        return vectorSize;
    }

    public void setVectorSize(int vectorSize) {
        this.vectorSize = vectorSize;
    }

    /**
     * col indice
     */



    public MatrixValidator() {
        matrix = new Integer[globalROW][globalCOL];
    }

    /**
     * Generate a random matrix validator for features selection
     */
    public void generate() {
        for (int i = HS_POS ; i < globalROW; i++) {
            matrix[i][HS_COL] = Validator.randomiBool();
            matrix[i][HS_PERIOD_COL] = Validator.randomPeriod(2, 50);
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = Validator.randomiBool();
        }
    }

    /**
     * Save matrix in influx db
     * @param code
     * @param error
     * @param rate
     */
    public void save(String code, int error, double rate) {
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(dbName);

        Point.Builder pt = Point.measurement(code);
        for (int i = 0 ; i < globalROW; i++) {
            for (int j = 0; j < globalCOL; j++){
                pt.field(new Integer(i*j).toString(),  matrix[i][j]);
            }
        }

        Point f = pt.build();
        bp.point(f);
        InfluxDaoConnector.writePoints(bp);

    }

    public void loadValidator(String s) {

    }

    public void saveModel(String s) {

    }


    public MatrixValidator clone() {
        Object o = null;
        try {

            o = super.clone();
        } catch(CloneNotSupportedException cnse) {

            cnse.printStackTrace(System.err);
        }

        return (MatrixValidator) o;
    }
}
