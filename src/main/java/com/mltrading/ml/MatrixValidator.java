package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.influxdb.dto.QueryResult;
import com.mltrading.models.stock.CacheRawMaterial;
import com.mltrading.models.stock.CacheStockIndice;
import com.mltrading.models.stock.CacheStockSector;
import com.mltrading.models.stock.StockAnalyse;

import java.io.Serializable;

/**
 * Created by gmo on 21/07/2016.
 */
public class MatrixValidator implements Serializable,Cloneable {

    /** Controls the perdiod */
    public enum TypeHistory {
        /** stock history */
        STK,
        /** sector history */
        SEC,
        /** indice history */
        IND,
        /** raw history */
        RAW
    }

    Integer maxDepth = 5;
    Integer maxBins = 32;
    Integer numTrees = 100; // Use more in practice.
    Integer seed = 12345;

    public double error;
    public double rate;
    public int vectorSize;


    public static String dbNamePerf = "perf";
    public static String dbNameModel = "modelNote";

    Integer matrix[][];

    /**
     * position for current stock
     */
    static public int HS_POS = 0;
    static private int N_HS = 1;

    static private int HS_COL = 0;
    static private int HS_PERIOD_COL = 1;
    static private int HS_VOLUME_COL = 2;
    static private int N_HS_COL = 3;

    private int vSize = 300;




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

    public static int randomiBool() {
        if (Math.random()>0.5) return 1;
        return 0;
    }

    public static int randomPeriod(int min, int max) {
        return (int) (Math.random() * max + min);
    }




    public MatrixValidator() {
        matrix = new Integer[globalROW][globalCOL];
    }

    /**
     * Generate a random matrix validator for features selection
     */
    public void generate() {
        for (int i = HS_POS ; i < globalROW; i++) {
            matrix[i][HS_COL] = randomiBool();
            matrix[i][HS_PERIOD_COL] = randomPeriod(2, 50);
            matrix[i][HS_VOLUME_COL] = randomiBool();
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = randomiBool();
        }
    }

    /**
     * Save matrix in influx db
     * @param code
     * @param error
     * @param rate
     */
    public void save(String code, int error, double rate) {
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(dbNamePerf);

        Point.Builder pt = Point.measurement(code);
        for (int i = 0 ; i < globalROW; i++) {
            for (int j = 0; j < globalCOL; j++){
                pt.field(String.format("%04d",i*globalCOL+j),  matrix[i][j].intValue());
            }
        }

        pt.field("maxDepth", maxDepth)
            .field("maxBins", maxBins)
            .field("numTrees", numTrees)
            .field("seed", seed)
            .field("error", error)
            .field("rate", rate)
            .field("vectorSize", vectorSize)
            .field("vsize", vSize);

        Point f = pt.build();
        bp.point(f);
        InfluxDaoConnector.writePoints(bp);

    }

    public void loadValidator(String code) {

        String query = "SELECT * FROM " + code;
        QueryResult result = InfluxDaoConnector.getPoints(query,dbNameModel);

        int size = result.getResults().get(0).getSeries().get(0).getValues().size() -1;

        for (int i = 0 ; i < globalROW; i++) {
            for (int j = 0; j < globalCOL; j++){
                matrix[i][j] = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(i*globalCOL+j+1))).intValue());
            }
        }

        int index = globalROW*globalCOL+1;
        this.error = (double) result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++);
        this.maxBins = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
        this.maxDepth = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
        this.numTrees = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
        this.rate = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue();
        this.seed = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
        this.vectorSize = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
        this.vSize = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
    }

    public void saveModel(String code) {
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(dbNameModel);
        Point.Builder pt = Point.measurement(code);
        for (int i = 0 ; i < globalROW; i++) {
            for (int j = 0; j < globalCOL; j++){
                pt.field(String.format("%04d", i * globalCOL + j),  matrix[i][j].intValue());
            }
        }

        pt.field("maxDepth", maxDepth)
            .field("maxBins", maxBins)
            .field("numTrees", numTrees)
            .field("seed", seed)
            .field("error", error)
            .field("rate", rate)
            .field("vectorSize", vectorSize)
            .field("vsize", vSize);

        Point f = pt.build();
        bp.point(f);
        InfluxDaoConnector.writePoints(bp);

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


    /**
     * get matrix stock period history
     * @return
     */
    public int getPeriodStockHist() {
        return getPeriodHist(HS_POS);
    }

    /**
     * get matrix stock period history
     * @return
     */
    public boolean getPeriodVolume() {
        return getPeriodVolume(HS_POS);
    }


    /**
     * get matrix period enable for @indice
     * @param indice
     * @return
     */
    public boolean getPeriodEnable(int indice) {
        return matrix[indice][HS_COL] == 0;
    }

    /**
     * get matrix period history for @indice
     * @param indice
     * @return
     */
    public int getPeriodHist(int indice) {
        return matrix[indice][HS_PERIOD_COL];
    }

    /**
     * get matrix period volume for @indice
     * @param indice
     * @return
     */
    public boolean getPeriodVolume(int indice) {
        return matrix[indice][HS_VOLUME_COL] == 0;
    }




    /**
     * get matrix COL_MMA20_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMMA20(int indice) {
        return matrix[indice][StockAnalyse.COL_MMA20_POS + N_HS_COL] == 0;
    }

    /**
     * get matrix COL_MMA50_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMMA50(int indice) {
        return matrix[indice][StockAnalyse.COL_MMA50_POS + N_HS_COL] == 0;
    }

    /**
     * get matrix COL_MME12_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMME12(int indice) {
        return matrix[indice][StockAnalyse.COL_MME12_POS + N_HS_COL] == 0;
    }

    /**
     * get matrix COL_MME26_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMME26(int indice) {
        return matrix[indice][StockAnalyse.COL_MME26_POS + N_HS_COL] == 0;
    }

    /**
     * get matrix COL_MACD_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMACD(int indice) {
        return matrix[indice][StockAnalyse.COL_MACD_POS + N_HS_COL] == 0;
    }

    /**
     * get matrix COL_MACD_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMOMENTUM(int indice) {
        return matrix[indice][StockAnalyse.COL_MOMENTUM_POS + N_HS_COL] == 0;
    }

    /**
     * get matrix COL_STDDEV_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATSTDDEV(int indice) {
        return matrix[indice][StockAnalyse.COL_STDDEV_POS + N_HS_COL] == 0;
    }



    public int getIndice(int indice, TypeHistory type) {
        if (type == TypeHistory.SEC) return indice + N_HS;
        if (type == TypeHistory.IND) return indice + N_HS + CacheStockSector.N_SECTOR;
        if (type == TypeHistory.RAW) return indice + N_HS + CacheStockSector.N_SECTOR + CacheStockIndice.N_INDICE;
        return 0;
    }

}
