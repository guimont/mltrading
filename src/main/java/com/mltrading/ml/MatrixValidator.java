package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.models.stock.cache.CacheRawMaterial;
import com.mltrading.models.stock.cache.CacheStockIndice;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.stock.StockAnalyse;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by gmo on 21/07/2016.
 */
public class MatrixValidator implements Serializable,Cloneable {


    private static final Random random = new Random(0);


    public void setCol(int col) {
        this.col = col;
    }

    public Integer[][] getMatrix() {
        return matrix;
    }



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

    int col = 0;

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
    static public int N_HS = 1;

    static public int HS_COL = 0;
    static public int HS_PERIOD_COL = 1;
    static private int HS_VOLUME_COL = 2;
    static private int N_HS_COL = 3;

    private int vSize = 0;


    public int getCol() {
        return col;
    }

    public static int globalROW = CacheStockSector.N_SECTOR+ CacheStockIndice.N_INDICE+ CacheRawMaterial.N_RAW+ N_HS;
    private static int globalCOL = N_HS_COL+StockAnalyse.N_AT;

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
        return random.nextBoolean() ? 1:0;
    }

    public static boolean randomBool() {
        return random.nextBoolean();
    }

    public static int randomPeriod(int min, int max) {
        return (int) (Math.random() * max + min);
    }


    public void replace(MatrixValidator validator) {
        this.matrix[validator.getCol()-1][HS_COL] = TRUEBOOL;
    }

    public void validate(int col) {
        this.matrix[col][HS_COL] = TRUEBOOL;
    }




    public MatrixValidator() {
        matrix = new Integer[globalROW][globalCOL];
    }

    /**
     * Generate a random matrix validator for features selection
     * a sense to disable HS_POS ???? not sure
     */
    /**
     *
     * @return
     */
    public MatrixValidator generateRandomModel(Integer notuse) {
        for (int i = HS_POS ; i < globalROW; i++) {
            matrix[i][HS_COL] = randomiBool();
            matrix[i][HS_PERIOD_COL] = randomPeriod(2, 100);
            matrix[i][HS_VOLUME_COL] = randomiBool();
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = randomiBool();
        }
        return this;
    }


    /**
     * merge two validator
     * Used by genetic layer algorithm
     * @param mv
     */
    public void merge(MatrixValidator mv) {
        for (int i = HS_POS ; i < globalROW; i++) {
            if (randomBool()) this.matrix[i][HS_COL] = mv.matrix[i][HS_COL];
            if (randomBool())  this.matrix[i][HS_PERIOD_COL] = mv.matrix[i][HS_PERIOD_COL];
            if (randomBool())  this.matrix[i][HS_VOLUME_COL] = mv.matrix[i][HS_VOLUME_COL];
            for (int j = N_HS_COL; j < globalCOL; j++)
                if (randomBool())  this.matrix[i][j] = mv.matrix[i][j];
        }
    }




    /**
     * Generate a random matrix validator for features selection
     */
    static private int TRUEBOOL = 1;
    static private int FALSEBOOL = 0;
    static private int DEFAULTPERIOD = 25;
    public void generateSimpleModel(Integer notuse) {
        //hs stock => enable
        //stock sector => enable
        //all indice => enable
        //raw and other => disable

        matrix[HS_POS][HS_COL] = TRUEBOOL;
        matrix[HS_POS][HS_PERIOD_COL] = DEFAULTPERIOD;
        matrix[HS_POS][HS_VOLUME_COL] = TRUEBOOL;
        for (int j = N_HS_COL; j < globalCOL; j++)
            matrix[HS_POS][j] = TRUEBOOL;

        //stock sector => disable
        for (int i = N_HS ; i < N_HS+CacheStockSector.N_SECTOR; i++) {
            matrix[i][HS_COL] = FALSEBOOL;
            matrix[i][HS_PERIOD_COL] = DEFAULTPERIOD;
            matrix[i][HS_VOLUME_COL] = TRUEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = TRUEBOOL;
        }

        //stock indice => disable
        for (int i = N_HS+CacheStockSector.N_SECTOR ; i < N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE; i++) {
            matrix[i][HS_COL] = FALSEBOOL;
            matrix[i][HS_PERIOD_COL] = DEFAULTPERIOD;
            matrix[i][HS_VOLUME_COL] = TRUEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = TRUEBOOL;
        }

        //stock raw => disable
        for (int i = N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE ; i < globalROW; i++) {
            matrix[i][HS_COL] = FALSEBOOL;
            matrix[i][HS_PERIOD_COL] = DEFAULTPERIOD;
            matrix[i][HS_VOLUME_COL] = FALSEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = TRUEBOOL;
        }
    }

    /**
     * improve existing matrix
     * @param sector
     */
    public void improveEconomicalModel(Integer sector) {
        matrix[HS_POS][HS_COL] = TRUEBOOL;
        matrix[HS_POS][HS_PERIOD_COL] = randomPeriod(2, 100);
        matrix[HS_POS][HS_VOLUME_COL] = randomiBool();
        for (int j = N_HS_COL; j < globalCOL; j++)
            matrix[HS_POS][j] = randomiBool();

        if (sector != CacheStockSector.NO_SECTOR) {
            matrix[N_HS + sector][HS_COL] = TRUEBOOL;
            matrix[N_HS + sector][HS_PERIOD_COL] = randomPeriod(2, 100);
            matrix[N_HS + sector][HS_VOLUME_COL] = randomiBool();
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[N_HS + sector][j] = randomiBool();
        }


        //stock indice => enable
        for (int i = N_HS+CacheStockSector.N_SECTOR ; i < N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE; i++) {
            matrix[i][HS_COL] = TRUEBOOL;
            matrix[i][HS_PERIOD_COL] = randomPeriod(2, 100);
            matrix[i][HS_VOLUME_COL] = randomiBool();
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = randomiBool();
        }
    }

    /**
     * merge validator with best validator and a generate Eco
     * @param validator
     */
    public void mergeEconomical(MatrixValidator validator) {
        //stock sector => recopy
        for (int i = N_HS ; i < N_HS+CacheStockSector.N_SECTOR; i++) {
            matrix[i][HS_COL] = validator.matrix[i][HS_COL] ;
            matrix[i][HS_PERIOD_COL] = validator.matrix[i][HS_PERIOD_COL] ;
            matrix[i][HS_VOLUME_COL] = validator.matrix[i][HS_VOLUME_COL] ;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = validator.matrix[i][j];
        }


        //stock raw => recopy
        for (int i = N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE ; i < globalROW; i++) {
            matrix[i][HS_COL] = validator.matrix[i][HS_COL];
            matrix[i][HS_VOLUME_COL] = validator.matrix[i][HS_VOLUME_COL];
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = validator.matrix[i][j];
        }

    }


    public void generateEconomicalModel(Integer sector) {
        //hs stock => enable
        //stock sector => enable
        //all indice => enable
        //raw and other => disable

        matrix[HS_POS][HS_COL] = TRUEBOOL;
        matrix[HS_POS][HS_PERIOD_COL] = randomPeriod(2, 100);
        matrix[HS_POS][HS_VOLUME_COL] = randomiBool();
        for (int j = N_HS_COL; j < globalCOL; j++)
            matrix[HS_POS][j] = randomiBool();


        //stock sector => disable
        for (int i = N_HS ; i < N_HS+CacheStockSector.N_SECTOR; i++) {
            matrix[i][HS_COL] = FALSEBOOL;
            matrix[i][HS_PERIOD_COL] = 0;
            matrix[i][HS_VOLUME_COL] = FALSEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = FALSEBOOL;
        }

        if (sector != CacheStockSector.NO_SECTOR) {
            matrix[N_HS + sector][HS_COL] = TRUEBOOL;
            matrix[N_HS + sector][HS_PERIOD_COL] = randomPeriod(2, 100);
            matrix[N_HS + sector][HS_VOLUME_COL] = randomiBool();
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[N_HS + sector][j] = randomiBool();
        }


        //stock indice => enable
        for (int i = N_HS+CacheStockSector.N_SECTOR ; i < N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE; i++) {
            matrix[i][HS_COL] = TRUEBOOL;
            matrix[i][HS_PERIOD_COL] = randomPeriod(2, 100);
            matrix[i][HS_VOLUME_COL] = randomiBool();
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = randomiBool();
        }

        //stock raw => enable
        for (int i = N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE ; i < globalROW; i++) {
            matrix[i][HS_COL] = FALSEBOOL;
            matrix[i][HS_PERIOD_COL] = 0;
            matrix[i][HS_VOLUME_COL] = FALSEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = FALSEBOOL;
        }
    }

    /**
     * Generate a random matrix validator for features selection
     */
    public void generateCompleteModel(Integer notuse) {
        //hs stock => enable
        //stock sector => enable
        //all indice => enable
        //raw and other => disable

        matrix[HS_POS][HS_COL] = TRUEBOOL;
        matrix[HS_POS][HS_PERIOD_COL] = DEFAULTPERIOD;
        matrix[HS_POS][HS_VOLUME_COL] = TRUEBOOL;
        for (int j = N_HS_COL; j < globalCOL; j++)
            matrix[HS_POS][j] = TRUEBOOL;

        //stock sector => disable
        for (int i = N_HS ; i < N_HS+CacheStockSector.N_SECTOR; i++) {
            matrix[i][HS_COL] = TRUEBOOL;
            matrix[i][HS_PERIOD_COL] = DEFAULTPERIOD;
            matrix[i][HS_VOLUME_COL] = FALSEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = TRUEBOOL;
        }

        //stock indice => enable
        for (int i = N_HS+CacheStockSector.N_SECTOR ; i < N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE; i++) {
            matrix[i][HS_COL] = TRUEBOOL;
            matrix[i][HS_PERIOD_COL] = DEFAULTPERIOD;
            matrix[i][HS_VOLUME_COL] = FALSEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = TRUEBOOL;
        }

        //stock raw => enable
        for (int i = N_HS+CacheStockSector.N_SECTOR+CacheStockIndice.N_INDICE ; i < globalROW; i++) {
            matrix[i][HS_COL] = TRUEBOOL;
            matrix[i][HS_PERIOD_COL] = DEFAULTPERIOD;
            matrix[i][HS_VOLUME_COL] = FALSEBOOL;
            for (int j = N_HS_COL; j < globalCOL; j++)
                matrix[i][j] = TRUEBOOL;
        }
    }


    public boolean optimizeModel(MatrixValidator mv) {
        if (col >= globalROW) return false;

        mv.matrix[col][HS_COL] = TRUEBOOL;
        mv.matrix[col][HS_PERIOD_COL] = DEFAULTPERIOD;
        mv.matrix[col][HS_VOLUME_COL] = TRUEBOOL;
        for (int j = N_HS_COL; j < globalCOL; j++)
            mv.matrix[col][j] = TRUEBOOL;
        col = col + 1;
        return true;
    }

    public void revertModel(MatrixValidator mv) {
        mv.matrix[col-1][HS_COL] = FALSEBOOL;
    }


    /**
     * Save matrix in influx db
     * @param code
     * @param error
     * @param rate
     */
    public void save(String code, int error, double rate) {
        BatchPoints bp = InfluxDaoConnector.getBatchPointsV1(dbNamePerf);

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

        try {
            String query = "SELECT * FROM " + code;
            QueryResult result = InfluxDaoConnector.getPoints(query, dbNameModel);

            int size = result.getResults().get(0).getSeries().get(0).getValues().size() - 1;

            for (int i = 0; i < globalROW; i++) {
                for (int j = 0; j < globalCOL; j++) {
                    matrix[i][j] = new Integer(((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(i * globalCOL + j + 1))).intValue());
                }
            }

            int index = globalROW * globalCOL + 1;
            this.error = (double) result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++);
            this.maxBins = new Integer(((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
            this.maxDepth = new Integer(((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
            this.numTrees = new Integer(((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
            this.rate = ((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue();
            this.seed = new Integer(((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
            this.vectorSize = new Integer(((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
            this.vSize = new Integer(((Double) (result.getResults().get(0).getSeries().get(0).getValues().get(size).get(index++))).intValue());
        } catch (Exception e) {

        }
    }

    public void saveModel(String code) {
        BatchPoints bp = InfluxDaoConnector.getBatchPointsV1(dbNameModel);
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
        return matrix[indice][HS_COL] == 1;
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
        return matrix[indice][HS_VOLUME_COL] == 1;
    }




    /**
     * get matrix COL_MMA20_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMMA20(int indice) {
        return matrix[indice][StockAnalyse.COL_MMA20_POS + N_HS_COL] == 1;
    }

    /**
     * get matrix COL_MMA50_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMMA50(int indice) {
        return matrix[indice][StockAnalyse.COL_MMA50_POS + N_HS_COL] == 1;
    }

    /**
     * get matrix COL_MME12_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMME12(int indice) {
        return matrix[indice][StockAnalyse.COL_MME12_POS + N_HS_COL] == 1;
    }

    /**
     * get matrix COL_MME26_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMME26(int indice) {
        return matrix[indice][StockAnalyse.COL_MME26_POS + N_HS_COL] == 1;
    }

    /**
     * get matrix COL_MACD_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMACD(int indice) {
        return matrix[indice][StockAnalyse.COL_MACD_POS + N_HS_COL] == 1;
    }

    /**
     * get matrix COL_MACD_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATMOMENTUM(int indice) {
        return matrix[indice][StockAnalyse.COL_MOMENTUM_POS + N_HS_COL] == 1;
    }

    /**
     * get matrix COL_STDDEV_POS for @indice
     * @param indice
     * @return
     */
    public boolean getATSTDDEV(int indice) {
        return matrix[indice][StockAnalyse.COL_STDDEV_POS + N_HS_COL] == 1;
    }



    public int getIndice(int indice, TypeHistory type) {
        if (type == TypeHistory.SEC) return indice + N_HS;
        if (type == TypeHistory.IND) return indice + N_HS + CacheStockSector.N_SECTOR;
        if (type == TypeHistory.RAW) return indice + N_HS + CacheStockSector.N_SECTOR + CacheStockIndice.N_INDICE;
        return 0;
    }

}
