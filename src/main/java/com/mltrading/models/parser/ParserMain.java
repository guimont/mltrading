package com.mltrading.models.parser;


import com.mltrading.models.stock.CacheRawMaterial;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;


/**
 * Created by gmo on 17/06/2015.
 */

@Deprecated
public class ParserMain {

    //static String cac40 = "http://bourse.lesechos.fr/bourse/actions/cours_az.jsp?select_fMARCHE_COURS=ind_FR0003500008&col=&ord=&page=&lettre=A";

    static String cac40 = "http://investir.lesechos.fr/actions/cotations/cours-az.html?select_fMARCHE_COURS=ind_FR0003500008&col=&ord=&page=&lettre=A";
    static String rawMatPetrole = "http://bourse.lesechos.fr/bourse/matieres_premieres/petrole.jsp";
    static String rawMatMetal = "http://bourse.lesechos.fr/bourse/matieres_premieres/metaux.jsp";


    static String refCode = "tr";


    public static void loaderAll() {
        loaderStock(cac40);
        //loaderRawMaterials(rawMatPetrole);
        //loaderRawMaterials(rawMatMetal);
    }

    public static void loaderStock(String url) {

        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            for (Element link : links) {

                String linkItem = link.attr("data-item");
                if ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("actionsParisTr") == 0) {
                    StockGeneral g = new StockGeneral();
                    g.setCode(link.attr("data-code"));
                    g.setPlace(link.attr("data-place"));
                    g.setCodif(link.attr("data-codif"));
                    Elements es = link.select("span");
                    for (Element e : es) {
                        String linkField = e.attr("data-field");
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("name") == 0)
                            g.setName(e.text());
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("valorisation") == 0)      {
                            String t = e.text().replace(',','.');
                            g.setValue(Float.parseFloat(t));
                        }

                    }

                    Elements et = link.select("td");
                    for (Element e : et) {
                        String linkField = e.attr("data-field");
                                          if (!linkField.isEmpty() && linkField.compareToIgnoreCase("open") == 0)      {
                            String t = e.text().replace(',','.');
                            g.setOpening(Float.parseFloat(t));
                        }
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("high") == 0)      {
                            String t = e.text().replace(',','.');
                            g.setHighest(Float.parseFloat(t));
                        }
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("low") == 0)      {
                            String t = e.text().replace(',','.');
                            g.setLowest(Float.parseFloat(t));
                        }

                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("variation") == 0)      {
                            String t = e.text().replace(',','.').replace('%',' ');//.replace('+','0');
                            g.setVariation(Float.parseFloat(t));
                        }
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("variationY") == 0)      {
                            String t = e.text().replace(',','.').replace('%',' ');
                            g.setFirstJanuaryVariation(Float.parseFloat(t));
                        }

                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("volume") == 0)      {
                            String t = e.text().replace('�',' ').replaceAll(" ","");
                            g.setVolume(Integer.parseInt(t));
                        }


                    }

                    CacheStockGeneral.getCache().put(g.getCode(),g);
                }

                /*if ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("matieresPetroleTr") == 0) {
                    StockGeneral g = new StockGeneral();
                    g.setCode(link.attr("data-code"));
                    g.setPlace(link.attr("data-place"));
                    g.setCodif(link.attr("data-codif"));
                    Elements es = link.select("span");
                    for (Element e : es) {
                        String linkField = e.attr("data-field");
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("name") == 0)
                            g.setName(e.text());
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("valorisation") == 0) {
                            String t = e.text().replace(',', '.');
                            g.setValue(Float.parseFloat(t));
                        }

                    }
                    CacheRawMaterial.getCache().put(g.getCode(),g);
                }*/

            }

            System.out.println(CacheStockGeneral.getCache().size());


            for (StockGeneral g: CacheStockGeneral.getCache().values()) {
                System.out.println(g.getCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    public static void loaderRawMaterials(String url) {
        try {
            String text = ParserCommon.loadUrl(new URL(url));

            Document doc = Jsoup.parse(text);

            Elements links = doc.select(refCode);
            for (Element link : links) {

                String linkItem = link.attr("data-item");

                if (((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("matieresPetroleTr") == 0) ||
                        ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("matieresGazoleTr") == 0) ||
                        ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("matieresGazTr") == 0) ||
                        ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("matieresFuelTr") == 0) ||
                        ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("matieresParisTr") == 0) ||
                        ((!linkItem.isEmpty()) && linkItem.compareToIgnoreCase("matieresLondresTr") == 0)) {
                    StockGeneral g = new StockGeneral();
                    g.setCode(link.attr("data-code"));
                    g.setPlace(link.attr("data-place"));
                    g.setCodif(link.attr("data-codif"));
                    Elements es = link.select("span");
                    for (Element e : es) {
                        String linkField = e.attr("data-field");
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("name") == 0)
                            g.setName(e.text());
                        if (!linkField.isEmpty() && linkField.compareToIgnoreCase("valorisation") == 0) {
                            String t = e.text().replace(',', '.');
                            g.setValue(Float.parseFloat(t));
                        }

                    }
                    CacheRawMaterial.getCache().put(g.getCode(),g);
                }
            }


            System.out.println(CacheRawMaterial.getCache().size());


            for (StockGeneral g: CacheRawMaterial.getCache().values()) {
                System.out.println(g.getCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

}
