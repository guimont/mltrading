package com.mltrading.models.parser.impl.google;

import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.RealTimeParser;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.stock.StockSector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 16/01/2017.
 */
public class RealTimeSectorGoogle extends ParserCommon implements RealTimeParser {

    //*doc.select("#finance_onebox").get(0).child(2).text()
    static String startUrl="https://finance.google.com/finance?q=INDEXEURO%3A";
    static String refCode = "#finance_onebox";

    public void refreshCache() {
        CacheStockSector.getSectorCache().values().forEach(s -> refreshCache(s));
        //CacheStockSector.getSectorCache().values().forEach(com.mltrading.models.parser.impl.google.RealTimeSectorGoogle::print);
    }


    private static void print(StockSector ss) {
        System.out.println("code: " + ss.getCode() +" value: " + ss.getValue());
    }

    private int refreshCache(StockSector ss) {

        try {
            String text = loadUrl(new URL(startUrl+ss.getCode()));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);


            ss.setValue(new Double( links.get(0).child(2).child(0).child(0).text().replaceAll(String.valueOf((char) 160), "").replaceAll(",", ".")));
            ss.setVariation(new Float( links.get(0).child(2).child(0).child(1).text().split(" ")[1]
                .replaceAll(",", ".").replaceAll("\\(", "").replaceAll("%\\)", "").replaceAll(String.valueOf((char) 160), "")));



        } catch (Exception e) {
            System.out.println("code: " + ss.getCode());
            e.printStackTrace();
        }

        return 0;
    }


}
