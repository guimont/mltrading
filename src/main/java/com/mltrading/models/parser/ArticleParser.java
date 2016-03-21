package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.HistogramDocument;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockIndice;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 09/03/2016.
 */
public interface ArticleParser {

    void fetch();

    public void fetchCurrent();

    public static void saveNotation(BatchPoints bp, HistogramDocument note) {
        Point pt = Point.measurement(note.getCode()).time(note.getTimeInsert().getMillis() + 3600000, TimeUnit.MILLISECONDS)
            .field("L1", note.getLvl_L1())
            .field("L2", note.getLvl_L2())
            .field("L3", note.getLvl_L3())
            .field("L4", note.getLvl_L4())
            .field("P1", note.getLvl_P1())
            .field("P2", note.getLvl_P2())
            .field("P3", note.getLvl_P3())
            .field("P4", note.getLvl_P4())
            .field("sum", note.sum())
            .build();
        bp.point(pt);
    }


}
