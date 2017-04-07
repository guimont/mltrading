package com.mltrading.models.stock.cache;

/**
 * Created by gmo on 04/04/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fast coding.. better to use database or file to store this listing
 * but purpose is to finish a version of this project quickly
 *
 */
public class CacheArticle {
    private Map<String, ArticlesUrl> cacheArticle = new HashMap<>();


    public CacheArticle() {
        new ArticlesUrl().add("").add("");
    }






    private class ArticlesUrl {
        List<String> urls = new ArrayList<>();

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        public List<String> add(String s) {
            return urls;
        }
    }
}
