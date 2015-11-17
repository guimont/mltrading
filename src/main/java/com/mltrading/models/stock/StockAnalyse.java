package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;

/**
 * Created by gmo on 16/11/2015.
 */
public class StockAnalyse {
    private Double mma20;

    private Double mma50;

    private Double mme12;

    private Double mme26;

    private Double momentum;

    private Double stdDev;


    public Double getMma20() {
        return mma20;
    }

    public void setMma20(Double mma20) {
        this.mma20 = mma20;
    }

    public Double getMma50() {
        return mma50;
    }

    public void setMma50(Double mma50) {
        this.mma50 = mma50;
    }

    public Double getMme12() {
        return mme12;
    }

    public void setMme12(Double mme12) {
        this.mme12 = mme12;
    }

    public Double getMme26() {
        return mme26;
    }

    public void setMme26(Double mme26) {
        this.mme26 = mme26;
    }

    public Double getMomentum() {
        return momentum;
    }

    public void setMomentum(Double momentum) {
        this.momentum = momentum;
    }

    public Double getStdDev() {
        return stdDev;
    }

    public void setStdDev(Double stdDev) {
        this.stdDev = stdDev;
    }


    @Override
    public String toString() {
        return "StockAnalyse{" +
            "mma20=" + mma20 +
            ", mma50=" + mma50 +
            ", mme12=" + mme12 +
            ", mme26=" + mme26 +
            ", momentum=" + momentum +
            ", stdDev=" + stdDev +
            '}';
    }


    public static StockAnalyse getAnalyse(String code, String date) {
        StockAnalyse a = new StockAnalyse();

        String query = "SELECT * FROM "+code+"T where time = '" + date + "'";
        QueryResult meanQ = InfluxDaoConnector.getPoints(query);

        a.setMma20((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1));
        a.setMma50((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(2));
        a.setMme12((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(3));
        a.setMme26((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(4));
        a.setMomentum((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(5));
        a.setStdDev((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(6));

        return a;
    }
}
