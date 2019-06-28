package com.mltrading.models.parser.impl.boursedirect;

import com.google.inject.Singleton;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.RealTimeParser;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Created by gmo on 20/11/2015.
 */

@Singleton
public class RealTimeParserBoursedirect extends ParserCommon implements RealTimeParser {

    //static String cac40 = "http://www.boursorama.com/bourse/actions/cours_az.phtml?MARCHE=1rPCAC&validate=";
    //static String cac40 = "http://www.boursorama.com/bourse/actions/cours_az.phtml?MARCHE=1rPCAC&valid=";



    public int refreshCache() {
       return loaderCache(false);

    }

    public int loaderCache() {
        loaderCache(true);
        return 1;
    }

    public int loaderCache(boolean init) {
        int NB_PAGES= 4;
        String sbf120Start =  "https://www.boursier.com/indices/composition/sbf-120-FR0003999481,FR-";
        String sbf120End =  ".html?tri=dcapi";
        for (int i =1; i< NB_PAGES; i++)
            loaderStock(sbf120Start+i+sbf120End,init);

        return 1;
    }




    /**
     * use boursorama to create cac 40
     * @param url cac4O url to parse
     * @return result
     */
    private int loaderStock(String url, boolean init) {


        try {
            String text = loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links =  doc.select("tr");

            Map<String, StockGeneral> ref = null;
            Map<String, StockGeneral> base = null;


            for (Element link : links) {



                if (link.child(2).text().equalsIgnoreCase("Variation") == true)
                    continue;

                String balise = link.text();

                String data[] = balise.split(" ");

                if (link.childNodes().size()<8)
                    continue;


                /*
                 * Specific mapping for value
                 */
                String nameSTock =  link.child(0).text();

                String codif = CacheStockGeneral.getCodeByName(nameSTock);
                if (CacheStockGeneral.getCode(codif) != null) {
                    base = CacheStockGeneral.getCache();
                    ref= CacheStockGeneral.getIsinCache();
                } else {
                    codif = CacheStockGeneral.getCodeExByName(nameSTock);

                    if (codif != null) {
                        base = CacheStockGeneral.getCacheEx();
                        ref= CacheStockGeneral.getIsinExCache();
                    } else {
                        System.out.println(nameSTock);
                        continue;
                    }

                }

                StockGeneral g;

                if (init)
                    g = new StockGeneral(codif);
                else {
                    String code = CacheStockGeneral.getCode(ref,codif);
                    g = base.get(code);
                }



                /*StringBuilder name = new StringBuilder();
                int index = 0;
                for (; index< data.length; index++) {
                    if (Character.isDigit(data[index].charAt(0))) break;
                    name.append(data[index]);
                }*/
                g.setName(nameSTock);
                if (init) System.out.println("load action: " + g.getName() +" code: " + codif);
                g.setValue(new Double(link.child(1).text().replaceAll(String.valueOf((char) 160), "").replaceAll(" ", "").replaceAll(",",".").replaceAll("€","").replaceAll(" \\(c\\)","").replaceAll(" \\(s\\)","")));

                g.setVariation(new Double(link.child(2).text().replaceAll(String.valueOf((char) 160), "").replaceAll("%", "").replaceAll(",",".").replaceAll("\\+", "").replaceAll("-", "")));

                g.setOpening(new Double(link.child(3).text().replaceAll(String.valueOf((char) 160), "").replaceAll(" ", "").replaceAll(",",".").replaceAll("€","").replaceAll(" ", "").replace("ND","0")));

                g.setHighest(new Double(link.child(4).text().replaceAll(String.valueOf((char) 160), "").replaceAll(" ", "").replaceAll(",",".").replaceAll("€","").replaceAll(" ", "").replace("ND","0")));

                g.setLowest(new Double(link.child(5).text().replaceAll(String.valueOf((char) 160), "").replaceAll(",",".").replaceAll("€","").replaceAll(" ", "").replace("ND","0")));

                g.setVolume(new Double((link.child(6).text().replaceAll(" ", "").replaceAll(String.valueOf((char) 160), ""))));


                g.setCode(CacheStockGeneral.getCode(ref,g.getCodif()));
                g.setPlace(CacheStockGeneral.getPlace(ref,g.getCodif()));
                g.setSector(CacheStockGeneral.getSector(ref,g.getCodif()));


                try {
                    if (init)
                        base.put(g.getCode(), g);
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
