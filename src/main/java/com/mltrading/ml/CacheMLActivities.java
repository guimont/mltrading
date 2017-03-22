package com.mltrading.ml;

import com.google.inject.Singleton;
import com.mltrading.models.util.MLActivities;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gmo on 09/02/2017.
 */
@Singleton
public class CacheMLActivities {
    private static List<MLActivities> cache = new ArrayList<>();
    private static long countGlobal = 0;


    public static void addActivities(MLActivities a) {
        cache.add(a);
    }


    public static List<MLActivities> getActivities() {
        return cache.subList(cache.size()-100 > 0 ? cache.size()-100:0, cache.size());
    }

    public static long getCountGlobal() {
        return countGlobal;
    }

    public static void setCountGlobal(long countGlobal) {
        CacheMLActivities.countGlobal = countGlobal;
    }
}
