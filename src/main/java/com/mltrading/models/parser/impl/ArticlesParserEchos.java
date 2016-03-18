package com.mltrading.models.parser.impl;

import com.mltrading.dao.InfluxDaoConnectorDocument;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.ArticleParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 09/03/2016.
 */
public class ArticlesParserEchos implements ArticleParser {
    //http://investir.lesechos.fr/actions/actualites/action-accor,xpar,ac,fr0000120404,isin.html?page=5
    static String base = "http://investir.lesechos.fr/actions/actualites/action-";
    static String sep = ",";
    static String end = ",isin.html?page=";

    static String refCode = "div.contenu-bloc-infos";

    @Override
    public void fetch() {
        loader();
    }

    static int MAXPAGE = 40;

    private void loader() {
        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {

            for (int page = 1; page <= MAXPAGE; page += 1) {
                String url = base + CacheStockGeneral.getIsinCache().get(g.getCode()).getName().toLowerCase().replaceAll(" ", "-") + sep + g.getPlace().toLowerCase() + sep + g.getCodif().toLowerCase() + sep + g.getCode().toLowerCase() + end + page;

                try {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String text;

                    System.out.println(url);
                    text = ParserCommon.loadUrl(new URL(url));

                    Document doc = Jsoup.parse(text);
                    Elements links = doc.select(refCode);

                    Element list = links.get(0);

                    BatchPoints bp = InfluxDaoConnectorDocument.getBatchPoints();

                    for (Element e : list.children()) {
                        StockDocument document = new StockDocument();
                        document.setCode(g.getCodif() + "R");
                        String hour = e.getAllElements().get(1).getAllElements().get(1).childNodes().get(0).toString();
                        hour = hour.substring(2, hour.length()).replaceAll("h", ":");
                        String date = e.getAllElements().get(1).getAllElements().get(1).childNodes().get(2).toString();
                        document.setDayInvestir(date, hour);
                        String href = e.getAllElements().get(1).attributes().toString();
                        document.setRef(href);
                        ArticleParser.saveDocument(bp, document);
                    }

                    //InfluxDaoConnectorDocument.writePoints(bp);


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getName());
                }
            }
        }
    }



}
