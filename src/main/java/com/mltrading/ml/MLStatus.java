package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 16/02/2016.
 */
public class MLStatus implements Serializable{

    private double avgD1 = 0;
    private double avgD5 = 0;
    private double avgD20 = 0;
    private double avgD40 = 0;

    private int errorRateD1 = 0;
    private int errorRateD5 = 0;
    private int errorRateD20 = 0;
    private int errorRateD40 = 0;

    private List<MLPerformances> perfList;

    public void savePerf(String code) {
        for (MLPerformances perfs : perfList) {
            perfs.save(code);
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
    public void mergeList(List<MLPerformances> list) {
        if (perfList == null) perfList = list;
        else {
            for (int i = 0; i< list.size(); i++) {
                if (list.get(i).getMlD5() != null) perfList.get(i).setMlD5(list.get(i).getMlD5());
                if (list.get(i).getMlD20() != null) perfList.get(i).setMlD20(list.get(i).getMlD20());
                if (list.get(i).getMlD40() != null) perfList.get(i).setMlD40(list.get(i).getMlD40());
            }
        }
    }


    public void calculeAvgPrd() {
        double avgD1 = 0, avgD5 =0, avgD20 =0, avgD40 =0;
        int errD1 = 0, errD5 =0, errD20 =0, errD40 =0;
        for (MLPerformances p : perfList) {
            if (p.getMlD1() != null && p.getMlD1().getValue()!=0) {
                avgD1 += p.getMlD1().getRealyield() - p.getMlD1().getYield();
                errD1 += p.getMlD1().isSign() == false ? 1 : 0;
            }
            if (p.getMlD5() != null && p.getMlD5().getValue()!=0) {
                avgD5 += p.getMlD5().getRealyield() - p.getMlD5().getYield();
                errD5 += p.getMlD5().isSign() == false ? 1 : 0;
            }
            if (p.getMlD20() != null && p.getMlD20().getValue()!=0) {
                avgD20 += p.getMlD20().getRealyield() - p.getMlD20().getYield();
                errD20 += p.getMlD20().isSign() == false ? 1 : 0;
            }
            if (p.getMlD40() != null && p.getMlD40().getValue()!=0) {
                avgD40 += p.getMlD40().getRealyield() - p.getMlD40().getYield();
                errD40 += p.getMlD40().isSign() == false ? 1 : 0;
            }
        }

        this.setAvgD1(avgD1 / 90 * 100);
        this.setAvgD5(avgD5 / 85 * 100);
        this.setAvgD20(avgD20 / 70 * 100);
        this.setAvgD40(avgD40 / 50 * 100);

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

    public List<MLPerformances> getPerfList() {
        return perfList;
    }

    public void replaceElementList(List<MLPerformances> rep, PredictionPeriodicity period) throws Exception {
        int col = PredictionPeriodicity.convert(period);
        for (int i = 0; i< this.getPerfList().size(); i++) {
            switch (col) {
                case 1: this.getPerfList().get(i).setMlD1(rep.get(i).getMlD1());
                        break;
                case 5 : this.getPerfList().get(i).setMlD5(rep.get(i).getMlD5());
                    break;
                case 20 : this.getPerfList().get(i).setMlD20(rep.get(i).getMlD20());
                    break;
                case 40 : this.getPerfList().get(i).setMlD40(rep.get(i).getMlD40());
                    break;
                default: throw new Exception("replacement impossible, period unknown");
            }
        }

    }

    /**
     * return last max StockHistory
     * @param code
     * @return O or max last StockHistory
     */
    public void loadPerf(final String code) {
        final int max = 90;
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

        int sizeP1 = listP1.getResults().get(0).getSeries().get(0).getValues().size();
        int sizeP5 = listP5.getResults().get(0).getSeries().get(0).getValues().size();
        int sizeP20 = listP20.getResults().get(0).getSeries().get(0).getValues().size();
        int sizeP40 = listP40.getResults().get(0).getSeries().get(0).getValues().size();

        /*if (sizeP1 < max)
            return ;*/

        for (int i = sizeP1-max; i < sizeP1; i++) {
            MLPerformances mlps = new MLPerformances();

            populate(mlps.getMlD1(), listP1, i);
            if (i < sizeP5 ) populate(mlps.getMlD5(), listP5, i); else mlps.setMlD5(null);
            if (i < sizeP20 ) populate(mlps.getMlD20(), listP20, i);  else mlps.setMlD20(null);
            if (i < sizeP40 ) populate(mlps.getMlD40(), listP40, i);  else mlps.setMlD40(null);
            mlps.setDate(mlps.getMlD1().getDate());

            perfList.add(mlps);

        }

        calculeAvgPrd();

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

        cloneObject.perfList = new ArrayList();
        for (MLPerformances perfs : perfList) {
            cloneObject.perfList.add(perfs.clone());
        }

        return cloneObject;
    }
}
