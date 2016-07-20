package com.mltrading.models.parser;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 24/06/2015.
 */
@Deprecated
public class ParserHistory {

    static String startUrl="http://bourse.lesechos.fr/bourse/details/donnees_histo.jsp?fw3_component=dataList&fw3_autonomous=/bourse/details/donnees_histo-view.jsp&dataList_pageNum=";
    static String placeUrl ="&&place=";
    static String codeUrl ="&code=";
    static String endUrl ="&OFFSET_START_RANGE=-36&CODE_RESOLUTION=DAY&codif=";
    static String refCode = "tr";

    public static void loader() {
        throw new NotImplementedException();

        /*int numPage;
        boolean retry = false;
        for (StockGeneral g: CacheStockGeneral.getCache().values()) {
            for(numPage = 20; numPage>0; numPage--) {
                String url = startUrl + numPage + placeUrl + g.getPlace() + codeUrl + g.getCode() + endUrl+ g.getCodif();
                try {
                    String text = null;

                    //inifinite loop
                    do {
                         text = ParserCommon.loadUrl(new URL(url));
                        if (text == null) retry = true;
                        else retry = false;
                    } while (retry);


                    Document doc = Jsoup.parse(text);

                    Elements links = doc.select(refCode);
                    for (Element link : links) {
                        //data-item="donneesHistoTr"
                        BatchPoints bp = InfluxDaoConnector.getBatchPoints();
                        String linkItem = link.attr("data-item");
                        if ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("donneesHistoTr") == 0) {
                            StockHistory hist = new StockHistory();
                            hist.setCode(g.getCode());
                            hist.setName(g.getName());
                            hist.setPlace(g.getPlace());

                            Elements et = link.select("td");
                            for (Element e : et) {
                                String linkField = e.attr("data-field");

                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("jour") == 0) {
                                    hist.setDayInvestir(e.text());
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("valorisation") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setValue(Double.parseDouble(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("high") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setHighest(Double.parseDouble(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("low") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setLowest(Double.parseDouble(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("open") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setOpening(Double.parseDouble(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("volume") == 0) {
                                    String t = e.text().replace('�', ' ').replaceAll(" ", "");
                                    hist.setVolume(Double.parseDouble(t));
                                }
                            }
                            //saveHistory(bp, hist);
                            System.out.println(hist.toString());

                        }

                        //InfluxDaoConnector.writePoints(bp);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getName());
                }
            }
        }*/
    }

    /*
    public static void saveHistory(BatchPoints bp, StockHistory hist) {


        Point pt = Point.measurement(hist.getCode()).time(hist.getTimeInsert().getMillis(), TimeUnit.MILLISECONDS)
                .field("open", hist.getOpening())
                .field("high", hist.getHighest())
                .field("low",hist.getLowest())
                .field("volume", hist.getVolume())
                .field("value",hist.getValue())
                .build();
        bp.point(pt);

    }*/


}
