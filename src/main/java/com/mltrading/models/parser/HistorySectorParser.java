package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.StockHistory;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 23/11/2015.
 */
public interface HistorySectorParser {

    void fetch();

    public static void saveHistory(BatchPoints bp, StockHistory hist) {
        Point pt = Point.measurement(hist.getCode()).time(hist.getTimeInsert().getMillis(), TimeUnit.MILLISECONDS)
            .field("open", hist.getOpening())
            .field("value",hist.getValue())
            .build();
        bp.point(pt);
    }
}
