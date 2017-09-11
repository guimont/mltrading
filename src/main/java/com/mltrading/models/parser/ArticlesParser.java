package com.mltrading.models.parser;


import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 09/03/2016.
 */
public interface ArticlesParser {

    void fetch();

    void fetchCurrent();


    default void saveDocument(BatchPoints bp, StockDocument doc) {
        Point pt = Point.measurement(doc.getCode()).time(doc.getTimeInsert().getMillis() + 3600000, TimeUnit.MILLISECONDS)
            .field("ref", doc.getRef())
            .field("source", doc.getSource())
            .build();
        bp.point(pt);
    }

}
