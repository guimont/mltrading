package com.mltrading.models.parser.impl;

import com.google.inject.Inject;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.StockParser;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.Stock;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.service.StockService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 05/01/2016.
 */
public class StockParserInvestir  implements StockParser{

    static String base = "http://investir.lesechos.fr/cours/profil-societe-action-";
    static String sep = ",";
    static String end = ",isin.html";

    static String refCode = "tbody";

    private StockService stockService = new StockService();

    //http://investir.lesechos.fr/cours/profil-societe-action-accor,xpar,ac,fr0000120404,isin.html

    @Override
    public void fetch() {
        loader();
    }


    private void loader() {
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {

            String url = base + CacheStockGeneral.getIsinCache().get(g.getCode()).getName().toLowerCase().replaceAll(" ","-") + sep + g.getPlace().toLowerCase() + sep  + g.getCodif().toLowerCase() + sep + g.getCode().toLowerCase() +end;
            Stock stock = new Stock();

            try {
                String text;

                System.out.println(url);
                text = ParserCommon.loadUrl(new URL(url));

                if (text != null) {
                    Document doc = Jsoup.parse(text);
                    Elements links = doc.select(refCode);
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
