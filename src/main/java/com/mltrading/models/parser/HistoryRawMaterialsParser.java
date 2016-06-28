package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockRawMat;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 23/11/2015.
 */
public interface HistoryRawMaterialsParser {

    void fetch(String host);

    public void fetchCurrent(int period);

    public static void saveHistory(BatchPoints bp, StockRawMat raw) {
        Point pt = Point.measurement(raw.getCode()).time(raw.getTimeInsert().getMillis() + 36000000, TimeUnit.MILLISECONDS)
            .field("open", raw.getOpening())
            .field("high", raw.getHighest())
            .field("low",raw.getLowest())
            .field("value", raw.getValue())
            .build();
        bp.point(pt);
    }
}
