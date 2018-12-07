package com.mltrading.ml;


import com.mltrading.models.stock.*;
import com.mltrading.models.stock.cache.CacheRawMaterial;
import com.mltrading.models.stock.cache.CacheStockIndice;
import com.mltrading.models.stock.cache.CacheStockSector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gmo on 24/11/2015.
 */
public class FeaturesStock extends Feature implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(FeaturesStock.class);









    public FeaturesStock() {
        vector = new Double[20000];
    }

    public FeaturesStock(FeaturesStock fs, double predictRes,PredictionPeriodicity t) {
        this.currentDate = fs.getCurrentDate();
        this.setDate(fs.getDate(t),t);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D1), PredictionPeriodicity.D1);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D5), PredictionPeriodicity.D5);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D20), PredictionPeriodicity.D20);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D40), PredictionPeriodicity.D40);
        this.currentValue = fs.getCurrentValue();
        this.currentVectorPos = fs.currentVectorPos;
        this.vector = fs.vector.clone();
        setPredictionValue(predictRes, t);
    }




    public static FeaturesStock transform(double value, PredictionPeriodicity t) {
        FeaturesStock fs = new FeaturesStock();

        fs.setPredictionValue(value, t);

        return fs;
    }



    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public void linearize(StockHistory sh, MatrixValidator validator) {
        this.vector[currentVectorPos++] = sh.getValue();
        if (validator.getPeriodVolume())  this.vector[currentVectorPos++] = sh.getVolume();
    }

    public void linearize(StockAnalyse sa,  MatrixValidator validator, int indice) {
        if (validator.getATMMA20(indice)) this.vector[currentVectorPos++] = sa.getMma20();
        if (validator.getATMMA50(indice)) this.vector[currentVectorPos++] = sa.getMma50();
        if (validator.getATMME12(indice)) this.vector[currentVectorPos++] = sa.getMme12();
        if (validator.getATMME26(indice)) this.vector[currentVectorPos++] = sa.getMme26();
        if (validator.getATMACD(indice)) this.vector[currentVectorPos++] = sa.getMacd();
        if (validator.getATMOMENTUM(indice)) this.vector[currentVectorPos++] = sa.getMomentum();
        if (validator.getATSTDDEV(indice)) this.vector[currentVectorPos++] = sa.getStdDev();
        if (validator.getATGARCH20(indice)) this.vector[currentVectorPos++] = sa.getGarch20();
        if (validator.getATGARCHVOL20(indice)) this.vector[currentVectorPos++] = sa.getGarch_vol_20();
        if (validator.getATGARCH50(indice)) this.vector[currentVectorPos++] = sa.getGarch50();
        if (validator.getATGARCHVOL50(indice)) this.vector[currentVectorPos++] = sa.getGarch_vol_50();
        if (validator.getATGARCH100(indice)) this.vector[currentVectorPos++] = sa.getGarch100();
        if (validator.getATGARCHVOL100(indice)) this.vector[currentVectorPos++] = sa.getGarch_vol_100();
    }





    public void linearizeNote(List<Double> dl) {
        for (Double d:dl)
            this.vector[currentVectorPos++] = d;
    }


    /**
     * * Create a FeatureStock list for current range day
     * Train model with this list
     * @param codif
     * @param validator
     * @param range
     * @return
     */
    public  static List<FeaturesStock> create(String codif, MatrixValidator validator, int range) {

        log.info("create FeaturesStock for: " + codif);

        List<FeaturesStock> fsL = new ArrayList<>();

        List<String> rangeDate;
        try {
            rangeDate = StockHistory.getDateHistoryListOffsetLimit(codif, range);
            if (rangeDate.size() < range * 0.7) { //why this code ????
                log.error("Cannot get date list for: " + codif + " not enough element");
                return null;
            }
        } catch (Exception e) {
            log.error("Cannot get date list for: " + codif + "  //exception:" + e);
            return null;
        }


        for (String date: rangeDate) {
            FeaturesStock fs = new FeaturesStock();
            fs.setCurrentDate(date);


            if (setResult(fs,PeriodicityList.periodicity,codif,date) == false) continue;

            /* remove after check
            try {
                periodicity.forEach(p -> {

                    final StockHistory res = StockHistory.getStockHistoryDayOffset(codif, date, PredictionPeriodicity.convert(p));
                    if (res != null) {
                        fs.setResultValue(res.getCurrentValue(), p);
                        fs.setDate(res.getDay(), p);
                    } else {
                        fs.setResultValue(0., p);
                        fs.setDate("J+N", p);
                    }});


            } catch (Exception e) {
                log.error("Cannot get date for: " + codif + " and date: " + date + " //exception:" + e);
                continue;
            }*/

            /**
             * stock
             */
            try {
                List<StockHistory> sh = StockHistory.getStockHistoryDateInvert(codif, date, validator.getPeriodStockHist());
                fs.linearize(sh, validator.getPeriodVolume());
                StockHistory current = StockHistory.getStockHistory(codif, date);
                //fs.linearize(current, validator); already done
                fs.setCurrentValue(current.getValue());

            } catch (Exception e) {
                log.error("Cannot get stock history for: " + codif + " and date: " + date + " range: " + validator.getPeriodStockHist() + " //exception:" + e);
                throw  e ;
            }


            try {
                StockAnalyse ash = StockAnalyse.getAnalyse(codif, date);
                fs.linearize(ash, validator, MatrixValidator.HS_POS);

            } catch (Exception e) {
                log.error("Cannot get analyse stock for: " + codif + " and date: " + date +  " //exception:" + e);
                throw  e ;
            }

            featureGenericParam(fs, codif, validator, date);



            /** add in index ???
             * volatility cac
             **/
           /* if (validator.getVCAC()) {
                try {
                    List<StockHistory> sVCac = StockHistory.getStockHistoryDateInvert("VCAC", date, validator.getVCACHist());
                    fs.linearize(sVCac,false);

                } catch (Exception e) {
                    log.error("Cannot get vcac stock for: " + codif + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }*/


            fsL.add(fs);
        }

        return fsL;
    }


    /**
     * Create a FeatureStock list for prediction
     * use each day to refresh data with current model
     * @param codif
     * @param validator
     * @param date
     * @return
     */
    public static FeaturesStock createRT(String codif, MatrixValidator validator, String date) {


        log.info("create FeaturesStock for: " + codif);


        FeaturesStock fs = new FeaturesStock();
        fs.setCurrentDate(date);

        /**
         * stock
         */
        try {
            List<StockHistory> sh = StockHistory.getStockHistoryDateInvert(codif, date, validator.getPeriodStockHist());
            fs.linearize(sh,validator.getPeriodVolume());
            StockHistory current = StockHistory.getStockHistory(codif, date);
            //fs.linearize(current, validator);
            fs.setCurrentValue(current.getValue());

        } catch (Exception e) {
            log.error("Cannot get stock history for: " + codif + " and date: " + date + " //exception:" + e);
            return null;
        }


        try {
            StockAnalyse ash = StockAnalyse.getAnalyse(codif, date);
            fs.linearize(ash, validator, MatrixValidator.HS_POS);

        } catch (Exception e) {
            log.error("Cannot get analyse stock for: " + codif + " and date: " + date + " //exception:" + e);
            return null;
        }


        featureGenericParam(fs, codif, validator, date);

/** add in index ???
 * volatility cac
 **/
        /*if (validator.getVCAC()) {
            try {
                List<StockHistory> sVCac = StockHistory.getStockHistoryDateInvert("VCAC", date, validator.getVCACHist());
                fs.linearize(sVCac,false);

            } catch (Exception e) {
                log.error("Cannot get vcac stock for: " + codif + " and date: " + date + " //exception:" + e);
                throw  e ;
            }
        }*/


        return fs;
    }


    /**
     *
     * @param fs
     * @param codif
     * @param validator
     * @param date
     */
    public static void featureGenericParam(FeaturesStock fs, String codif, MatrixValidator validator,String date) {
        /**
         * sector
         */
        try {

            /** ALL sector*/
            for (StockSector g : CacheStockSector.getSectorCache().values()) {
                int row = validator.getIndice(g.getRow(), MatrixValidator.TypeHistory.SEC);
                //if (row != rowS) { //if same row, sector of this stock already done
                log.trace("get sector " + g.getCodif() +"stock for: " + codif + " and date: " + date);
                filledFeaturesStock(fs,validator,row,date,g.getCodif());
            }



            /** ALL indice*/
            for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {
                int row = validator.getIndice(g.getRow(), MatrixValidator.TypeHistory.IND);
                log.trace("get indice " + g.getCodif() +"stock for: " + codif + " and date: " + date);
                filledFeaturesStock(fs,validator,row,date,g.getCodif());
            }

            /** ALL raw*/
            for (StockRawMat g : CacheRawMaterial.getCache().values()) {
                int row = validator.getIndice(g.getRow(), MatrixValidator.TypeHistory.RAW);
                log.trace("get raw " + g.getCodif() +"stock for: " + codif + " and date: " + date);
                filledFeaturesStock(fs,validator,row,date,g.getCodif());
            }

        } catch (Exception e) {
            log.error("Cannot get sector/indicd/raw/analyse stock for: " + codif + " and date: " + date + " //exception:" + e );
            throw  e ;
        }


    }

    private static void filledFeaturesStock(FeaturesStock fs, MatrixValidator validator, int row, String date, String code) {
        if (validator.getPeriodEnable(row)) {
            List<StockHistory> si = StockHistory.getStockHistoryDateInvert(code, date, validator.getPeriodHist(row));
            fs.linearize(si, validator.getPeriodVolume());
            StockAnalyse asi = StockAnalyse.getAnalyse(code, si.get(0).getDay());
            fs.linearize(asi, validator, row);
        }
    }


}
