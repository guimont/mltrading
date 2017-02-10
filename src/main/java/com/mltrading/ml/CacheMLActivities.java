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


    public static void addActivities(MLActivities a) {
        cache.add(a);
    }


    public static List<MLActivities> getActivities() {
        return cache;
    }



}
