package com.mltrading.models.parser.impl;

import com.mltrading.models.parser.ConsensusParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.Consensus;
import com.mltrading.models.stock.StockGeneral;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gmo on 03/12/2015.
 */
public class ConsensusParserInvestir implements ConsensusParser {

    static String base = "http://investir.lesechos.fr/cours/consensus-analystes-action-";
    static String sep = ",";
    static String end = ",isin.html";

    static String refCode = "tbody";

    //accor,xpar,ac,fr0000120404;
    @Override
    public void fetch() {
        loader();
    }


    public static Consensus fetchStock(String code) {
        StockGeneral g = CacheStockGeneral.getIsinCache().get(code);

        Consensus c = new Consensus();
           try {
               String url = base + CacheStockGeneral.getIsinCache().get(g.getCode()).getName().toLowerCase().replaceAll(" ","-") + sep + g.getPlace().toLowerCase() + sep  + g.getCodif().toLowerCase() + sep + g.getCode().toLowerCase() +end;

               String text;

                text = ParserCommon.loadUrl(new URL(url));

                if (text != null) {
                    Document doc = Jsoup.parse(text);
                    Elements links = doc.select(refCode);
                    Elements sn = links.get(0).children();
                    for (int i = 1; i < 7; i++) {
                        c.getNotation(i).setSell(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(0).text()));
                        c.getNotation(i).setRelieve(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(1).text()));
                        c.getNotation(i).setKeep(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(2).text()));
                        c.getNotation(i).setReinforce(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(3).text()));
                        c.getNotation(i).setBuy(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(4).text()));
                    }

                    System.out.println(c.toString());
                }

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Consensus ERROR for : " + g.getName());
        }

        return c;
    }

    private void loader() {

        for (StockGeneral g: CacheStockGeneral.getCache().values()) {

            boolean retry = false;
            String url = base + CacheStockGeneral.getIsinCache().get(g.getCode()).getName().toLowerCase().replaceAll(" ","-") + sep + g.getPlace().toLowerCase() + sep  + g.getCodif().toLowerCase() + sep + g.getCode().toLowerCase() +end;
            try {
                String text;

                //inifinite loop
                do {
                    text = ParserCommon.loadUrl(new URL(url));
                    /*if (text == null) retry = true;
                    else retry = false;*/
                } while (retry);

                if (text != null) {
                    Document doc = Jsoup.parse(text);
                    Elements links = doc.select(refCode);
                    Elements sn = links.get(0).children();
                    Consensus c = new Consensus();
                    for (int i = 1; i < 7; i++) {
                        c.getNotation(i).setSell(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(0).text()));
                        c.getNotation(i).setRelieve(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(1).text()));
                        c.getNotation(i).setKeep(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(2).text()));
                        c.getNotation(i).setReinforce(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(3).text()));
                        c.getNotation(i).setBuy(new Integer(sn.get(i).children().get(3).children().get(0).children().get(0).children().get(0).child(4).text()));
                    }

                    System.out.println(c.toString());
                }

            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Consensus ERROR for : " + g.getName());
            }
        }

    }


}
