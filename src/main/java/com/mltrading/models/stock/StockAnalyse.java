package com.mltrading.models.stock;


import com.mltrading.models.stock.cache.CacheStockAnalyse;
import org.influxdb.dto.BatchPoints;

import java.util.List;

/**
 * Created by gmo on 16/11/2015.
 */
public class StockAnalyse extends StockBase{

    private static CacheStockAnalyse cache = CacheStockAnalyse.CacheStockAnalyseHolder();


    public static int COL_MMA20_POS  = 0;
    public static int COL_MMA50_POS  = 1;
    public static int COL_MME12_POS  = 2;
    public static int COL_MME26_POS  = 3;
    public static int COL_MACD_POS   = 4;
    public static int COL_MOMENTUM_POS  = 5;
    public static int COL_STDDEV_POS  = 6;
    public static int COL_GARCH20_POS  = 7;
    public static int COL_GARCHVOL20_POS  = 8;
    public static int COL_GARCH50_POS  = 9;
    public static int COL_GARCHVOL50_POS  = 10;
    public static int COL_GARCH100_POS  = 11;
    public static int COL_GARCHVOL100_POS  = 12;
    public static int COL_RESERVE  = 10;


    public static int N_AT = 13 + COL_RESERVE;


    private Double mma20;

    private Double mma50;

    private Double mme12;

    private Double mme26;

    private Double momentum;

    private Double stdDev;

    private Double macd;

    private Double garch20;

    private Double garch_vol_20;

    private Double garch50;

    private Double garch_vol_50;

    private Double garch100;

    private Double garch_vol_100;

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


    public Double getGarch50() {
        return garch50;
    }

    public void setGarch50(Double garch50) {
        this.garch50 = garch50;
    }

    public Double getGarch_vol_50() {
        return garch_vol_50;
    }

    public void setGarch_vol_50(Double garch_vol_50) {
        this.garch_vol_50 = garch_vol_50;
    }

    public Double getGarch100() {
        return garch100;
    }

    public void setGarch100(Double garch100) {
        this.garch100 = garch100;
    }

    public Double getGarch_vol_100() {
        return garch_vol_100;
    }

    public void setGarch_vol_100(Double garch_vol_100) {
        this.garch_vol_100 = garch_vol_100;
    }

    public Double getGarch20() {
        return garch20;
    }

    public void setGarch20(Double garch20) {
        this.garch20 = garch20;
    }

    public Double getGarch_vol_20() {
        return garch_vol_20;
    }

    public void setGarch_vol_20(Double garch_vol_20) {
        this.garch_vol_20 = garch_vol_20;
    }

    public static StockAnalyse getAnalyse(String code, String date) {
        return cache.getStockAnalyse(code, date);

        /*StockAnalyse a = new StockAnalyse();

        String query = "SELECT * FROM " + code + "T where time = '" + date + "'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, StockHistory.dbName));

        a.setMma20(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MMA20_COLUMN).toString()));
        a.setMma50(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MMA50_COLUMN).toString()));
        a.setMme12(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MME12_COLUMN).toString()));
        a.setMme26(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MME26_COLUMN).toString()));
        a.setMomentum(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(MOMENTUM_COLUMN).toString()));
        a.setStdDev(new Double(list.getResults().get(0).getSeries().get(0).getValues().get(0).get(STDDEV_COLUMN).toString()));
        a.setMacd(a.getMme26()-a.getMme12());

        return a;*/
    }

    public static List<StockAnalyse> getAllAnalyse(final String code) {
        return cache.getAllStockAnalyse(code);

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
        copy.setGarch20(this.garch20);
        copy.setGarch_vol_20(this.garch_vol_20);
        copy.setGarch50(this.garch50);
        copy.setGarch_vol_50(this.garch_vol_50);
        copy.setGarch100(this.garch100);
        copy.setGarch_vol_100(this.garch_vol_100);

        return copy;
    }


}
