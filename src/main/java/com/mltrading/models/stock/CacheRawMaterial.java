package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gmo on 17/06/2015.
 */
public class CacheRawMaterial {

    public static int RAW_DTOE_POS  = 0;
    public static int RAW_PETB_POS  = 1;
    public static int RAW_GOLD_POS  = 2;
    public static int RAW_SILV_POS  = 3;
    public static int RAW_COPP_POS   = 4;
    public static int RAW_GAZ_POS  = 5;
    public static int RAW_EURI1M_POS   = 6;
    public static int RAW_EURI1Y_POS  = 7;
    public static int RAW_EURI10Y_POS  = 8;
    public static int RAW_USRI1M_POS  = 9;
    public static int RAW_USRI1Y_POS  = 10;
    public static int RAW_USRI10Y_POS  = 11;
    public static int RAW_WHEAT_POS  = 12;

    public static int N_RAW = 13;




    private static final Map<String, StockRawMat> RawMap;

    static {
        Map<String, StockRawMat> aMap = new HashMap<>();
        aMap.put("DTOE", new StockRawMat("DTOE", "dollar_euro", "http://fr.investing.com/currencies/eur-usd-historical-data", RAW_DTOE_POS));
        aMap.put("PETB", new StockRawMat("PETB", "Petrole_Brent", "http://fr.investing.com/commodities/brent-oil-historical-data", RAW_PETB_POS));

        aMap.put("GOLD", new StockRawMat("GOLD", "Or", "http://fr.investing.com/commodities/gold-historical-data", RAW_GOLD_POS));
        aMap.put("SILV", new StockRawMat("SILV", "Argent", "http://fr.investing.com/commodities/silver-historical-data", RAW_SILV_POS));
        aMap.put("COPP", new StockRawMat("COPP", "Cuivre", "http://fr.investing.com/commodities/copper-historical-data", RAW_COPP_POS));
        aMap.put("GAZ", new StockRawMat("GAZ", "Gaz", "http://fr.investing.com/commodities/natural-gas-historical-data", RAW_GAZ_POS));

        aMap.put("WHEAT", new StockRawMat("WHEAT", "Ble", "http://fr.investing.com/commodities/us-wheat-historical-data", RAW_WHEAT_POS));
        //aMap.put("CORN", new StockRawMat("CORN", "Maïs", "http://fr.investing.com/commodities/us-corn-historical-data"));

        aMap.put("EURI1M", new StockRawMat("EURI1M", "France_1_mois", "http://fr.investing.com/rates-bonds/france-1-month-bond-yield-historical-data", RAW_EURI1M_POS));
        aMap.put("EURI1Y", new StockRawMat("EURI1Y", "France_1_an", "http://fr.investing.com/rates-bonds/france-1-year-bond-yield-historical-data", RAW_EURI1Y_POS));
        aMap.put("EURI10Y", new StockRawMat("EURI10Y", "France_10_ans", "http://fr.investing.com/rates-bonds/france-10-year-bond-yield-historical-data", RAW_EURI10Y_POS));

        aMap.put("USRI1M", new StockRawMat("USRI1M", "Etats-Unis_1_mois", "http://fr.investing.com/rates-bonds/u.s.-1-month-bond-yield-historical-data", RAW_USRI1M_POS));
        aMap.put("USRI1Y", new StockRawMat("USRI1Y", "Etats-Unis_1_an", "http://fr.investing.com/rates-bonds/u.s.-1-year-bond-yield-historical-data", RAW_USRI1Y_POS));
        aMap.put("USRI10Y", new StockRawMat("USRI10Y", "Etats-Unis_10_ans", "http://fr.investing.com/rates-bonds/u.s.-10-year-bond-yield-historical-data", RAW_USRI10Y_POS));

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
