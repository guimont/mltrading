package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 23/11/2015.
 */

@Singleton
public class HistoryParserYahoo implements HistoryParser {
    @Override
    public void fetch() {
       loader();
    }

    static String startUrl="https://fr.finance.yahoo.com/q/hp?s=";
    static String endUrl ="&a=00&b=3&c=2010&d=10&e=23&f=2015&g=d&z=66&y=";
    static int PAGINATION = 66;
    static String refCode = "tbody";
    static int MAXPAGE = 1518;

    public  void loader() {

        int numPage = 0;
        boolean retry = false;
        for (StockGeneral g: CacheStockGeneral.getCache().values()) {
            for(numPage =0; numPage <= 150 ; numPage += PAGINATION) {
                String url = startUrl + g.getCodif() +"." + g.getPlaceCodif() + endUrl+ numPage;
                try {
                    String text = null;

                    //inifinite loop
                    do {
                        text = ParserCommon.loadUrl(new URL(url));
                        if (text == null) retry = true;
                        else retry = false;
                    } while (retry);


                    Document doc = Jsoup.parse(text);
                    BatchPoints bp = InfluxDaoConnector.getBatchPoints();

                    Elements links = doc.select(refCode);
                    for (Element link : links) {

                        if (link.children().size() > 40) {
                            Elements sublinks = link.children().select("tr");
                            for (Element elt : sublinks) {
                                Elements t = elt.select("td");
                                if (t.size() > 3) {
                                    StockHistory hist = new StockHistory(g);
                                    hist.setDayYahoo(t.get(0).text());
                                    hist.setOpening(new Double(t.get(1).text().replace(",", ".")));
                                    hist.setHighest(new Double(t.get(2).text().replace(",", ".")));
                                    hist.setLowest(new Double(t.get(3).text().replace(",", ".")));
                                    hist.setValue(new Double(t.get(4).text().replace(",", ".")));
                                    hist.setVolume(new Double(t.get(5).text().replaceAll(" ", "")));
                                    HistoryParser.saveHistory(bp, hist);
                                    System.out.println(hist.toString());
                                }
                            }
                        }
                    }
                    InfluxDaoConnector.writePoints(bp);


                    /*for (Element link : links) {
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
                                    hist.setDay(e.text());
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("valorisation") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setValue(Float.parseFloat(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("high") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setHighest(Float.parseFloat(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("low") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setLowest(Float.parseFloat(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("open") == 0) {
                                    String t = e.text().replace(',', '.');
                                    hist.setOpening(Float.parseFloat(t));
                                }
                                if (!linkField.isEmpty() && linkField.compareToIgnoreCase("volume") == 0) {
                                    String t = e.text().replace('�', ' ').replaceAll(" ", "");
                                    hist.setVolume(Integer.parseInt(t));
                                }
                            }
                            HistoryParser.saveHistory(bp, hist);
                            System.out.println(hist.toString());

                        }

                        //InfluxDaoConnector.writePoints(bp);

                    }*/

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getName());
                }
            }
        }
    }




}
