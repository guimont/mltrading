package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistorySectorParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.CacheStockSector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 15/01/2016.
 */

@Singleton
@Deprecated
public class HistorySectorParserInvestir implements HistorySectorParser {

    public void fetch() {
       throw new NotImplementedException();
    }

    @Override
    public void fetchCurrent(int period) {
        throw new NotImplementedException();
        /*loaderFrom();*/
    }


    //http://investir.lesechos.fr/cours/historique-indice-cac-biens-de-consommation,xpar,frcg,qs0011017686,isin.html
    //http://investir.lesechos.fr/cours/historique-indice-cac-societes-financieres,xpar,frfin,qs0011017801,isin.html

    static String refCode = "tbody";


    /**
     * Read only current page from investir, cannot choice range
     *
    public void loaderFrom() {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            try {
                String text = ParserCommon.loadUrl(new URL(g.getUrlInvestir()));

                Document doc = Jsoup.parse(text);
                BatchPoints bp = InfluxDaoConnector.getBatchPoints();

                Elements links = doc.select(refCode);

                for (Element link : links) {

                    if (link.children().size() > 20) {
                        Elements sublinks = link.children().select("tr");
                        for (Element elt : sublinks) {
                            Elements t = elt.select("td");
                            if (t.size() > 3) {

                                StockSector sect = new StockSector(g.getCode(), g.getName(), g.getPlace());
                                sect.setDayInvestirExt(t.get(0).text());
                                sect.setValue(new Double(t.get(1).text().replaceAll("\\u00a0", "").replace(",", ".")));
                                sect.setHighest(new Double(t.get(3).text().replaceAll("\\u00a0", "").replace(",", ".")));
                                sect.setLowest(new Double(t.get(4).text().replaceAll("\\u00a0", "").replace(",", ".")));
                                sect.setOpening(new Double(t.get(5).text().replaceAll("\\u00a0", "").replace(",", ".")));
                                sect.setVolume(new Double(0));
                                HistorySectorParser.saveHistory(bp, sect);
                                System.out.println(sect.toString());
                            }
                        }
                    }
                }
                InfluxDaoConnector.writePoints(bp);



            }  catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }

        }
    }*/



}
