package com.mltrading.models.stock.cache;

import com.mltrading.models.stock.StockRawMat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 17/06/2015.
 */
public class CacheRawMaterial {

    public static int RAW_DTOE_POS  = 0;
    public static int RAW_ETOL_POS  = 1;
    public static int RAW_LTOD_POS  = 2;
    public static int RAW_ETOY_POS  = 3;
    public static int RAW_DTOC_POS   = 4;

    //public static int RAW_DJCOM_POS  = 5;
    //public static int RAW_DJCOMEN_POS   = 6;

    public static int RAW_CHANGE_POS   = 5;
    public static int RAW_PETB_POS  = RAW_CHANGE_POS;
    public static int RAW_GAZ_POS  = RAW_CHANGE_POS + 1;
    public static int RAW_GOLD_POS  = RAW_CHANGE_POS + 2;
    public static int RAW_SILV_POS  = RAW_CHANGE_POS + 3;
    public static int RAW_COPP_POS  = RAW_CHANGE_POS +4;
    public static int RAW_PLAT_POS  = RAW_CHANGE_POS +5;
    public static int RAW_PALA_POS  = RAW_CHANGE_POS +6;
    public static int RAW_WHEAT_POS  = RAW_CHANGE_POS +7;
    public static int RAW_CORN_POS  = RAW_CHANGE_POS +8;

    public static int RAW_PRIM_POS   = 14;

    public static int RAW_EURI1M_POS  = RAW_PRIM_POS;
    public static int RAW_EURI1Y_POS  = RAW_PRIM_POS +1;
    public static int RAW_EURI10Y_POS  = RAW_PRIM_POS +2;
    public static int RAW_USRI1M_POS  = RAW_PRIM_POS +3;
    public static int RAW_USRI1Y_POS  = RAW_PRIM_POS +4;
    public static int RAW_UKRI1M_POS  = RAW_PRIM_POS +5;
    public static int RAW_USRI10Y_POS  = RAW_PRIM_POS +6;
    public static int RAW_UKRI1Y_POS  = RAW_PRIM_POS +7;
    public static int RAW_UKRI10Y_POS  = RAW_PRIM_POS +8;

    public static int RAW_GYRI1M_POS  = RAW_PRIM_POS +9;
    public static int RAW_GYRI1Y_POS  = RAW_PRIM_POS +10;
    public static int RAW_GYRI10Y_POS  = RAW_PRIM_POS +11;

    public static int RAW_SPRI1M_POS  = RAW_PRIM_POS +12;
    public static int RAW_SPRI1Y_POS  = RAW_PRIM_POS +13;
    public static int RAW_SPRI10Y_POS  = RAW_PRIM_POS +14;


    public static int RAW_ITRI1M_POS  = RAW_PRIM_POS +15;
    public static int RAW_ITRI1Y_POS  = RAW_PRIM_POS +16;
    public static int RAW_ITRI10Y_POS  = RAW_PRIM_POS +17;

    public static int RAW_CHRI1Y_POS  = RAW_PRIM_POS +18;
    public static int RAW_CHRI10Y_POS  = RAW_PRIM_POS +19;




    public static int N_RAW = 34;




    private static final Map<String, StockRawMat> RawMap;

