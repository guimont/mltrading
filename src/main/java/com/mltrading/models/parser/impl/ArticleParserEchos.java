package com.mltrading.models.parser.impl;

import com.mltrading.models.parser.ArticleParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.repository.StockRepository;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 09/03/2016.
 */
public class ArticleParserEchos implements ArticleParser {
    //http://investir.lesechos.fr/actions/actualites/action-accor,xpar,ac,fr0000120404,isin.html?page=5
    static String base = "http://investir.lesechos.fr/actions/actualites/action-";
    static String sep = ",";
    static String end = ",isin.html?page";

    static String refCode = "tbody";


    private void loader(StockRepository repository) {
        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {
            int page = 0;

            String url = base + CacheStockGeneral.getIsinCache().get(g.getCode()).getName().toLowerCase().replaceAll(" ", "-") + sep + g.getPlace().toLowerCase() + sep + g.getCodif().toLowerCase() + sep + g.getCode().toLowerCase() + end + page;


            try {
                String text;

                System.out.println(url);
                text = ParserCommon.loadUrl(new URL(url));



            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }
        }
    }


}
