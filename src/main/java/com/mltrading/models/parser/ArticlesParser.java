package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.HistogramDocument;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 09/03/2016.
 */
public interface ArticlesParser {

    void fetch();

    public void fetchCurrent();


    public static void saveDocument(BatchPoints bp, StockDocument doc) {
        Point pt = Point.measurement(doc.getCode()).time(doc.getTimeInsert().getMillis() + 3600000, TimeUnit.MILLISECONDS)
            .field("ref", doc.getRef())
            .build();
        bp.point(pt);
    }


}
