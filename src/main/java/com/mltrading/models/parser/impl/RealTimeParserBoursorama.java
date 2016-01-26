package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.RealTimeParser;
import com.mltrading.models.stock.CacheRawMaterial;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 20/11/2015.
 */

@Singleton
public class RealTimeParserBoursorama implements RealTimeParser {

    static String cac40 = "http://www.boursorama.com/bourse/actions/cours_az.phtml?MARCHE=1rPCAC&validate=";

    static String refCode = "tbody";


    public static int loaderCache() {
        return loaderStock(cac40);

    }

    //TODO
    private static int loaderStock(String url) {

        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            Elements sublinks = links.select("tr");

            for (Element link : sublinks) {

                StockGeneral g = new StockGeneral();
                String name = link.child(1).text();
                String value = link.child(3).text();
                String variation = link.child(4).text();
                String open = link.child(5).text();
                String volume = link.child(9).text();


                CacheStockGeneral.getCache().put(g.getCode(),g);
            }


            System.out.println(CacheStockGeneral.getCache().size());


            for (StockGeneral g: CacheStockGeneral.getCache().values()) {
                System.out.println(g.getCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
