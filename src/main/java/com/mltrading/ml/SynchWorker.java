package com.mltrading.ml;

import com.google.inject.Singleton;
import com.mltrading.config.MLProperties;
import com.mltrading.models.parser.ParserCommon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 02/02/2017.
 */
@Singleton
public class SynchWorker {

    private static List<String> uriList = new ArrayList<>();

    private static ParserCommon parserCommon = new ParserCommon();

    public static void addUri(String uri) {
        uriList.add(uri);
    }

    public static void delete() {
        for (String uri : uriList) {
            try {
                parserCommon.loadUrl(new URL("http://"+uri+"/delete"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        for (String uri : uriList) {
            try {
                parserCommon.loadUrl(new URL("http://"+uri+"/saveValidator"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void load() {
        for (String uri : uriList) {
            try {
                parserCommon.loadUrl(new URL("http://"+uri+"/load"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void init() {
        String uri = MLProperties.getProperty("worker");
        if (uri != null)
            addUri(uri);
    }


    public static void add(String uri) {
        if (uri != null)
            addUri(uri);
    }
}
