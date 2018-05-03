package com.mltrading.ml;

import com.mltrading.models.stock.StockHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.util.List;

public abstract class Feature implements Serializable {

    protected String currentDate;
    protected String dateD1;
    protected String dateD5;
    protected String dateD20;
    protected String dateD40;

    protected double resultValueD1 = 0;
    protected double resultValueD5 = 0;
    protected double resultValueD20 = 0;
    protected double resultValueD40 = 0;
    protected double predictionValueD1 = 0;
    protected double predictionValueD5 = 0;
    protected double predictionValueD20 = 0;
    protected double predictionValueD40 = 0;
    protected double currentValue;

    protected Double vector[];

    public int currentVectorPos = 0;

    private static final Logger log = LoggerFactory.getLogger(Feature.class);


    public String getDate(PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                return dateD1;
            case D5:
                return dateD5;
            case D20:
                return dateD20;
            case D40:
                return dateD40;
        }
        //default
        return "";
    }

    public void setDate(String date, PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                dateD1 = date;
                break;
            case D5:
                dateD5 = date;
                break;
            case D20:
                dateD20 = date;
                break;
            case D40:
                dateD40 = date;
                break;
        }
    }


    public Double getResultValue(PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                return resultValueD1;
            case D5:
                return resultValueD5;
            case D20:
                return resultValueD20;
            case D40:
                return resultValueD40;
        }

        //default
        return new Double(0);
    }

    public void setResultValue(double resultValue, PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                resultValueD1 = resultValue;
                break;
            case D5:
                resultValueD5 = resultValue;
                break;
            case D20:
                resultValueD20 = resultValue;
                break;
            case D40:
                resultValueD40 = resultValue;
                break;
        }
    }

    public void setPredictionValue(double predictionValue, PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                predictionValueD1 = predictionValue;
                break;
            case D5:
                predictionValueD5 = predictionValue;
                break;
            case D20:
                predictionValueD20 = predictionValue;
                break;
            case D40:
                predictionValueD40 = predictionValue;
                break;
        }
    }


    public double getPredictionValue( PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                return predictionValueD1;
            case D5:
                return predictionValueD5;
            case D20:
                return predictionValueD20;
            case D40:
                return predictionValueD40;
        }

        //default
        return 0;
    }



    public Double[] getVector() {
        return vector;
    }



    public double getCurrentValue() {
        return currentValue;
    }




    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double[] vectorize() {

        double[] result = new double[currentVectorPos];

        for(int i = 0; i < currentVectorPos; ++i) {
            try {
                result[i] = vector[i].doubleValue();
            } catch (NullPointerException npe) {
                result[i] = 0;
            }
        }

        return result;
    }


    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void linearize(List<? extends StockHistory> shL, boolean bVolume) {
        for (StockHistory sh:shL) {
            this.vector[currentVectorPos++] = sh.getValue();
            if (bVolume)  this.vector[currentVectorPos++] = sh.getVolume();
        }
    }


    protected static boolean setResult(Feature fs, List<PredictionPeriodicity> periodicity, String codif, String date) {
        try {
            periodicity.forEach(p -> {

                final StockHistory res = StockHistory.getStockHistoryDayOffset(codif, date, PredictionPeriodicity.convert(p));
                if (res != null) {
                    fs.setResultValue(res.getValue(), p);
                    fs.setDate(res.getDay(), p);
                } else {
                    fs.setResultValue(0., p);
                    fs.setDate("J+N", p);
                }});


        } catch (Exception e) {
            log.error("Cannot get date for: " + codif + " and date: " + date + " //exception:" + e);
            return false;
        }

        return true;

    }

    protected static boolean setResultYield(Feature fs, List<PredictionPeriodicity> periodicity, String codif, String date) {
        try {
            final StockHistory cur = StockHistory.getStockHistoryDayOffset(codif, date, 0);
            periodicity.forEach(p -> {

                final StockHistory res = StockHistory.getStockHistoryDayOffset(codif, date, PredictionPeriodicity.convert(p));
                if (res != null) {
                    fs.setResultValue((res.getValue()-cur.getValue())/cur.getValue()*100, p);
                    fs.setDate(res.getDay(), p);
                } else {
                    fs.setResultValue(0., p);
                    fs.setDate("J+N", p);
                }});


        } catch (Exception e) {
            log.error("Cannot get date for: " + codif + " and date: " + date + " //exception:" + e);
            return false;
        }

        return true;

    }


}
