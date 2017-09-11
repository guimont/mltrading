package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.ArticleNotation;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.*;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.repository.ArticleRepository;
import org.influxdb.dto.BatchPoints;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gmo on 09/03/2016.
 */
@Singleton
public class ArticleParserNotation implements ArticleNotation {

    private static final Logger log = LoggerFactory.getLogger(ArticleParserNotation.class);
    static String refCode = "div.contenu_article";

    static Map<String, Integer>  notationCache = new HashMap<>();
    static Map<String, Integer>  ignoredCache = new HashMap<>();


    public ArticleParserNotation() {
        openNotationFile(notationCache);
        //openIgnoreFile(ignoredCache);
    }


    /**
     * parse article after current date
     * @param articleRepository
     * @param g
     * @param dateRef
     */
    private void loaderFrom(ArticleRepository articleRepository, StockGeneral g, String dateRef) {
        DateTime dref = new DateTime(dateRef);

        List<StockDocument> sds =  StockDocument.getStockDocumentInvert(g.getCodif(), StockDocument.TYPE_ARTICLE);
        notationCache.put(g.getName(),0);

        if (sds != null) {
            for (StockDocument d : sds) {
                if (dref.isAfter(d.getTimeInsert())) {
                    return;
                }
                parse(articleRepository, g, d);

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

    private void openNotationFile( Map<String, Integer> notationCache) {
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

    /**
     * parse article
     * @param articleRepository
     */
    private void loader(ArticleRepository articleRepository) {
        int i = 0;
        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {

            notationCache.put(g.getName(),0);

            List<StockDocument> sds =  StockDocument.getStockDocument(g.getCodif(), StockDocument.TYPE_ARTICLE);

            if (sds != null) {

                for (StockDocument d : sds) {
                    parse(articleRepository, g, d);
                }
            }
        }
    }


    private void parse(ArticleRepository articleRepository, StockGeneral g , StockDocument d) {
        Article art = null;
        try {
            art = articleRepository.findOne(new ArticleKey(g.getCodif(),d.getDate()));
        } catch (Exception e) {
            return;
        }


        if (art == null || art.getTopic() == null) return; //no topic means article is not correct
        art.getTopic().forEach(s-> notationCache.put(s,0));

        try {

            HistogramDocument hd = new HistogramDocument(d.getCode()+"N", d.getDate());
            hd.setSource("ECHOS");

            for (String sentence : art.getLines()) {
                System.out.println(sentence);
                hd.analyseDocument(sentence, notationCache);
            }

            hd.setNote(hd.sum());

            Function<String, Stream<String>> f = line -> Pattern.compile("[^\\p{javaLetter}]").splitAsStream(line);
            List<String> words = art.getLines().stream().flatMap(f)
                .filter(s -> notationCache.containsKey(s))
                .map(String::toLowerCase).collect(Collectors.toList());

            Map<String, Long> r  = words.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

            double pertinence = r.values().stream().mapToLong(Long::intValue).sum();

            hd.setPertinence(pertinence);

            art.getTopic().forEach(e -> hd.setTf(r.get(e.toLowerCase())));
            art.getTopic().forEach(e -> hd.setTf_idf(new Double(r.get(e.toLowerCase())/pertinence)));


            BatchPoints bp = InfluxDaoConnector.getBatchPoints(HistogramDocument.dbName);

            saveDocument(bp,hd);

            InfluxDaoConnector.writePoints(bp);

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("ERROR for : " + g.getName());
        }
    }


    @Override
    public void fetch(ArticleRepository repository) {
        loader(repository);
    }

    @Override
    public void fetchCurrent(ArticleRepository repository) {
        //get article
        //si non present add
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            String dateRef = HistogramDocument.getLastDateHistory(g.getCodif() + "R");
            loaderFrom(repository, g, dateRef);
        }
    }


    /*
    public void fetchSpecific(ArticleRepository repository, StockGeneral g) {
        loaderFrom(repository, g, "2010-01-01");
    }*/
}
