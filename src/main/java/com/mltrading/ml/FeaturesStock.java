package com.mltrading.ml;

import com.mltrading.models.parser.Analyse;
import com.mltrading.models.stock.*;
import org.apache.commons.lang.ArrayUtils;
import scala.Serializable;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 24/11/2015.
 */
public class FeaturesStock implements Serializable {

    private double predictionValue;

    private Double vector[];

    int currentVectorPos = 0;


    public FeaturesStock() {
        vector = new Double[20000];
    }

    public double getPredictionValue() {
        return predictionValue;
    }

    public void setPredictionValue(double predictionValue) {
        this.predictionValue = predictionValue;
    }



    public static FeaturesStock transform(StockHistory sh,double value) {
        FeaturesStock fs = new FeaturesStock();

        fs.setPredictionValue(value);

        return fs;
    }

    public double[] vectorize() {

        return ArrayUtils.toPrimitive(vector);
    }

    public  static List<FeaturesStock> transformList(List<StockHistory> shL) {
        List<FeaturesStock> fsL = new ArrayList<>();

        for (int i = 0; i< shL.size()-1; i++) {
            fsL.add(transform(shL.get(i), shL.get(i+1).getValue()));
        }


        //for (StockHistory sh:shL) {


            /*if (feature != null)
                fsL.add(transform(feature,sh.getValue()));
            try {
                feature = (StockHistory) sh.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }*/
        return fsL;
    }

    public void linearize(StockHistory sh) {
        this.vector[currentVectorPos++] = sh.getValue();
        this.vector[currentVectorPos++] = sh.getConsensusNote();
        this.vector[currentVectorPos++] = sh.getVolume();
    }

    public void linearize(StockAnalyse sa) {
        this.vector[currentVectorPos++] = sa.getMme12();
        this.vector[currentVectorPos++] = sa.getMme26();
        this.vector[currentVectorPos++] = sa.getMomentum();
        this.vector[currentVectorPos++] = sa.getStdDev();
    }

    public void linearizeSH(List<StockHistory> shl) {
        for (StockHistory sh:shl)
            this.vector[currentVectorPos++] = sh.getValue();
    }

    public void linearizeSS(List<StockSector> shl) {
        for (StockSector sh:shl)
            this.vector[currentVectorPos++] = sh.getValue();
    }

    public void linearizeSI(List<StockIndice> shl) {
        for (StockIndice sh:shl)
            this.vector[currentVectorPos++] = sh.getValue();
    }

    static int OFFSET_BASE = 50;
    static int RANGE_MAX = 300;
    static int XT_PERIOD = 20;

    public  static List<FeaturesStock> create(Stock stock) {
        //Xt,Xt-1,...,Xn ,Consensus AT => StockHistory
        //Indice Xt,..Xn, AT => StockIndice
        //Secteur Xt,..Xn, AT => StockSecteur
        //Vola Cac Xt,..Xn, AT
        //indice etranger

        List<FeaturesStock> fsL = new ArrayList<>();
        List<String> rangeDate = StockHistory.getDateHistoryListOffsetLimit(stock.getCode(), OFFSET_BASE,RANGE_MAX);

        for (String date: rangeDate) {
            FeaturesStock fs = new FeaturesStock();
            StockHistory  res = StockHistory.getStockHistoryDayAfter(stock.getCode(), date);
            fs.setPredictionValue(res.getValue());

            /**
             * stock
             */
            List<StockHistory> sh = StockHistory.getStockHistoryDateInvert(stock.getCode(), date, XT_PERIOD);
            fs.linearizeSH(sh);
            StockHistory current = StockHistory.getStockHistory(stock.getCode(), date);
            fs.linearize(current);
            StockAnalyse ash = StockAnalyse.getAnalyse(stock.getCode(), date);
            fs.linearize(ash);

            /**
             * sector
             */
            List<StockSector> ss = StockSector.getStockSectorDateInvert(stock.getSector(), date, XT_PERIOD);
            fs.linearizeSS(ss);
            StockAnalyse ass = StockAnalyse.getAnalyse(stock.getSector(), date);
            fs.linearize(ass);

            /**
             * indice
             */
            String codeIndice = StockIndice.translate(stock.getIndice());
            List<StockIndice> si = StockIndice.getStockIndiceDateInvert(codeIndice, date, XT_PERIOD);
            fs.linearizeSI(si);
            StockAnalyse asi = StockAnalyse.getAnalyse(codeIndice, date);
            fs.linearize(asi);

            /**
             * volatility cac
             */
            List<StockHistory> sVCac = StockHistory.getStockHistoryDateInvert("VCAC", date, XT_PERIOD);
            fs.linearizeSH(sVCac);


            fsL.add(fs);
        }

        return fsL;
    }
}
