package com.mltrading.models.parser;

import com.mltrading.models.stock.HistogramDocument;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.repository.ArticleRepository;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 26/07/2017.
 */
public interface ArticleNotation {

    void fetch(ArticleRepository repository);
    void fetchCurrent(ArticleRepository repository);


    default void saveDocument(BatchPoints bp, HistogramDocument doc) {
        Point pt = Point.measurement(doc.getCode()).time(doc.getTimeInsert().getMillis() + 3600000, TimeUnit.MILLISECONDS)
            .field("source", doc.getSource())
            .field("note", doc.getNote())
            .field("pertinence", doc.getPertinence())
            .field("tf", doc.getTf())
            .field("tf_idf", doc.getTf_idf())
            .build();
        bp.point(pt);
    }

}
