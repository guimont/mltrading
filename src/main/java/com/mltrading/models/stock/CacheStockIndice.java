package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 18/12/2015.
 */
public class CacheStockIndice {

    private static final Map<String, StockIndice> IndiceMap;
    static {
        Map<String, StockIndice> aMap = new HashMap<>();
        aMap.put("EFCHI", new StockIndice("EFCHI", "cac40"));
        aMap.put("EDJI", new StockIndice("EDJI", "dji"));

        aMap.put("EN225", new StockIndice("EN225", "nikkei"));
        aMap.put("EFTSE", new StockIndice("EFTSE", "ftse"));

        IndiceMap = Collections.unmodifiableMap(aMap);
    }


    public static Map<String,StockIndice> getIndiceCache() {
        return IndiceMap;
    }

}
