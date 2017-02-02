package com.mltrading.ml;

import com.mltrading.models.parser.ParserCommon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 02/02/2017.
 */
public class SynchWorker {

    private static List<String> uriList = new ArrayList<>();


    public static void addUri(String uri) {
        uriList.add(uri);
    }

    public static void delete() {
        for (String uri : uriList) {
            try {
                ParserCommon.loadUrl(new URL(uri+"/delete"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        for (String uri : uriList) {
            try {
                ParserCommon.loadUrl(new URL(uri+"/save"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void load() {
        for (String uri : uriList) {
            try {
                ParserCommon.loadUrl(new URL(uri+"/load"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


}
