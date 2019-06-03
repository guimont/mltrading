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
        loaderCache(true);
        loaderCacheEx(true);

        return 1;
    }

    public int loaderCache(boolean init) {
        String cac40 =  "https://www.boursorama.com/bourse/actions/cotations/?quotation_az_filter[market]=1rPCAC";
        String cac40_2 =  "https://www.boursorama.com/bourse/actions/cotations/page-2?quotation_az_filter%5Bmarket%5D=1rPCAC";
        loaderStock(cac40,1, init);
        return loaderStock(cac40_2,2, init);
    }

    public int loaderCacheEx(boolean init) {
        int NB_PAGES= 6;
        String sbf120Start =  "https://www.boursorama.com/bourse/actions/cotations/page-";
        String sbf120End =  "?quotation_az_filter%5Bmarket%5D=1rPPX4&quotation_az_filter%5Bletter%5D=&quotation_az_filter%5Bfilter%5D=";
        for (int i =1; i< NB_PAGES; i++)
            loaderStockEx(sbf120Start+i+sbf120End,init);

        return 1;
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


    private int loaderStockEx(String url, boolean init) {

        String refCode = ".c-block";

        try {
            String text = loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            Elements sublinks = links.select("tr");

            for (Element link : sublinks) {


                if (!link.hasAttr("data-ist"))
                    continue;

                if (link.childNodes().size()<8)
                    continue;

                String balise = link.text();

                String data[] = balise.split(" ");

                if (data.length < 5) continue;

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

                if (init) {
                    if (CacheStockGeneral.getCode(codif) != null) continue;
                    g = new StockGeneral(codif);
                }
                else {
                    String code = CacheStockGeneral.getCode(codif);
                    g = CacheStockGeneral.getCacheEx().get(code);
                }



                StringBuilder name = new StringBuilder();
                int index = 0;
                for (; index< data.length; index++) {
                    if (Character.isDigit(data[index].charAt(0))) break;
                    name.append(data[index]);
                }
                g.setName(name.toString());
                g.setValue(new Double(link.getElementsByAttribute("data-ist-last").text().replaceAll(" ", "").replaceAll(" \\(c\\)","").replaceAll(" \\(s\\)","")));

                g.setVariation(new Double(link.getElementsByAttribute("data-ist-instant-variation").text().replaceAll("%", "")));

                g.setOpening(new Double(link.getElementsByAttribute("data-ist-open").text().replaceAll(" ", "").replace("ND","0")));

                g.setHighest(new Double(link.getElementsByAttribute("data-ist-high").text().replaceAll(" ", "").replace("ND","0")));

                g.setLowest(new Double(link.getElementsByAttribute("data-ist-low").text().replaceAll(" ", "").replace("ND","0")));

                g.setVolume(new Double(link.getElementsByAttribute("data-ist-totalvolume").text().replaceAll(" ", "")));


                g.setCode(CacheStockGeneral.getCodeEx(g.getCodif()));
                g.setPlace(CacheStockGeneral.getPlaceEx(g.getCodif()));
                g.setSector(CacheStockGeneral.getSectorEx(g.getCodif()));

                try {
                    if (init)
                        CacheStockGeneral.getCacheEx().put(g.getCode(), g);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }





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




            for (Element link : sublinks) {



                if (!link.hasAttr("data-ist"))
                    continue;

                String balise = link.text();

                String data[] = balise.split(" ");

                if (link.childNodes().size()<8)
                    continue;


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

                /* fix for URW on boursorama // stock is twice and second is not correct*/
                if (pageElt == 2 && codif.equalsIgnoreCase("URW"))
                    continue;



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
                System.out.println("load action: " + g.getName() +" code: " + codif);
                g.setValue(new Double(data[index].replaceAll(" \\(c\\)","").replaceAll(" \\(s\\)","")));
                g.setVariation(new Double(data[index+1].replaceAll("%", "")));
                g.setOpening(new Double(data[index+2].replace("ND","0")));
                g.setHighest(new Double(data[index+3].replace("ND","0")));
                g.setLowest(new Double(data[index+4].replace("ND","0")));
                g.setVolume(new Double(data[index+6].replaceAll(" ", "")));

                g.setCode(CacheStockGeneral.getCode(g.getCodif()));
                g.setPlace(CacheStockGeneral.getPlace(g.getCodif()));
                g.setSector(CacheStockGeneral.getSector(g.getCodif()));


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
