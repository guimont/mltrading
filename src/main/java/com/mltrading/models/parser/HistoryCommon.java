package com.mltrading.models.parser;

import com.mltrading.models.stock.StockHistory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

public interface HistoryCommon {

    static final int NO_RANGE = 0xFFFF;

    default void saveHistory(BatchPoints bp, StockHistory hist) {
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
