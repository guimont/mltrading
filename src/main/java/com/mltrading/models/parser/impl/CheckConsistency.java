package com.mltrading.models.parser.impl;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;
import com.mltrading.models.stock.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

    static int OFFSET_BASE = 50;
    static int RANGE_MAX = 300;
    static int XT_PERIOD = 20;

    public static void consistency(Stock stock) {
        log.info("create FeaturesStock for: " + stock.getCodeif());


        List<String> rangeDate = null;
        try {
            rangeDate = StockHistory.getDateHistoryListOffsetLimit(stock.getCode(), OFFSET_BASE, RANGE_MAX);
            if (rangeDate.size() < 200) {
                log.error("Cannot get date list for: " + stock.getCode() + " not enough element");
                return;
            }
        } catch (Exception e) {
            log.error("Cannot get date list for: " + stock.getCode() + "  //exception:" + e);
            return;
        }

        for (String date: rangeDate) {

            try {
                StockHistory  res = StockHistory.getStockHistoryDayAfter(stock.getCode(), date);
            } catch (Exception e) {
                log.error("Cannot get date for: " + stock.getCode() + " and date: " + date + " //exception:" + e);
            }

            /**
             * stock
             */
            try {
                List<StockHistory> sh = StockHistory.getStockHistoryDateInvert(stock.getCode(), date, XT_PERIOD);
                StockHistory current = StockHistory.getStockHistory(stock.getCode(), date);
            } catch (Exception e) {
                log.error("Cannot get stock history for: " + stock.getCode() + " and date: " + date +  " //exception:" + e);
            }


            try {
                StockAnalyse ash = StockAnalyse.getAnalyse(stock.getCode(), date);
            } catch (Exception e) {
                log.error("Cannot get analyse stock for: " + stock.getCode() + " and date: " + date +  " //exception:" + e);
                continue;            }

            /**
             * sector
             */
            try {
                List<StockSector> ss = StockSector.getStockSectorDateInvert(stock.getSector(), date, XT_PERIOD);
                //Check date equals
                if (!ss.get(0).getDay().equalsIgnoreCase(date))
                    log.error("Cannot get sector correct stock for: " + stock.getSector() + " and date: " + date);
                StockAnalyse ass = StockAnalyse.getAnalyse(stock.getSector(), date);
            } catch (Exception e) {
                log.error("Cannot get sector/analyse stock for: " + stock.getSector() + " and date: " + date + " //exception:" + e);
            }


            /**
             * indice
             */
            try {
                String codeIndice = StockIndice.translate(stock.getIndice());
                List<StockIndice> si = StockIndice.getStockIndiceDateInvert(codeIndice, date, XT_PERIOD);
                if (!si.get(0).getDay().equalsIgnoreCase(date))
                    log.error("Cannot get indice correct stock for: " + stock.getSector() + " and date: " + date);
                StockAnalyse asi = StockAnalyse.getAnalyse(codeIndice, date);
            } catch (Exception e) {
                log.error("Cannot get indice/analyse stock for: " + stock.getIndice() + " and date: " + date +  " //exception:" + e);
            }


            /**
             * volatility cac
             */
            try {
                List<StockIndice> sVCac = StockIndice.getStockIndiceDateInvert("VCAC", date, XT_PERIOD);
            } catch (Exception e) {
                log.error("Cannot get vcac stock for: " + stock.getCodeif() + " and date: " + date + " //exception:" + e);
                continue;
            }

        }

        return ;
    }


}
