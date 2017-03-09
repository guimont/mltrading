package com.mltrading.models.stock.cache;

import com.mltrading.models.stock.StockIndice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 18/12/2015.
 */
public class CacheStockIndice {

    public static int INDICE_PX1_POS  = 0;
    public static int INDICE_PX4_POS  = 1;
    public static int INDICE_FTSE_POS  = 2;
    //public static int INDICE_DAX_POS  = 3; not enough indice
    //public static int INDICE_ESTOXX50E_POS  = 3;
    public static int INDICE_SMI_POS   = 3;
    public static int INDICE_DJI_POS  = 4;
    public static int INDICE_INX_POS   = 5;
    public static int INDICE_NDX_POS  = 6;
    public static int INDICE_NI225_POS  = 7;
    public static int INDICE_HSI_POS  = 8;
    public static int INDICE_SENSEX_POS  = 9;

    public static int N_INDICE  = 10;


    //INDEXDB:DAX  DAX
    //INDEXEURO:PX1 CAC 40
    //INDEXEURO:PX4 SBF120
    //INDEXFTSE:UKX FTSEdow
    //INDEXSWX:SMI SMI
    //INDEXDJX:DJI
    //INDEXNASDAQ:NDX nasdaq
    //INDEXSP:INX SP500
    //INDEXNIKKEI:NI225 nikkeiBS
    //INDEXHANGSENG:HSI
    //INDEXBOM:SENSEX sensex inde
    //INDEXFTSE:XIN9 china A50

    private static final Map<String, StockIndice> IndiceMap;
    static {
        Map<String, StockIndice> aMap = new HashMap<>();
        aMap.put("PX1", new StockIndice("PX1", "cac 40 french","INDEXEURO",  INDICE_PX1_POS));
        aMap.put("PX4", new StockIndice("PX4", "sbf 120 french","INDEXEURO",  INDICE_PX4_POS));
        aMap.put("UKX", new StockIndice("UKX", "ftse britain","INDEXFTSE", INDICE_FTSE_POS));
        //aMap.put("DAX", new StockIndice("DAX", "dax deutch","INDEXDB", INDICE_DAX_POS));
        aMap.put("SMI", new StockIndice("SMI", "swiss","INDEXSWX", INDICE_SMI_POS));
        aMap.put("DJI", new StockIndice("DJI", "dji US","INDEXDJX", INDICE_DJI_POS));
        aMap.put("INX", new StockIndice("INX", "S&P 500 US","INDEXSP", INDICE_INX_POS));
        aMap.put("NDX", new StockIndice("NDX", "nasdaq US","INDEXNASDAQ", INDICE_NDX_POS));
        aMap.put("NI225", new StockIndice("NI225", "nikkei japon","INDEXNIKKEI", INDICE_NI225_POS));
        aMap.put("HSI", new StockIndice("HSI", "china","INDEXHANGSENG", INDICE_HSI_POS));
        aMap.put("SENSEX", new StockIndice("SENSEX", "india","INDEXBOM", INDICE_SENSEX_POS));


        IndiceMap = Collections.unmodifiableMap(aMap);
    }


    public static Map<String,StockIndice> getIndiceCache() {
        return IndiceMap;
    }


}
