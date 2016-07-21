package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 18/12/2015.
 */
public class CacheStockIndice {

    public static int INDICE_EFCHI_POS=1;

    private static final Map<String, StockIndice> IndiceMap;
    static {
        Map<String, StockIndice> aMap = new HashMap<>();
        aMap.put("EFCHI", new StockIndice("EFCHI", "cac 40"));
        aMap.put("EDJI", new StockIndice("EDJI", "dji"));
        aMap.put("EN225", new StockIndice("EN225", "nikkei"));
        aMap.put("EFTSE", new StockIndice("EFTSE", "ftse"));
        aMap.put("EGDAXI", new StockIndice("EGDAXI", "dax"));
        aMap.put("ESTOXX50E", new StockIndice("ESTOXX50E", "stoxx 50"));

        IndiceMap = Collections.unmodifiableMap(aMap);
    }


    public static Map<String,StockIndice> getIndiceCache() {
        return IndiceMap;
    }


}
