package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 23/11/2015.
 */
public interface HistoryParser {

    static String dbName = "history";

    void fetch();

    public void fetchSpecific(StockGeneral g);

    public void fetchCurrent( int period);


    public static void saveHistory(BatchPoints bp, StockHistory hist) {
        Point pt = Point.measurement(hist.getCodif()).time(hist.getTimeInsert().getMillis()+36000000, TimeUnit.MILLISECONDS)
            .field("open", hist.getOpening())
            .field("high", hist.getHighest())
            .field("low",hist.getLowest())
            .field("volume", hist.getVolume())
            .field("value",hist.getValue())
            .field("consensus",hist.getConsensusNote())
            .build();
        bp.point(pt);
    }
}
