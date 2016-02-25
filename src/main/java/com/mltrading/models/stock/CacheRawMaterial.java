package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gmo on 17/06/2015.
 */
public class CacheRawMaterial {


    private static final Map<String, StockRawMat> RawMap;

    static {
        Map<String, StockRawMat> aMap = new HashMap<>();
        aMap.put("DTOE", new StockRawMat("DTOE", "dollar_euro", "http://fr.investing.com/currencies/eur-usd-historical-data"));
        aMap.put("PETB", new StockRawMat("PETB", "Petrole_Brent", "http://fr.investing.com/commodities/brent-oil-historical-data"));

        aMap.put("GOLD", new StockRawMat("GOLD", "Or", "http://fr.investing.com/commodities/gold-historical-data"));
        aMap.put("SILV", new StockRawMat("SILV", "Argent", "http://fr.investing.com/commodities/silver-historical-data"));
        aMap.put("GOLD", new StockRawMat("COPP", "Cuivre", "http://fr.investing.com/commodities/copper-historical-data"));
        aMap.put("GAZ", new StockRawMat("GAZ", "Gaz", "http://fr.investing.com/commodities/natural-gas-historical-data"));

        aMap.put("WHEAT", new StockRawMat("WHEAT", "Ble", "http://fr.investing.com/commodities/us-wheat-historical-data"));
        aMap.put("CORN", new StockRawMat("CORN", "Maïs", "http://fr.investing.com/commodities/us-corn-historical-data"));

        aMap.put("EURI1M", new StockRawMat("EURI1M", "France_1_mois", "http://fr.investing.com/rates-bonds/france-1-month-bond-yield-historical-data"));
        aMap.put("EURI1Y", new StockRawMat("EURI1Y", "France_1_an", "http://fr.investing.com/rates-bonds/france-1-year-bond-yield-historical-data"));
        aMap.put("EURI10Y", new StockRawMat("EURI10Y", "France_10_ans", "http://fr.investing.com/rates-bonds/france-10-year-bond-yield-historical-data"));

        aMap.put("USRI1M", new StockRawMat("USRI1M", "Etats-Unis_1_mois", " http://fr.investing.com/rates-bonds/u.s.-1-month-bond-yield-historical-data"));
        aMap.put("USRI1Y", new StockRawMat("USRI1Y", "Etats-Unis_1_an", "http://fr.investing.com/rates-bonds/u.s.-1-year-bond-yield-historical-data"));
        aMap.put("USRI10Y", new StockRawMat("USRI10Y", "Etats-Unis_10_ans", "http://fr.investing.com/rates-bonds/u.s.-10-year-bond-yield-historical-data"));

        RawMap = Collections.unmodifiableMap(aMap);
    }

    private CacheRawMaterial() {
    }

    private static class CacheStockGeneralHolder {
        /** Instance unique non pre-initialise */
        private final static CacheRawMaterial instance = new CacheRawMaterial();
    }


    public static Map<String,StockRawMat> getCache() {
        return CacheStockGeneralHolder.instance.RawMap;
    }

    public static void removeCache() {
        CacheStockGeneralHolder.instance.RawMap.clear();
    }

}
