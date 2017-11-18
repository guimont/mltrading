package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.TimeSeriesDao.DaoChecker;
import org.influxdb.dto.QueryResult;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 16/02/2016.
 */
public class MLStatus implements Serializable,DaoChecker{

    private double avgD1 = 0;
    private double avgD5 = 0;
    private double avgD20 = 0;
    private double avgD40 = 0;

    private int errorRateD1 = 0;
    private int errorRateD5 = 0;
    private int errorRateD20 = 0;
    private int errorRateD40 = 0;


    private int countD1 = 0, countD5 = 0, countD20 = 0, countD40 = 0;

    private List<MLPerformances> perfList;


    /**
     * save perf for all period
     * @param code
     */
    public void savePerf(String code) {
        for (MLPerformances perfs : perfList) {
            perfs.save(code);
        }
    }

    /**
     * save perf for specific period
     * @param code
     * @param p
     */
    public void savePerf(String code, PredictionPeriodicity p) {
        for (MLPerformances perfs : perfList) {
            perfs.save(code,p);
        }

    }


    public void saveLastPerf(String code) {
        perfList.get(perfList.size()-1).save(code);
    }

    /**
     * merge perfList
     * Warning => list have to be smaller or equal than perfList
     * @param list
     */
    public void mergeList(List<MLPerformances> list, PredictionPeriodicity period) {
        if (perfList == null) perfList = list;
        else {
            for (int i = 0; i< list.size(); i++) {
                if (list.get(i).getMl(period) != null) perfList.get(i).setMl(list.get(i).getMl(period), period);
            }
        }
    }


    public void calculeAvgPrd() {
        double avgD1 = 0, avgD5 =0, avgD20 =0, avgD40 =0;
        int errD1 = 0, errD5 =0, errD20 =0, errD40 =0;
        int countD1 = 0, countD5 = 0, countD20 = 0, countD40 = 0;
        for (MLPerformances p : perfList) {
            if (p.getMl(PredictionPeriodicity.D1) != null && p.getMl(PredictionPeriodicity.D1).getValue()!=0) {
                avgD1 += p.getMl(PredictionPeriodicity.D1).getRealyield() - p.getMl(PredictionPeriodicity.D1).getYield();
                errD1 += p.getMl(PredictionPeriodicity.D1).isSign() == false ? 1 : 0;
                countD1++;
            }

            if (p.getMl(PredictionPeriodicity.D5) != null && p.getMl(PredictionPeriodicity.D5).getValue()!=0) {
                avgD5 += p.getMl(PredictionPeriodicity.D5).getRealyield() - p.getMl(PredictionPeriodicity.D5).getYield();
                errD5 += p.getMl(PredictionPeriodicity.D5).isSign() == false ? 1 : 0;
                countD5++;
            }


            if (p.getMl(PredictionPeriodicity.D20) != null && p.getMl(PredictionPeriodicity.D20).getValue()!=0) {
                avgD20 += p.getMl(PredictionPeriodicity.D20).getRealyield() - p.getMl(PredictionPeriodicity.D20).getYield();
                errD20 += p.getMl(PredictionPeriodicity.D20).isSign() == false ? 1 : 0;
                countD20++;
            }

            if (p.getMl(PredictionPeriodicity.D40) != null && p.getMl(PredictionPeriodicity.D40).getValue()!=0) {
                avgD40 += p.getMl(PredictionPeriodicity.D40).getRealyield() - p.getMl(PredictionPeriodicity.D40).getYield();
                errD40 += p.getMl(PredictionPeriodicity.D40).isSign() == false ? 1 : 0;
                countD40++;
            }

        }

        this.setCountD1(countD1);
        this.setCountD5(countD5);
        this.setCountD20(countD20);
        this.setCountD40(countD40);

        this.setAvgD1(avgD1 / countD1 * 100);
        this.setAvgD5(avgD5 / countD5 * 100);
        this.setAvgD20(avgD20 / countD20 * 100);
        this.setAvgD40(avgD40 / countD40 * 100);

        this.setErrorRateD1(errD1);
        this.setErrorRateD5(errD5);
        this.setErrorRateD20(errD20);
        this.setErrorRateD40(errD40);

    }

    private void setErrorRateD40(int errD40) {
        this.errorRateD40 = errD40;
    }

    private void setAvgD40(double avgD40) {
        this.avgD40 = avgD40;
    }

