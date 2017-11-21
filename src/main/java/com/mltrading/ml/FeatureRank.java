package com.mltrading.ml;

import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureRank extends Feature implements Serializable{


    public FeatureRank() {
        vector = new Double[20000];
    }

    public FeatureRank(FeatureRank fs, double pred, PredictionPeriodicity t) {
        this.currentDate = fs.getCurrentDate();
        this.setDate(fs.getDate(t),t);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D1), PredictionPeriodicity.D1);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D5), PredictionPeriodicity.D5);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D20), PredictionPeriodicity.D20);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D40), PredictionPeriodicity.D40);
        this.currentValue = fs.getCurrentValue();
        this.currentVectorPos = fs.currentVectorPos;
        this.vector = fs.vector.clone();
        setPredictionValue(pred, t);
    }

    /**
     *
     * @param code isin code
     * @param codif codif
     * @param range
     * @return
     */
    public  static List<FeatureRank> create(String code, String codif, int range) {
        List<PredictionPeriodicity> periodicity = Arrays.asList(PredictionPeriodicity.D20, PredictionPeriodicity.D40);

        //log.info("create FeaturesStock for: " + codif);
        StockGeneral sg = CacheStockGeneral.getCache().get(code);

        String sector = sg.getSector();

        List<FeatureRank> frL = new ArrayList<>();

        List<String> rangeDate;
        try {
            rangeDate = StockHistory.getDateHistoryListOffsetLimit(codif, range);
        } catch (Exception e) {
            return null;
        }


        for (String date: rangeDate) {
            FeatureRank fr = new FeatureRank();
            fr.setCurrentDate(date);

            if (setResultYield(fr,periodicity,codif,date) == false) continue;

            periodicity.forEach(p -> {
                MLStocks ref = CacheMLStock.getMLStockCache().get(codif);

                double res = ref.getStatus().getAvg(p);
                fr.linearize(res);
                double error = ref.getStatus().getErrorRate(p);
                fr.linearize(error);

                double yieldPrediction = (sg.getPrediction().getPrediction(p) - sg.getValue())/ sg.getValue() * 100;
                fr.linearize(yieldPrediction);

            });

            StockHistory si = StockHistory.getStockHistory("PX1", date );
            fr.linearize(si);
            StockAnalyse asi = StockAnalyse.getAnalyse("PX1", date);
            fr.linearize(asi);

            si = StockHistory.getStockHistory(sector, date );
            fr.linearize(si);
            asi = StockAnalyse.getAnalyse(sector, date);
            fr.linearize(asi);

            si = StockHistory.getStockHistory("VCAC", date );
            fr.linearize(si);
            asi = StockAnalyse.getAnalyse(sector, date);
            fr.linearize(asi);
            new DateTime(date);
            StockDocument sd = StockDocument.getNextStockDocument(codif, date);
            int diff =check_diff( sd.getTimeInsert(),new DateTime(date));
            fr.linearize(diff);


        }

        return  frL;
    }

    public static int DAYS_BY_MONTH = 31;
    public static int DAYS_BY_YEAR = 365;
    private static int check_diff( DateTime timeInsert , DateTime timeNow ) {
        int shift = (timeNow.getYear() - timeInsert.getYear()) * DAYS_BY_YEAR;
        return (timeNow.getDayOfMonth()+timeNow.getMonthOfYear()*DAYS_BY_MONTH + shift) - (timeInsert.getDayOfMonth()+timeInsert.getMonthOfYear()*DAYS_BY_MONTH);
    }


    public void linearize(double value) {
        this.vector[currentVectorPos++] = value;
    }



    public void linearize(StockHistory sh) {
       this.vector[currentVectorPos++] = sh.getValue();
    }


    private void linearize(StockAnalyse sa) {
        this.vector[currentVectorPos++] = sa.getMma20();
        this.vector[currentVectorPos++] = sa.getMma50();
        this.vector[currentVectorPos++] = sa.getMme12();
        this.vector[currentVectorPos++] = sa.getMme26();
        this.vector[currentVectorPos++] = sa.getMacd();
        this.vector[currentVectorPos++] = sa.getMomentum();
        this.vector[currentVectorPos++] = sa.getStdDev();

    }


}
