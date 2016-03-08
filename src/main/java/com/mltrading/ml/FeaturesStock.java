package com.mltrading.ml;


import com.mltrading.models.stock.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 24/11/2015.
 */
public class FeaturesStock implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(FeaturesStock.class);

    private String currentDate;
    private String dateD1;
    private String dateD5;
    private String dateD20;

    private double resultValueD1 = 0;
    private double resultValueD5 = 0;
    private double resultValueD20 = 0;
    private double predictionValueD1 = 0;
    private double predictionValueD5 = 0;
    private double predictionValueD20 = 0;
    private double currentValue;

    private Double vector[];

    int currentVectorPos = 0;
    int normalVectorPos = 0;


    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    /** Controls the perdiod */
    public enum PredictionPeriodicity {
        /** 1 day */
        D1,
        /** 5 days    mid range */
        D5,
        /** 20 days   long range */
        D20
    }



    public FeaturesStock() {
        vector = new Double[20000];
    }

    public FeaturesStock(FeaturesStock fs, double predictRes,PredictionPeriodicity t) {
        this.currentDate = fs.getCurrentDate();
        this.setDate(fs.getDate(t),t);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D1), PredictionPeriodicity.D1);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D5), PredictionPeriodicity.D5);
        setResultValue(fs.getResultValue(PredictionPeriodicity.D20), PredictionPeriodicity.D20);
        this.currentValue = fs.getCurrentValue();
        this.currentVectorPos = fs.currentVectorPos;
        this.vector = fs.vector.clone();
        setPredictionValue(predictRes, t);
    }


    public String getDate(PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                return dateD1;
            case D5:
                return dateD5;
            case D20:
                return dateD20;
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
        }
    }


    public double getResultValue(PredictionPeriodicity t) {
        switch (t) {
            case D1 :
                return resultValueD1;
            case D5:
                return resultValueD5;
            case D20:
                return resultValueD20;
        }

        //default
        return 0;
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
        }

        //default
        return 0;
    }



    public Double[] getVector() {
        return vector;
    }

    public int getCurrentVectorPos() {
        return currentVectorPos;
    }



    public double getCurrentValue() {
        return currentValue;
    }




    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public static FeaturesStock transform(StockHistory sh, double value, PredictionPeriodicity t) {
        FeaturesStock fs = new FeaturesStock();

        fs.setPredictionValue(value, t);

        return fs;
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public  static List<FeaturesStock> transformList(List<StockHistory> shL, PredictionPeriodicity t) {
        List<FeaturesStock> fsL = new ArrayList<>();

        for (int i = 0; i< shL.size()-1; i++) {
            fsL.add(transform(shL.get(i), shL.get(i+1).getValue(), t));
        }

        return fsL;
    }

    public void linearize(StockHistory sh, Validator validator) {
        this.vector[currentVectorPos++] = sh.getValue();
        if (validator.historyConsensus) this.vector[currentVectorPos++] = sh.getConsensusNote();
        if (validator.historyVolume)  this.vector[currentVectorPos++] = sh.getVolume();
    }

    public void linearize(StockAnalyse sa,  Validator validator) {
        if (validator.analyseMme12) this.vector[currentVectorPos++] = sa.getMme12();
        if (validator.analyseMme26) this.vector[currentVectorPos++] = sa.getMme26();
        if (validator.analyseMomentum) this.vector[currentVectorPos++] = sa.getMomentum();
        if (validator.analyseStdDev) this.vector[currentVectorPos++] = sa.getStdDev();
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

    public void linearizeSR(List<StockRawMat> shl) {
        for (StockRawMat sh:shl)
            this.vector[currentVectorPos++] = sh.getValue();
    }

    static int OFFSET_BASE = 50;
    static int RANGE_MAX = 300;
    static int RANGE_TEST = 90;


    public  static List<FeaturesStock> create(StockGeneral stock, Validator validator, int range) {
        //Xt,Xt-1,...,Xn ,Consensus AT => StockHistory
        //Indice Xt,..Xn, AT => StockIndice
        //Secteur Xt,..Xn, AT => StockSecteur
        //Vola Cac Xt,..Xn, AT
        //indice etranger

        log.info("create FeaturesStock for: " + stock.getCodif());

        List<FeaturesStock> fsL = new ArrayList<>();

        List<String> rangeDate = null;
        try {
            rangeDate = StockHistory.getDateHistoryListOffsetLimit(stock.getCode(), OFFSET_BASE, range);
            if (rangeDate.size() < range * 0.7) { //why this code ????
                log.error("Cannot get date list for: " + stock.getCode() + " not enough element");
                return null;
            }
        } catch (Exception e) {
            log.error("Cannot get date list for: " + stock.getCode() + "  //exception:" + e);
            return null;
        }

        for (String date: rangeDate) {
            FeaturesStock fs = new FeaturesStock();
            fs.setCurrentDate(date);


            try {
                StockHistory  res = StockHistory.getStockHistoryDayAfter(stock.getCode(), date);
                fs.setResultValue(res.getValue(),PredictionPeriodicity.D1);
                fs.setDate(res.getDay(),PredictionPeriodicity.D1);

                //cannot catch exception for this perdiod
                res = StockHistory.getStockHistoryDayOffset(stock.getCode(), date, 5);
                if (res != null) {
                    fs.setResultValue(res.getValue(),PredictionPeriodicity.D5);
                    fs.setDate(res.getDay(),PredictionPeriodicity.D5);
                }
                res = StockHistory.getStockHistoryDayOffset(stock.getCode(), date, 20);
                if (res != null) {
                    fs.setResultValue(res.getValue(),PredictionPeriodicity.D20);
                    fs.setDate(res.getDay(),PredictionPeriodicity.D20);
                }
            } catch (Exception e) {
                log.error("Cannot get date for: " + stock.getCode() + " and date: " + date + " //exception:" + e);
                continue;
            }

            /**
             * stock
             */
            try {
            List<StockHistory> sh = StockHistory.getStockHistoryDateInvert(stock.getCode(), date, validator.perdiodHist);
            fs.linearizeSH(sh);
            StockHistory current = StockHistory.getStockHistory(stock.getCode(), date);
            fs.linearize(current, validator);
            fs.setCurrentValue(current.getValue());

            } catch (Exception e) {
                log.error("Cannot get stock history for: " + stock.getCode() + " and date: " + date +  " //exception:" + e);
                throw  e ;
            }


            try {
                StockAnalyse ash = StockAnalyse.getAnalyse(stock.getCode(), date);
                fs.linearize(ash, validator);

            } catch (Exception e) {
                log.error("Cannot get analyse stock for: " + stock.getCode() + " and date: " + date +  " //exception:" + e);
                throw  e ;
            }

            /**
             * sector
             */
            try {
                List<StockSector> ss = StockSector.getStockSectorDateInvert(stock.getSector(), date, validator.perdiodSector);
                fs.linearizeSS(ss);
                if (validator.sectorAT) {
                    StockAnalyse ass = StockAnalyse.getAnalyse(stock.getSector(), date);
                    fs.linearize(ass, validator);
                }
            } catch (Exception e) {
                log.error("Cannot get sector/analyse stock for: " + stock.getSector() + " and date: " + date + " //exception:" + e);
                throw  e ;
            }


            /**
             * indice cac
             */
            try {


                if (validator.cac) {
                    List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EFCHI", date, validator.perdiodCac);
                    fs.linearizeSI(si);
                }

                if (validator.cacAT) {
                    StockAnalyse asi = StockAnalyse.getAnalyse("EFCHI", date);
                    fs.linearize(asi, validator);
                }
            } catch (Exception e) {
                log.error("Cannot get indice/analyse stock for: " + "EFCHI" + " and date: " + date +  " //exception:" + e);
                throw  e ;
            }

            /**
             * indice down jons
             */
            if (validator.indiceDJI) {
                try {

                    List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EDJI", date, validator.perdiodDJI);
                    fs.linearizeSI(si);
                    if (validator.DJIAT) {
                        StockAnalyse asi = StockAnalyse.getAnalyse("EDJI", si.get(0).getDay());
                        fs.linearize(asi, validator);
                    }
                } catch (Exception e) {
                    log.error("Cannot get indice/analyse stock for: " + "EDJI" + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * indice nikkei
             */
            if (validator.indiceN225) {
                try {

                    List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EN225", date, validator.perdiodN225);
                    fs.linearizeSI(si);
                    if (validator.N225AT) {
                        StockAnalyse asi = StockAnalyse.getAnalyse("EN225", si.get(0).getDay());
                        fs.linearize(asi, validator);
                    }
                } catch (Exception e) {
                    log.error("Cannot get indice/analyse stock for: " + "EN225" + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * indice FTSE london
             */
            if (validator.indiceFTSE) {
                try {

                    List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EFTSE", date, validator.perdiodFTSE);
                    fs.linearizeSI(si);
                    if (validator.FTSEAT) {
                        StockAnalyse asi = StockAnalyse.getAnalyse("EFTSE", si.get(0).getDay());
                        fs.linearize(asi, validator);
                    }
                } catch (Exception e) {
                    log.error("Cannot get indice/analyse stock for: " + "EFTSE" + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * indice DAX london
             */
            if (validator.indiceDAX) {
                try {

                    List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EGDAXI", date, validator.perdiodDAX);
                    fs.linearizeSI(si);
                    if (validator.DAXIAT) {
                        StockAnalyse asi = StockAnalyse.getAnalyse("EGDAXI", si.get(0).getDay());
                        fs.linearize(asi, validator);
                    }
                } catch (Exception e) {
                    log.error("Cannot get indice/analyse stock for: " + "EGDAXI" + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

           /**
             * indice STOXX50 london
             *
            if (validator.indiceSTOXX50) {
                try {

                    List<StockIndice> si = StockIndice.getStockIndiceDateInvert("ESTOXX50E", date, validator.perdiodSTOXX50);
                    fs.linearizeSI(si);
                    //log.info("find " + si.size() + " ESTOXX50E for " + validator.perdiodSTOXX50 + " and date: " + date);
                    if (validator.STOXX50AT) {
                        StockAnalyse asi = StockAnalyse.getAnalyse("ESTOXX50E", si.get(0).getDay());
                        //log.info("find " + asi.toString() + " ESTOXX50E for " + validator.perdiodSTOXX50 + " and date: " + si.get(0).getDay());
                        fs.linearize(asi, validator);
                    }
                } catch (Exception e) {
                    log.error("Cannot get indice/analyse stock for: " + "ESTOXX50E" + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }   */



            /**
             * volatility cac
             */
            if (validator.cacVola) {
                try {
                    List<StockIndice> sVCac = StockIndice.getStockIndiceDateInvert("VCAC", date, validator.perdiodcacVola);
                    fs.linearizeSI(sVCac);

                } catch (Exception e) {
                    log.error("Cannot get vcac stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * taux de change $/€
             */
            if (validator.DOLLAR) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("DTOE", date, validator.perdiodDOLLAR);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get dollar stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * prix du petrole
             */
            if (validator.PETROL) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("PETB", date, validator.perdiodPETROL);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get petrol stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }


            /**
             * Euribor 1 mois
             */
            if (validator.EURI1M) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("EURI1M", date, validator.perdiodEURI1M);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get euribord 1 month stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * Euribor 1 année
             */
            if (validator.EURI1Y) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("EURI1Y", date, validator.perdiodEURI1Y);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get euribord 1 year stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * Euribor 10 année
             */
            if (validator.EURI10Y) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("EURI10Y", date, validator.perdiodEURI10Y);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get euribord 10 years stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }


            /**
             * Usribor 1 mois
             */
            if (validator.USRI1M) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("USRI1M", date, validator.perdiodUSRI1M);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get usribord 1 month stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * Usribor 1 année
             */
            if (validator.USRI1Y) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("USRI1Y", date, validator.perdiodUSRI1Y);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get usribord 1 year stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }

            /**
             * Usribor 10 année
             */
            if (validator.USRI10Y) {
                try {
                    List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("USRI10Y", date, validator.perdiodUSRI10Y);
                    fs.linearizeSR(sDE);

                } catch (Exception e) {
                    log.error("Cannot get usribord 10 years stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    throw  e ;
                }
            }



            fsL.add(fs);
        }

        return fsL;
    }


    public static FeaturesStock createRT(StockGeneral stock, Validator validator, String date) {
        //Xt,Xt-1,...,Xn ,Consensus AT => StockHistory
        //Indice Xt,..Xn, AT => StockIndice
        //Secteur Xt,..Xn, AT => StockSecteur
        //Vola Cac Xt,..Xn, AT
        //indice etranger

        log.info("create FeaturesStock for: " + stock.getCodif());


        FeaturesStock fs = new FeaturesStock();
        fs.setCurrentDate(date);

        /**
         * stock
         */
        try {
            List<StockHistory> sh = StockHistory.getStockHistoryDateInvert(stock.getCode(), date, validator.perdiodHist);
            fs.linearizeSH(sh);
            StockHistory current = StockHistory.getStockHistory(stock.getCode(), date);
            fs.linearize(current, validator);
            fs.setCurrentValue(current.getValue());

        } catch (Exception e) {
            log.error("Cannot get stock history for: " + stock.getCode() + " and date: " + date + " //exception:" + e);
            return null;
        }


        try {
            StockAnalyse ash = StockAnalyse.getAnalyse(stock.getCode(), date);
            fs.linearize(ash, validator);

        } catch (Exception e) {
            log.error("Cannot get analyse stock for: " + stock.getCode() + " and date: " + date + " //exception:" + e);
            return null;
        }

        /**
         * sector
         */
        try {
            List<StockSector> ss = StockSector.getStockSectorDateInvert(stock.getSector(), date, validator.perdiodSector);
            fs.linearizeSS(ss);
            if (validator.sectorAT) {
                StockAnalyse ass = StockAnalyse.getAnalyse(stock.getSector(), date);
                fs.linearize(ass, validator);
            }
        } catch (Exception e) {
            log.error("Cannot get sector/analyse stock for: " + stock.getSector() + " and date: " + date + " //exception:" + e);
            return null;
        }

        /**
         * indice cac
         */
        try {
            if (validator.cac) {
                List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EFCHI", date, validator.perdiodCac);
                fs.linearizeSI(si);
            }

            if (validator.cacAT) {
                StockAnalyse asi = StockAnalyse.getAnalyse("EFCHI", date);
                fs.linearize(asi, validator);
            }
        } catch (Exception e) {
            log.error("Cannot get indice/analyse stock for: " + "EFCHI" + " and date: " + date + " //exception:" + e);
            return null;
        }

        /**
         * indice down jons
         */
        if (validator.indiceDJI) {
            try {

                List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EDJI", date, validator.perdiodDJI);
                fs.linearizeSI(si);
                if (validator.DJIAT) {
                    StockAnalyse asi = StockAnalyse.getAnalyse("EDJI", si.get(0).getDay());
                    fs.linearize(asi, validator);
                }
            } catch (Exception e) {
                log.error("Cannot get indice/analyse stock for: " + "EDJI" + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * indice nikkei
         */
        if (validator.indiceN225) {
            try {

                List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EN225", date, validator.perdiodN225);
                fs.linearizeSI(si);
                if (validator.N225AT) {
                    StockAnalyse asi = StockAnalyse.getAnalyse("EN225", si.get(0).getDay());
                    fs.linearize(asi, validator);
                }
            } catch (Exception e) {
                log.error("Cannot get indice/analyse stock for: " + "EN225" + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * indice FTSE london
         */
        if (validator.indiceFTSE) {
            try {

                List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EFTSE", date, validator.perdiodFTSE);
                fs.linearizeSI(si);
                if (validator.FTSEAT) {
                    StockAnalyse asi = StockAnalyse.getAnalyse("EFTSE", si.get(0).getDay());
                    fs.linearize(asi, validator);
                }
            } catch (Exception e) {
                log.error("Cannot get indice/analyse stock for: " + "EFTSE" + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * indice DAX london
         */
        if (validator.indiceDAX) {
            try {

                List<StockIndice> si = StockIndice.getStockIndiceDateInvert("EGDAXI", date, validator.perdiodDAX);
                fs.linearizeSI(si);
                if (validator.DAXIAT) {
                    StockAnalyse asi = StockAnalyse.getAnalyse("EGDAXI", si.get(0).getDay());
                    fs.linearize(asi, validator);
                }
            } catch (Exception e) {
                log.error("Cannot get indice/analyse stock for: " + "EGDAXI" + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * indice STOXX50 london
         *
         if (validator.indiceSTOXX50) {
         try {

         List<StockIndice> si = StockIndice.getStockIndiceDateInvert("ESTOXX50E", date, validator.perdiodSTOXX50);
         fs.linearizeSI(si);
         if (validator.STOXX50AT) {
         StockAnalyse asi = StockAnalyse.getAnalyse("ESTOXX50E", si.get(0).getDay());
         fs.linearize(asi, validator);
         }
         } catch (Exception e) {
         log.error("Cannot get indice/analyse stock for: " + "EGDAXI" + " and date: " + date + " //exception:" + e);
         return null;
         }
         }*/


        /**
         * volatility cac
         */
        if (validator.cacVola) {
            try {
                List<StockIndice> sVCac = StockIndice.getStockIndiceDateInvert("VCAC", date, validator.perdiodcacVola);
                fs.linearizeSI(sVCac);

            } catch (Exception e) {
                log.error("Cannot get vcac stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * taux de change $/€
         */
        if (validator.DOLLAR) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("DTOE", date, validator.perdiodDOLLAR);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get dollar stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * prix du petrole
         */
        if (validator.PETROL) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("PETB", date, validator.perdiodPETROL);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get petrol stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }


        /**
         * Euribor 1 mois
         */
        if (validator.EURI1M) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("EURI1M", date, validator.perdiodEURI1M);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get euribord 1 month stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * Euribor 1 année
         */
        if (validator.EURI1Y) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("EURI1Y", date, validator.perdiodEURI1Y);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get euribord 1 year stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * Euribor 10 année
         */
        if (validator.EURI10Y) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("EURI10Y", date, validator.perdiodEURI10Y);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get euribord 10 years stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }


        /**
         * Usribor 1 mois
         */
        if (validator.USRI1M) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("USRI1M", date, validator.perdiodUSRI1M);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get usribord 1 month stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * Usribor 1 année
         */
        if (validator.USRI1Y) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("USRI1Y", date, validator.perdiodUSRI1Y);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get usribord 1 year stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        /**
         * Usribor 10 année
         */
        if (validator.USRI10Y) {
            try {
                List<StockRawMat> sDE = StockRawMat.getStockRawDateInvert("USRI10Y", date, validator.perdiodUSRI10Y);
                fs.linearizeSR(sDE);

            } catch (Exception e) {
                log.error("Cannot get usribord 10 years stock for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                return null;
            }
        }

        return fs;
    }
}
