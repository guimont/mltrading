package com.mltrading.models.parser.impl.boursorama;

import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.RealTimeParser;
import com.mltrading.models.stock.StockSector;
import com.mltrading.models.stock.cache.CacheStockSector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.net.URL;

public class RealTimeSectorBoursorama extends ParserCommon implements RealTimeParser {

    static String startUrl="https://www.boursorama.com/bourse/indices/cours/1rP";


    public void refreshCache() {
        CacheStockSector.getSectorCache().values().forEach(s -> refreshCache(s));
    }

    private int refreshCache(StockSector ss) {

        try {
            String text = loadUrl(new URL(startUrl+ss.getCode()));

            if (text != null) {
                Document doc = Jsoup.parse(text);
                ss.setValue(new Double(doc.select(".c-faceplate__price").select(".c-instrument--last").text()
                    .replaceAll(" ", "").replaceAll(",", ".")));
                ss.setVariation(new Float(doc.select(".c-faceplate__fluctuation").select(".c-instrument--variation").text()
                    .replaceAll(",", ".").replaceAll(" ", "").replaceAll("%", "")));

            }
            else {
                System.out.println("Cannot refresh cache for code: " + ss.getCode());
            }

        } catch (Exception e) {
            System.out.println("code: " + ss.getCode());
            e.printStackTrace();
        }

        return 0;
    }




}
