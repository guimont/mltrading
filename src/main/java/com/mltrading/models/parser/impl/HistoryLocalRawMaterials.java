package com.mltrading.models.parser.impl;

import com.mltrading.models.parser.HistoryRawMaterialsParser;
import com.mltrading.models.parser.ParserCommon;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gmo on 02/12/2015.
 */
public class HistoryLocalRawMaterials implements HistoryRawMaterialsParser {

    @Override
    public void fetch() {
        loader();
    }

    final String url = "http://localhost:8090/petrol.html";
    final String refCode = "td";

    public  void loader() {
        try {
            String text = ParserCommon.loadUrl(new URL(url));
            Document doc = Jsoup.parse(text);
            Elements links = doc.select(refCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
