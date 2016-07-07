package com.mltrading.models.parser;

import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockSector;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 23/11/2015.
 */
public interface HistorySectorParser {

    void fetch();

    public void fetchCurrent(int period);


}