    static {
        Map<String, StockRawMat> aMap = new HashMap<>();
        aMap.put("DTOE", new StockRawMat("ETOD", "Euro_Dollar", "https://fr.investing.com/currencies/eur-usd-historical-data", RAW_DTOE_POS));
        aMap.put("ETOL", new StockRawMat("ETOL", "Euro_Livre", "https://fr.investing.com/currencies/eur-gbp-historical-data", RAW_ETOL_POS));
        aMap.put("LTOD", new StockRawMat("LTOD", "Livre_Dollar", "https://fr.investing.com/currencies/gbp-usd-historical-data", RAW_LTOD_POS));
        aMap.put("ETOY", new StockRawMat("ETOY", "Euro_Yen", "https://fr.investing.com/currencies/eur-jpy-historical-data", RAW_ETOY_POS));
        aMap.put("DTOC", new StockRawMat("DTOC", "Dollar_yuan", "https://fr.investing.com/currencies/usd-cny-historical-data", RAW_DTOC_POS));

        //not enough history
        //aMap.put("DJCOM", new StockRawMat("DJCOM", "DJ_Commodity", "https://fr.investing.com/indices/dj-commodity-historical-data", RAW_DJCOM_POS));
        //aMap.put("DJCOMEN", new StockRawMat("DJCOMEN", "DJ_Commodity_ennergy", "https://fr.investing.com/indices/dj-commodity-energy-historical-data", RAW_DJCOMEN_POS));


        aMap.put("PETB", new StockRawMat("PETB", "Petrole_Brent", "https://fr.investing.com/commodities/brent-oil-historical-data", RAW_PETB_POS));
        aMap.put("GAZ", new StockRawMat("GAZ", "Gaz", "https://fr.investing.com/commodities/natural-gas-historical-data", RAW_GAZ_POS));
        aMap.put("GOLD", new StockRawMat("GOLD", "Or", "https://fr.investing.com/commodities/gold-historical-data", RAW_GOLD_POS));
        aMap.put("SILV", new StockRawMat("SILV", "Argent", "https://fr.investing.com/commodities/silver-historical-data", RAW_SILV_POS));
        aMap.put("COPP", new StockRawMat("COPP", "Cuivre", "https://fr.investing.com/commodities/copper-historical-data", RAW_COPP_POS));
        aMap.put("PLAT", new StockRawMat("PLAT", "Platinium", "https://fr.investing.com/commodities/platinum-historical-data", RAW_PLAT_POS));
        aMap.put("PALA", new StockRawMat("PALA", "Paladium", "https://fr.investing.com/commodities/palladium-historical-data", RAW_PALA_POS));


        aMap.put("WHEAT", new StockRawMat("WHEAT", "Ble", "https://fr.investing.com/commodities/us-wheat-historical-data", RAW_WHEAT_POS));
        aMap.put("CORN", new StockRawMat("CORN", "Mais", "https://fr.investing.com/commodities/us-corn-historical-data",RAW_CORN_POS));

        aMap.put("EURI1M", new StockRawMat("EURI1M", "France_1_mois", "https://fr.investing.com/rates-bonds/france-1-month-bond-yield-historical-data", RAW_EURI1M_POS));
        aMap.put("EURI1Y", new StockRawMat("EURI1Y", "France_1_an", "https://fr.investing.com/rates-bonds/france-1-year-bond-yield-historical-data", RAW_EURI1Y_POS));
        aMap.put("EURI10Y", new StockRawMat("EURI10Y", "France_10_ans", "https://fr.investing.com/rates-bonds/france-10-year-bond-yield-historical-data", RAW_EURI10Y_POS));

        aMap.put("USRI1M", new StockRawMat("USRI1M", "Etats-Unis_1_mois", "https://fr.investing.com/rates-bonds/u.s.-1-month-bond-yield-historical-data", RAW_USRI1M_POS));
        aMap.put("USRI1Y", new StockRawMat("USRI1Y", "Etats-Unis_1_an", "https://fr.investing.com/rates-bonds/u.s.-1-year-bond-yield-historical-data", RAW_USRI1Y_POS));
        aMap.put("USRI10Y", new StockRawMat("USRI10Y", "Etats-Unis_10_ans", "https://fr.investing.com/rates-bonds/u.s.-10-year-bond-yield-historical-data", RAW_USRI10Y_POS));

        aMap.put("UKRI1M", new StockRawMat("UKRI1M", "UK_1_mois", "https://fr.investing.com/rates-bonds/uk-1-month-bond-yield-historical-data", RAW_UKRI1M_POS));
        aMap.put("UKRI1Y", new StockRawMat("UKRI1Y", "UK_1_an", "https://fr.investing.com/rates-bonds/uk-1-year-bond-yield-historical-data", RAW_UKRI1Y_POS));
        aMap.put("UKRI10Y", new StockRawMat("UKRI10Y", "UK_10_ans", "https://fr.investing.com/rates-bonds/uk-10-year-bond-yield-historical-data", RAW_UKRI10Y_POS));

        aMap.put("GYRI1M", new StockRawMat("GYRI1M", "Germany_1_mois", "https://fr.investing.com/rates-bonds/germany-1-month-bond-yield-historical-data", RAW_GYRI1M_POS));
        aMap.put("GYRI1Y", new StockRawMat("GYRI1Y", "Germany_1_an", "https://fr.investing.com/rates-bonds/germany-1-year-bond-yield-historical-data", RAW_GYRI1Y_POS));
        aMap.put("GYRI10Y", new StockRawMat("GYRI10Y", "Germany_10_ans", "https://fr.investing.com/rates-bonds/germany-10-year-bond-yield-historical-data", RAW_GYRI10Y_POS));

        aMap.put("SPRI1M", new StockRawMat("SPRI1M", "Spain_3_mois", "https://fr.investing.com/rates-bonds/spain-3-month-bond-yield-historical-data", RAW_SPRI1M_POS));
        aMap.put("SPRI1Y", new StockRawMat("SPRI1Y", "Spain_1_an", "https://fr.investing.com/rates-bonds/spain-1-year-bond-yield-historical-data", RAW_SPRI1Y_POS));
        aMap.put("SPRI10Y", new StockRawMat("SPRI10Y", "Spain_10_ans", "https://fr.investing.com/rates-bonds/spain-10-year-bond-yield-historical-data", RAW_SPRI10Y_POS));

        aMap.put("ITRI1M", new StockRawMat("ITRI1M", "Italy_3_mois", "https://fr.investing.com/rates-bonds/italy-3-month-bond-yield-historical-data", RAW_ITRI1M_POS));
        aMap.put("ITRI1Y", new StockRawMat("ITRI1Y", "Italy_1_an", "https://fr.investing.com/rates-bonds/italy-1-year-bond-yield-historical-data", RAW_ITRI1Y_POS));
        aMap.put("ITRI10Y", new StockRawMat("ITRI10Y", "Italy_10_ans", "https://fr.investing.com/rates-bonds/italy-10-year-bond-yield-historical-data", RAW_ITRI10Y_POS));

        aMap.put("CHRI1Y", new StockRawMat("CHRI1Y", "China_1_an", "https://fr.investing.com/rates-bonds/china-1-year-bond-yield-historical-data", RAW_CHRI1Y_POS));
        aMap.put("CHRI10Y", new StockRawMat("CHRI10Y", "China_10_ans", "https://fr.investing.com/rates-bonds/china-10-year-bond-yield-historical-data", RAW_CHRI10Y_POS));




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
