package com.mltrading.models.parser.impl.boursorama;

import com.google.inject.Singleton;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.RealTimeParser;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import org.apache.commons.lang.StringUtils;
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
public class RealTimeParserBoursorama extends ParserCommon implements RealTimeParser {

    //static String cac40 = "http://www.boursorama.com/bourse/actions/cours_az.phtml?MARCHE=1rPCAC&validate=";
    //static String cac40 = "http://www.boursorama.com/bourse/actions/cours_az.phtml?MARCHE=1rPCAC&valid=";



    public int refreshCache() {
       return loaderCache(false);

    }

    public int loaderCache() {
        return loaderCache(true);
    }

    public int loaderCache(boolean init) {
        String cac40 =  "https://www.boursorama.com/bourse/actions/cotations/?quotation_az_filter[market]=1rPCAC";
        String cac40_2 =  "https://www.boursorama.com/bourse/actions/cotations/page-2?quotation_az_filter%5Bmarket%5D=1rPCAC";
        loaderStock(cac40,26, init);
        return loaderStock(cac40_2,12, init);
    }


    /*
    private int refreshCache(String url,int pageElt) {

        try {
            String text = loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            Elements sublinks = links.select("tr");

            int max = 0;


            for (Element link : sublinks) {

                if (max > pageElt) break;

                if (link.hasAttr("data-ist") == false)
                    continue;

                // if (!link.hasAttr("href")) break;

                String balise = link.text();

                String data[] = balise.split(" ");

                String codif =  link.attr("data-ist");
                if (codif.contains("1rP"))
                    codif = codif.split("1rP")[1];
                else if (codif.contains("1rA"))
                    codif = codif.split("1rA")[1];
                else if (codif.contains("FF11_"))
                    codif = codif.split("FF11_")[1];

                if (codif.equals("OR"))
                    codif = "OREAL";

                max++;

                String code = CacheStockGeneral.getCode(codif);
                if (code != null) {
                    StockGeneral g = CacheStockGeneral.getCache().get(code);

                    StringBuilder name = new StringBuilder();
                    int index = 0;
                    for (; index< data.length; index++) {
                        if (Character.isDigit(data[index].charAt(0)))
                            break;
                    }


                    g.setValue(new Double(data[index].replaceAll(" \\(c\\)","").replaceAll(" \\(s\\)","")));
                    g.setVariation(new Double(data[index+1].replaceAll("%", "")));
                    g.setOpening(new Double(data[index+2].replace("ND","0")));
                    g.setHighest(new Double(data[index+3].replace("ND","0")));
                    g.setLowest(new Double(data[index+4].replace("ND","0")));
                    g.setVolume(new Double(data[index+6].replaceAll(" ", "")));


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
            }*

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
*/




    /**
     * use boursorama to create cac 40
     * @param url cac4O url to parse
     * @return result
     */
    private int loaderStock(String url,int pageElt, boolean init) {

        String refCode = ".c-block";

        try {
            String text = loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            Elements sublinks = links.select("tr");

            int max = 0;

            for (Element link : sublinks) {


                if (max > pageElt) break;

                if (!link.hasAttr("data-ist"))
                    continue;

                String balise = link.text();

                String data[] = balise.split(" ");

                /*
                 * Specific mapping for value
                 */
                String codif =  link.attr("data-ist");
                if (codif.contains("1rP"))
                    codif = codif.split("1rP")[1];
                else if (codif.contains("1rA"))
                    codif = codif.split("1rA")[1];
                else if (codif.contains("FF11_"))
                    codif = codif.split("FF11_")[1];

                if (codif.equals("OR"))
                    codif = "OREAL";


                StockGeneral g;

                if (init)
                    g = new StockGeneral(codif);
                else {
                    String code = CacheStockGeneral.getCode(codif);
                    g = CacheStockGeneral.getCache().get(code);
                }



                StringBuilder name = new StringBuilder();
                int index = 0;
                for (; index< data.length; index++) {
                    if (Character.isDigit(data[index].charAt(0))) break;
                    name.append(data[index]);
                }
                g.setName(name.toString());
                g.setValue(new Double(data[index].replaceAll(" \\(c\\)","").replaceAll(" \\(s\\)","")));
                g.setVariation(new Double(data[index+1].replaceAll("%", "")));
                g.setOpening(new Double(data[index+2].replace("ND","0")));
                g.setHighest(new Double(data[index+3].replace("ND","0")));
                g.setLowest(new Double(data[index+4].replace("ND","0")));
                g.setVolume(new Double(data[index+6].replaceAll(" ", "")));

                g.setCode(CacheStockGeneral.getCode(g.getCodif()));
                g.setPlace(CacheStockGeneral.getPlace(g.getCodif()));
                g.setSector(CacheStockGeneral.getSector(g.getCodif()));

                max++;

                try {
                    if (init)
                        CacheStockGeneral.getCache().put(g.getCode(), g);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

}