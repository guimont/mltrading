package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockIndice;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 23/11/2015.
 */
public interface HistoryIndiceParser {

    void fetch();

    public void fetchCurrent(int period);


    public static void saveHistory(BatchPoints bp, StockIndice hist) {
        Point pt = Point.measurement(hist.getCode()).time(hist.getTimeInsert().getMillis()+3600000, TimeUnit.MILLISECONDS)
            .field("open", hist.getOpening())
            .field("high", hist.getHighest())
            .field("low", hist.getLowest())
            .field("volume", hist.getVolume())
            .field("value",hist.getValue())
            .build();
        bp.point(pt);
    }
}
