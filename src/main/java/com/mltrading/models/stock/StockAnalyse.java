package com.mltrading.models.stock;

import com.mltrading.dao.Requester;
import com.mltrading.influxdb.dto.QueryRequest;
import com.mltrading.influxdb.dto.QueryResult;

/**
 * Created by gmo on 16/11/2015.
 */
public class StockAnalyse extends Object{


    public static int COL_MMA20_POS  = 0;
    public static int COL_MMA50_POS  = 1;
    public static int COL_MME12_POS  = 2;
    public static int COL_MME26_POS  = 3;
    public static int COL_MACD_POS   = 4;
    public static int COL_MOMENTUM_POS  = 5;
    public static int COL_STDDEV_POS  = 6;


    public static int N_AT = 7;


    private Double mma20;

    private Double mma50;

    private Double mme12;

    private Double mme26;

    private Double momentum;

    private Double stdDev;

    private Double macd;

    public Double getMacd() {
        return macd;
    }

    public void setMacd(Double macd) {
        this.macd = macd;
    }

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
            ", macd=" + macd +
            '}';
    }

    static private int DATE_COLUMN = 0;
    static private int MMA20_COLUMN = 1;
    static private int MMA50_COLUMN = 2;
    static private int MME12_COLUMN = 3;
    static private int MME26_COLUMN = 4;
    static private int MOMENTUM_COLUMN = 5;
    static private int STDDEV_COLUMN = 6;

    public static StockAnalyse getAnalyse(String code, String date) {
        StockAnalyse a = new StockAnalyse();

        String query = "SELECT * FROM " + code + "T where time = '" + date + "'";
        QueryResult list = Requester.sendRequest(new QueryRequest(query, StockHistory.dbName));

        a.setMma20(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MMA20_COLUMN).toString()));
        a.setMma50(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MMA50_COLUMN).toString()));
        a.setMme12(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MME12_COLUMN).toString()));
        a.setMme26(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MME26_COLUMN).toString()));
        a.setMomentum(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MOMENTUM_COLUMN).toString()));
        a.setStdDev(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(STDDEV_COLUMN).toString()));
        a.setMacd(a.getMme26()-a.getMme12());

        return a;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        StockAnalyse copy = new StockAnalyse();
        copy.setMma20(this.mma20);
        copy.setMma50(this.mma50);
        copy.setMme12(this.mme12);
        copy.setMme26(this.mme26);
        copy.setMomentum(this.momentum);
        copy.setStdDev(this.stdDev);
        copy.setMacd(this.macd);
        return copy;
    }
}
