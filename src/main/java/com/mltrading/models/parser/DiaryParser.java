package com.mltrading.models.parser;

import com.mltrading.models.stock.StockDocument;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

public interface DiaryParser {
    void fetch();

    void fetchCurrent(int period);

    default void saveDiary(BatchPoints bp, StockDocument doc) {
        Point pt = Point.measurement(doc.getCode()+StockDocument.TYPE_DIARY).time(doc.getTimeInsert().getMillis() + 3600000, TimeUnit.MILLISECONDS)
            .field("ref", doc.getRef())
            .addField("info",doc.getRef())
            .build();
        bp.point(pt);
    }

}
