package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;
import com.mltrading.models.util.CsvFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 27/01/2016.
 */
public class CheckConsistency {
    private static final Logger log = LoggerFactory.getLogger(CheckConsistency.class);


    private static int OFFSET_BASE = 50;
    private static int RANGE_MAX = 300;
    private static int XT_PERIOD = 20;
    private static int XT_DAY = 1;
    private static int RANGE = 200;

    private static String STOCK = "STK";
    private static String STOCK_AT = "STKAT";
    private static final String SECTOR = "SEC";
    private static final String SECTOR_AT = "SECAT";

    public static void consistency() {

        List<StockGeneral> sg = new ArrayList(CacheStockGeneral.getIsinCache().values());
        List<String> rangeDate = null;
        CsvFileWriter errorConsistency = new CsvFileWriter();

        for (StockGeneral stock:sg) {
            log.info("create FeaturesStock for: " + stock.getCodif());

            try {
                List<String> rangeDateStock = StockHistory.getDateHistoryListOffsetLimit(stock.getCodif(), OFFSET_BASE, RANGE_MAX);
                if (rangeDate == null || rangeDateStock.size() > rangeDate.size())
                    rangeDate = rangeDateStock;

                if (rangeDate.size() < RANGE) {
                    log.error("Cannot get date list for: " + stock.getCodif() + " not enough element");
                    errorConsistency.writeData(STOCK, stock.getCode(), stock.getCodif(), "no date", RANGE);
                    continue;
                }
            } catch (Exception e) {
                log.error("Cannot get date list for: " + stock.getCodif() + "  //exception:" + e);
                continue;
            }

            for (String date : rangeDate) {

                try {
                    StockHistory res = StockHistory.getStockHistoryDayAfter(stock.getCodif(), date);
                } catch (Exception e) {
                    log.error("Cannot get date for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                }

                /**
                 * stock
                 */
                try {
                    StockHistory current = StockHistory.getStockHistory(stock.getCodif(), date);
                } catch (Exception e) {
                    log.error("Cannot get stock history for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(STOCK, stock.getCode(), stock.getCodif(), date, XT_DAY);
                }

                /**
                 * stock
                 */
                try {
                    List<StockHistory> sh = StockHistory.getStockHistoryDateInvert(stock.getCodif(), date, XT_PERIOD);
                } catch (Exception e) {
                    log.error("Cannot get stock history for: " + stock.getCodif() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(STOCK, stock.getCode(), stock.getCodif(), date, XT_PERIOD);
                }


                /*try {
                    StockAnalyse ash = StockAnalyse.getAnalyse(stock.getCode(), date);
                } catch (Exception e) {
                    log.error("Cannot get analyse stock for: " + stock.getCode() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(STOCK_AT, stock.getCode(), stock.getCodif(), date, XT_DAY);
                }*/
            }
        }


        List<StockSector> ssL = new ArrayList(CacheStockSector.getSectorCache().values());

        for (StockSector sector:ssL) {
            /**
             * sector
             */
            for (String date : rangeDate) {
                try {
                    List<StockHistory> ss = StockHistory.getStockHistoryDateInvert(sector.getCode(), date, XT_PERIOD);
                    //Check date equals

                    if (!ss.get(0).getDay().equalsIgnoreCase(date)) {
                        log.error("Cannot get sector correct stock for: " + sector.getCode() + " and date: " + date);
                        errorConsistency.writeData(SECTOR, sector.getCode(), sector.getCodif(), date, XT_DAY);
                    }

                } catch (Exception e) {
                    log.error("Cannot get sector/analyse stock for: " + sector.getCode() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(SECTOR, sector.getCode(), sector.getCodif(), date, XT_PERIOD);
                }


                /*try {
                    StockAnalyse ass = StockAnalyse.getAnalyse(sector.getCode(), date);
                } catch (Exception e) {
                    log.error("Cannot get sector/analyse stock for: " + sector.getCode() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(SECTOR_AT, sector.getCode(), sector.getCodif(), date, XT_DAY);
                }*/
            }
        }


        List<StockRawMat> ssR = new ArrayList(CacheRawMaterial.getCache().values());

        for (StockRawMat sector:ssR) {
            /**
             * sector
             */
            for (String date : rangeDate) {
                try {
                    List<StockHistory> ss = StockHistory.getStockHistoryDateInvert(sector.getCode(), date, XT_PERIOD);
                    //Check date equals

                    if (!ss.get(0).getDay().equalsIgnoreCase(date)) {
                        log.error("Cannot get sector correct stock for: " + sector.getCode() + " and date: " + date);
                        errorConsistency.writeData(SECTOR, sector.getCode(), sector.getCodif(), date, XT_DAY);
                    }

                } catch (Exception e) {
                    log.error("Cannot get sector/analyse stock for: " + sector.getCode() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(SECTOR, sector.getCode(), sector.getCodif(), date, XT_PERIOD);
                }


                /*try {
                    StockAnalyse ass = StockAnalyse.getAnalyse(sector.getCode(), date);
                } catch (Exception e) {
                    log.error("Cannot get sector/analyse stock for: " + sector.getCode() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(SECTOR_AT, sector.getCode(), sector.getCodif(), date, XT_DAY);
                }*/
            }
        }


        List<StockIndice> ssI = new ArrayList(CacheStockIndice.getIndiceCache().values());

        for (StockIndice sector:ssI) {
            /**
             * sector
             */
            for (String date : rangeDate) {
                try {
                    List<StockHistory> ss = StockHistory.getStockHistoryDateInvert(sector.getCode(), date, XT_PERIOD);
                    //Check date equals

                    if (!ss.get(0).getDay().equalsIgnoreCase(date)) {
                        log.error("Cannot get sector correct stock for: " + sector.getCode() + " and date: " + date);
                        errorConsistency.writeData(SECTOR, sector.getCode(), sector.getCodif(), date, XT_DAY);
                    }

                } catch (Exception e) {
                    log.error("Cannot get sector/analyse stock for: " + sector.getCode() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(SECTOR, sector.getCode(), sector.getCodif(), date, XT_PERIOD);
                }


                /*try {
                    StockAnalyse ass = StockAnalyse.getAnalyse(sector.getCode(), date);
                } catch (Exception e) {
                    log.error("Cannot get sector/analyse stock for: " + sector.getCode() + " and date: " + date + " //exception:" + e);
                    errorConsistency.writeData(SECTOR_AT, sector.getCode(), sector.getCodif(), date, XT_DAY);
                }*/
            }
        }

        errorConsistency.close();




            /**
             * indice
             *
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
             *
            try {
                List<StockIndice> sVCac = StockIndice.getStockIndiceDateInvert("VCAC", date, XT_PERIOD);
            } catch (Exception e) {
                log.error("Cannot get vcac stock for: " + stock.getCodeif() + " and date: " + date + " //exception:" + e);
                continue;
            }

        }*/

        return ;
    }


}
