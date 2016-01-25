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
import java.util.regex.Pattern;

/**
 * Created by gmo on 21/11/2015.
 */

@Singleton
public class RealTimeParserYahoo implements RealTimeParser {

    static String cac40 = "https://fr.finance.yahoo.com/q/cp?s=%5EFCHI";
    static String refCode = "tbody";


    public static int refreshCache() {
        return refreshStock(cac40);

    }

    public static int loaderCache() {
        return loaderStock(cac40);

    }

    private static int refreshStock(String url) {

        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            for (Element link : links) {
                if (link.children().size() > 40) {
                    Elements sublinks  = link.children().select("tr");
                    for (Element elt : sublinks) {
                        Elements t = elt.select("td");
                        if (t.size() > 3) {
                            try {
                                String[] refSplit = t.get(0).text().split(Pattern.quote("."));

                                String codif = refSplit[0];
                                Float value = new Float(t.get(2).child(0).text().replace(",", "."));
                                //System.out.println(g.getName());

                                Float variation = new Float(t.get(3).child(0).child(1).text().replace(",", "."));
                                Integer volume = new Integer(t.get(4).child(0).text().replaceAll(" ", ""));

                                StockGeneral g = CacheStockGeneral.getCache().get(CacheStockGeneral.getCode(codif));
                                g.setValue(value);
                                g.setVariation(variation);
                                g.setVolume(volume);
                            }
                            catch (IndexOutOfBoundsException e){
                                System.out.println(e);
                            }
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private static int loaderStock(String url) {

        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            for (Element link : links) {
                if (link.children().size() > 40) {
                    Elements sublinks  = link.children().select("tr");
                    for (Element elt : sublinks) {
                        Elements t = elt.select("td");
                        if (t.size() > 3) {
                            StockGeneral g = new StockGeneral();
                            String[] refSplit = t.get(0).text().split(Pattern.quote("."));
                            g.setName(t.get(1).text());
                            g.setPlaceCodif(refSplit[1]);
                            g.setCodif(refSplit[0]);
                            g.setValue(new Float(t.get(2).child(0).text().replace(",", ".")));
                            //System.out.println(g.getName());
                            try {
                                g.setVariation(new Float(t.get(3).child(0).child(1).text().replace(",", ".")));
                                g.setVolume(new Integer(t.get(4).child(0).text().replaceAll(" ", "")));
                            }
                            catch (IndexOutOfBoundsException e){
                                System.out.println(e);
                            }

                            g.setCode(CacheStockGeneral.getCode(g.getCodif()));
                            g.setPlace(CacheStockGeneral.getPlace(g.getCodif()));

                            CacheStockGeneral.getCache().put(g.getCode(),g);
                        }
                    }
                }
            }


            System.out.println(CacheStockGeneral.getCache().size());


            /*for (StockGeneral g: CacheStockGeneral.getCache().values()) {
                System.out.println(g.getCode());
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
