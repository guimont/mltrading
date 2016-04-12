package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;
import com.mltrading.dao.InfluxDaoConnectorNotation;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.ArticleParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.HistogramDocument;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 09/03/2016.
 */
@Singleton
public class ArticleParserEchos implements ArticleParser {

    private static final Logger log = LoggerFactory.getLogger(HistogramDocument.class);
    static String refCode = "div.contenu_article";

    static Map<String, Integer>  notationCache = new HashMap<>();
    static Map<String, Integer>  ignoredCache = new HashMap<>();

    public ArticleParserEchos() {
        openNotationFile(notationCache);
        //openIgnoreFile(ignoredCache);
    }

    @Override
    public void fetch() {
        loader();
    }

    @Override
    public void fetchCurrent() {
    //get article
        //si non present add
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            String dateRef = HistogramDocument.getLastDateHistory(g.getCodif() + "R");
            loaderFrom(g, dateRef);
        }

    }

    @Override
    public void fetchSpecific(StockGeneral g) {
        loaderFrom(g, "2010-01-01");
    }


    private void loaderFrom(StockGeneral g, String dateRef) {
        DateTime dref = new DateTime(dateRef);

        List<StockDocument> sds =  StockDocument.getStockDocumentInvert(g.getCodif());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (StockDocument d: sds) {
            if (dref.isAfter(d.getTimeInsert())) {
                return;
            }
            String url = d.getRef();
            try {
                String text;

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!url.startsWith("http")) {
                    url = url.replace("href=\"http://", "http://").replaceAll("\"", "");
                    url = url.replace("href=", "http://investir.lesechos.fr").replaceAll("\"", "");
                }
                System.out.println(url);
                text = ParserCommon.loadUrl(new URL(url));



                Document doc = Jsoup.parse(text);

                /**
                 * Check balise number, if too much dont refer to action
                 */
                if (doc.select("div.bloc-tags").select("li").size() <5) {

                    Elements links = doc.select(refCode);
                    HistogramDocument hd = new HistogramDocument(d.getCode()+"R", d.getDate());

                    Elements sentences = links.select("p");

                    for (Element sentence : sentences) {
                        hd.analyseDocument(Jsoup.parse(sentence.toString()).text(), notationCache);
                    }

                    log.info("resultat: " + hd.sum());
                    //Element list = links.get(0);

                    BatchPoints bp = InfluxDaoConnectorNotation.getBatchPoints();
                    ArticleParser.saveNotation(bp, hd);

                    InfluxDaoConnectorNotation.writePoints(bp);
                }

            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }


        }
    }


    public static void openIgnoreFile( Map<String, Integer> ignoredCache) {
        String fileName = "ignored.txt";
        //lecture du fichier texte
        try{
            InputStream ips=new FileInputStream(fileName);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String ligne;
            while ((ligne=br.readLine())!=null){
                String word = ligne.toLowerCase();
                Integer note = new Integer(0);
                ignoredCache.put(word,note);
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static void openNotationFile( Map<String, Integer> notationCache) {
        //String fileName = "/home/gmo/notation.txt";
        String fileName = "notation.txt";
        //lecture du fichier texte
        try{
            InputStream ips=new FileInputStream(fileName);
            InputStreamReader ipsr=new InputStreamReader(ips, "UTF8");
            BufferedReader br=new BufferedReader(ipsr);
            String ligne;
            while ((ligne=br.readLine())!=null){

                String word = ligne.split(" ")[0].toLowerCase();
                Integer note = new Integer(ligne.split(" ")[1]);
                //if (notationCache.get(word) != null) System.out.println(word);
                notationCache.put(word,note);
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void loader() {
        int i = 0;
        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {

            if (i++ < 15) continue;
            List<StockDocument> sds =  StockDocument.getStockDocument(g.getCodif());

            /*try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            for (StockDocument d: sds) {
                String url = d.getRef();
                try {
                    String text;

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!url.startsWith("http"))
                        url = url.replace("href=\"","http://investir.lesechos.fr").replaceAll("\"","");
                    System.out.println(url);
                    text = ParserCommon.loadUrl(new URL(url));



                    Document doc = Jsoup.parse(text);

                    /**
                     * Check balise number, if too much dont refer to action
                     */
                    if (doc.select("div.bloc-tags").select("li").size() <5) {

                        Elements links = doc.select(refCode);
                        HistogramDocument hd = new HistogramDocument(d.getCode(), d.getDate());

                        Elements sentences = links.select("p");

                        for (Element sentence : sentences) {
                            hd.analyseDocument(Jsoup.parse(sentence.toString()).text(), notationCache);
                        }

                        log.info("resultat: " + hd.sum());
                        //Element list = links.get(0);

                        BatchPoints bp = InfluxDaoConnectorNotation.getBatchPoints();
                        ArticleParser.saveNotation(bp, hd);

                        InfluxDaoConnectorNotation.writePoints(bp);
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("ERROR for : " + g.getName());
                }

            }
        }
    }



}
