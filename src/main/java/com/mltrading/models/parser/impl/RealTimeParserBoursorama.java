package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.RealTimeParser;
import com.mltrading.models.stock.cache.CacheStockGeneral;
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

    //static String cac40 = "http://www.boursorama.com/bourse/actions/cours_az.phtml?MARCHE=1rPCAC&validate=";
    static String cac40 = "http://www.boursorama.com/bourse/actions/cours_az.phtml?MARCHE=1rPCAC&valid=";
    static String refCode = "tbody";


    public static int refreshCache() {
        return refreshCache(cac40);

    }

    public static int loaderCache() {
        return loaderStock(cac40);

    }


    private static int refreshCache(String url) {

        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            Elements sublinks = links.select("tr");

            for (Element link : sublinks) {


                if (!link.hasAttr("href")) break;

                String balise = link.getElementsByAttribute("href").get(1).toString();

                String codif;
                /* specific format for solvay*/
                /* specific format for solvay*/
                if (balise.contains("SOLB")) {
                    codif = "SOLB";
                } else if (balise.contains("OREAL")) {
                    codif = "OREAL";
                } else {

                    String splitRes[] = balise.split("=1r");
                    splitRes = splitRes[1].split("\">");
                    codif = splitRes[0].substring(1);
                }

                String code = CacheStockGeneral.getCode(codif);
                if (code != null) {
                    StockGeneral g = CacheStockGeneral.getCache().get(code);

                    g.setValue(new Double(link.child(3).text().replaceAll(" \\(c\\)", "")));
                    g.setVariation(new Double(link.child(4).text().replaceAll("%", "")));
                    g.setOpening(new Double(link.child(5).text().replace("ND","0")));
                    g.setVolume(new Double(link.child(9).text().replaceAll(" ", "")));


                    try {
                        CacheStockGeneral.getCache().put(g.getCode(), g);
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }
            }


            //System.out.println(CacheStockGeneral.getCache().size());


            /*for (StockGeneral g: CacheStockGeneral.getCache().values()) {
                System.out.println(g.getCode());
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }






    /**
     * use boursorama to create cac 40
     * @param url
     * @return
     */
    private static int loaderStock(String url) {

        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            Elements sublinks = links.select("tr");

            for (Element link : sublinks) {

                StockGeneral g = new StockGeneral();

                if (!link.hasAttr("href")) break;

                String balise = link.getElementsByAttribute("href").get(1).toString();

                /* specific format for solvay*/
                if (balise.contains("SOLB")) {
                    g.setCodif("SOLB");
                } else if (balise.contains("OREAL")) {
                    g.setCodif("OREAL");
                } else {

                    String splitRes[] = balise.split("=1r");
                    splitRes = splitRes[1].split("\">");
                    g.setCodif(splitRes[0].substring(1));
                }
                g.setName(link.child(1).text());
                g.setValue(new Double(link.child(3).text().replaceAll(" \\(c\\)","")));
                g.setVariation(new Double(link.child(4).text().replaceAll("%", "")));
                g.setOpening(new Double(link.child(5).text().replace("ND","0")));
                g.setVolume(new Double(link.child(9).text().replaceAll(" ", "")));

                g.setCode(CacheStockGeneral.getCode(g.getCodif()));
                g.setPlace(CacheStockGeneral.getPlace(g.getCodif()));
                g.setSector(CacheStockGeneral.getSector(g.getCodif()));

                try {
                    CacheStockGeneral.getCache().put(g.getCode(), g);
                } catch (Exception e) {
                    System.out.print(e);
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
