package com.mltrading.models.parser.impl;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gmo on 27/01/2016.
 */
public class CheckConsistency {
    private static final Logger log = LoggerFactory.getLogger(CheckConsistency.class);

    public static void countfrom() {
        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {
            String query = "SELECT * FROM "+g.getCode() +" where time > '2013-06-01T00:00:00Z'";
            QueryResult list = InfluxDaoConnector.getPoints(query);
            try {
                int size = list.getResults().get(0).getSeries().get(0).getValues().size();
                log.info(g.getCodif() + " / " + size);
            } catch (Exception e) {
                log.info("cannot find for: " + g.getCodif());
            }


        }

    }


}
