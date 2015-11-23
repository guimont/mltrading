package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.RealTimeParser;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 21/11/2015.
 */

@Singleton
public class RealTimeParserYahoo implements RealTimeParser {

    static String cac40 = "https://fr.finance.yahoo.com/q/cp?s=%5EFCHI";
    static String refCode = "tbody";

    @Override
    public int refreshCache() {
        return loaderStock(cac40);

    }

    public int loaderStock(String url) {

        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            Elements sublinks = links.select("tr");

            for (Element link : sublinks) {


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
