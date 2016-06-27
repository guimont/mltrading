package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.HistogramDocument;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockIndice;
import com.mltrading.repository.ArticleRepository;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 09/03/2016.
 */
public interface ArticleParser {

    void fetch(ArticleRepository repository);

    public void fetchCurrent(ArticleRepository repository);

    public void fetchSpecific(ArticleRepository repository, StockGeneral g);




}