    public double getAvgD40() {
        return avgD40;
    }

    public int getErrorRateD40() {
        return errorRateD40;
    }

    public double getAvgD1() {
        return avgD1;
    }

    public void setAvgD1(double avgD1) {
        this.avgD1 = avgD1;
    }

    public double getAvgD5() {
        return avgD5;
    }

    public void setAvgD5(double avgD5) {
        this.avgD5 = avgD5;
    }

    public double getAvgD20() {
        return avgD20;
    }

    public void setAvgD20(double avgD20) {
        this.avgD20 = avgD20;
    }

    public int getErrorRateD1() {
        return errorRateD1;
    }

    public void setErrorRateD1(int errorRateD1) {
        this.errorRateD1 = errorRateD1;
    }

    public int getErrorRateD5() {
        return errorRateD5;
    }

    public void setErrorRateD5(int errorRateD5) {
        this.errorRateD5 = errorRateD5;
    }

    public int getErrorRateD20() {
        return errorRateD20;
    }

    public void setErrorRateD20(int errorRateD6) {
        this.errorRateD20 = errorRateD6;
    }

    public int getCountD1() {
        return countD1;
    }

    public void setCountD1(int countD1) {
        this.countD1 = countD1;
    }

    public int getCountD5() {
        return countD5;
    }

    public void setCountD5(int countD5) {
        this.countD5 = countD5;
    }

    public int getCountD20() {
        return countD20;
    }

    public void setCountD20(int countD20) {
        this.countD20 = countD20;
    }

    public int getCountD40() {
        return countD40;
    }

    public void setCountD40(int countD40) {
        this.countD40 = countD40;
    }

    public List<MLPerformances> getPerfList() {
        return perfList;
    }

    public void replaceElementList(List<MLPerformances> rep, PredictionPeriodicity period) throws Exception {

        try {
            for (int i = 0; i < this.getPerfList().size(); i++) {
                this.getPerfList().get(i).setMl(rep.get(i).getMl(period), period);
            }
        }
        catch (Exception e) {
            System.out.println("fuck off: " + period + " " +this.getPerfList());

        }
    }

    /**
     * return last max StockHistory
     * @param code
     * @return O or max last StockHistory
     */
    public boolean loadPerf(final String code) {
        final int max = CacheMLStock.RENDERING;
        perfList = new ArrayList();

        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +"PD1 where time > '2015-06-01T00:00:00Z'";
        QueryResult listP1 = InfluxDaoConnector.getPoints(query, MatrixValidator.dbNameModel);
        query = "SELECT * FROM "+code +"PD5 where time > '2015-06-01T00:00:00Z'";
        QueryResult listP5 = InfluxDaoConnector.getPoints(query, MatrixValidator.dbNameModel);
        query = "SELECT * FROM "+code +"PD20 where time > '2015-06-01T00:00:00Z'";
        QueryResult listP20 = InfluxDaoConnector.getPoints(query, MatrixValidator.dbNameModel);
        query = "SELECT * FROM "+code +"PD40 where time > '2015-06-01T00:00:00Z'";
        QueryResult listP40 = InfluxDaoConnector.getPoints(query, MatrixValidator.dbNameModel);

        if (checker(listP1) == false) return false;
        if (checker(listP5) == false) return false;
        if (checker(listP20) == false) return false;
        if (checker(listP40) == false) return false;

        int sizeP1 = listP1.getResults().get(0).getSeries().get(0).getValues().size();
        int sizeP5 = listP5.getResults().get(0).getSeries().get(0).getValues().size();
        int sizeP20 = listP20.getResults().get(0).getSeries().get(0).getValues().size();
        int sizeP40 = listP40.getResults().get(0).getSeries().get(0).getValues().size();

        /*if (sizeP1 < max)
            return ;*/

        for (int i = sizeP1-max; i < sizeP1; i++) {
            MLPerformances mlps = new MLPerformances();

            populate(mlps.getMl(PredictionPeriodicity.D1), listP1, i);
            if (i < sizeP5 ) populate(mlps.getMl(PredictionPeriodicity.D5), listP5, i); else mlps.setMl(null, PredictionPeriodicity.D5);
            if (i < sizeP20 ) populate(mlps.getMl(PredictionPeriodicity.D20), listP20, i);  else mlps.setMl(null, PredictionPeriodicity.D20);
            if (i < sizeP40 ) populate(mlps.getMl(PredictionPeriodicity.D40), listP40, i);  else mlps.setMl(null, PredictionPeriodicity.D40);
            mlps.setDate(mlps.getMl(PredictionPeriodicity.D1).getDate());

            perfList.add(mlps);

        }

        calculeAvgPrd();

        return true;

    }


