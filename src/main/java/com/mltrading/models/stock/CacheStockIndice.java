package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 18/12/2015.
 */
public class CacheStockIndice {

    public static int INDICE_EFCHI_POS  = 0;
    public static int INDICE_EFTSE_POS  = 1;
    public static int INDICE_EGDAXI_POS  = 2;
    public static int INDICE_ESTOXX50E_POS  = 3;
    public static int INDICE_ESSMI_POS   = 4;
    public static int INDICE_EDJI_POS  = 5;
    public static int INDICE_ENYA_POS   = 6;
    public static int INDICE_ENDX_POS  = 7;
    public static int INDICE_EN225_POS  = 8;
    public static int INDICE_EHSI_POS  = 9;
    public static int INDICE_EBSESN_POS  = 10;
    public static int INDICE_ETA100_POS  = 11;

    public static int N_INDICE  = 12;


    private static final Map<String, StockIndice> IndiceMap;
    static {
        Map<String, StockIndice> aMap = new HashMap<>();
        aMap.put("EFCHI", new StockIndice("EFCHI", "cac 40 french", INDICE_EFCHI_POS));
        aMap.put("EFTSE", new StockIndice("EFTSE", "ftse britain", INDICE_EFTSE_POS));
        aMap.put("EGDAXI", new StockIndice("EGDAXI", "dax deutch", INDICE_EGDAXI_POS));
        aMap.put("ESTOXX50E", new StockIndice("ESTOXX50E", "stoxx 50 UE", INDICE_ESTOXX50E_POS));
        aMap.put("ESSMI", new StockIndice("ESSMI", "swiss", INDICE_ESSMI_POS));
        aMap.put("EDJI", new StockIndice("EDJI", "dji US", INDICE_EDJI_POS));
        aMap.put("ENYA", new StockIndice("ENYA", "new york US", INDICE_ENYA_POS));
        aMap.put("ENDX", new StockIndice("ENDX", "nasdaq US", INDICE_ENDX_POS));
        aMap.put("EN225", new StockIndice("EN225", "nikkei japon", INDICE_EN225_POS));
        aMap.put("EHSI", new StockIndice("EHSI", "china", INDICE_EHSI_POS));
        aMap.put("EBSESN", new StockIndice("EBSESN", "india", INDICE_EBSESN_POS));
        aMap.put("ETA100", new StockIndice("ETA100", "orient", INDICE_ETA100_POS));

        IndiceMap = Collections.unmodifiableMap(aMap);
    }


    public static Map<String,StockIndice> getIndiceCache() {
        return IndiceMap;
    }


}