    static public int DATE_COLUMN = 1;
    static public int ERROR_COLUMN = 2;
    static public int PREDICTION_COLUMN = 3;
    static public int REALVALUE_COLUMN = 4;
    static public int REALYIELD_COLUMN = 5;
    static public int SIGN_COLUMN = 6;
    static public int VALUE_COLUMN = 7;
    static public int YIELD_COLUMN = 8;

    public static void populate(MLPerformance mlp, QueryResult meanQ, int i) {
        mlp.setDate((String) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(DATE_COLUMN));
        mlp.setError((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(ERROR_COLUMN));
        mlp.setPrediction((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(PREDICTION_COLUMN));
        mlp.setRealvalue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(REALVALUE_COLUMN));
        mlp.setRealyield((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(REALYIELD_COLUMN));
        mlp.setSign((boolean) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(SIGN_COLUMN));
        mlp.setValue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VALUE_COLUMN));
        mlp.setYield((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(YIELD_COLUMN));

    }

    public void setPerfList(List<MLPerformances> perfList) {
        this.perfList = perfList;
    }


    public void setPerfList(List<MLPerformances> perfList,PredictionPeriodicity p) throws Exception {
        if (p == PredictionPeriodicity.D1 || this.perfList == null) {
            this.perfList = perfList;
        } else replaceElementList(perfList,p);
    }


    public int getErrorRate(PredictionPeriodicity period) {
        if (period == PredictionPeriodicity.D1) return errorRateD1;
        if (period == PredictionPeriodicity.D5) return errorRateD5;
        if (period == PredictionPeriodicity.D20) return errorRateD20;
        if (period == PredictionPeriodicity.D40) return errorRateD40;

        return 0;
    }

    public double getAvg(PredictionPeriodicity period) {
        if (period == PredictionPeriodicity.D1) return avgD1;
        if (period == PredictionPeriodicity.D5) return avgD5;
        if (period == PredictionPeriodicity.D20) return avgD20;
        if (period == PredictionPeriodicity.D40) return avgD40;
        return 0;
    }

    public void setAvg(double avg, PredictionPeriodicity period) {
        if (period == PredictionPeriodicity.D1) avgD1 = avg;
        if (period == PredictionPeriodicity.D5) avgD5= avg;
        if (period == PredictionPeriodicity.D20) avgD20= avg;
        if (period == PredictionPeriodicity.D40) avgD40= avg;
    }

    public void setErrorRate(int errorRate, PredictionPeriodicity period) {
        if (period == PredictionPeriodicity.D1) errorRateD1 = errorRate;
        if (period == PredictionPeriodicity.D5) errorRateD5= errorRate;
        if (period == PredictionPeriodicity.D20) errorRateD20= errorRate;
        if (period == PredictionPeriodicity.D40) errorRateD40= errorRate;
    }


    @Override
    public MLStatus clone()  {
        MLStatus cloneObject = new MLStatus();
        cloneObject.avgD1 = this.avgD1;
        cloneObject.avgD20 = this.avgD20;
        cloneObject.avgD5 = this.avgD5;
        cloneObject.avgD40 = this.avgD40;
        cloneObject.errorRateD1 = this.errorRateD1;
        cloneObject.errorRateD5 = this.errorRateD5;
        cloneObject.errorRateD20 = this.errorRateD20;
        cloneObject.errorRateD40 = this.errorRateD40;

        cloneObject.countD1 = this.countD1;
        cloneObject.countD5 = this.countD5;
        cloneObject.countD20 = this.countD20;
        cloneObject.countD40 = this.countD40;

        cloneObject.perfList = new ArrayList();
        for (MLPerformances perfs : perfList) {
            cloneObject.perfList.add(perfs.clone());
        }

        return cloneObject;
    }

    public int getCount(PredictionPeriodicity period) {
        if (period == PredictionPeriodicity.D1) return countD1;
        if (period == PredictionPeriodicity.D5) return countD5;
        if (period == PredictionPeriodicity.D20) return countD20;
        if (period == PredictionPeriodicity.D40) return countD40;

        return 0;
    }




}
