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


    public static String urlPX1= "https://investir.lesechos.fr/cours/historique-indice-cac-40,xpar,px1,fr0003500008,isin.html";
    public static String urlPX4= "https://investir.lesechos.fr/cours/historique-indice-sbf-120,xpar,px4,fr0003999481,isin.html";

    public static String urlUKX= "https://investir.lesechos.fr/cours/historique-indice-ftse-100,xlon,gb0001383545,ukx,tick.html";
    public static String urlSMI= "https://investir.lesechos.fr/cours/historique-indice-smi,xswx,smi,ch0009980894,isin.html";
    public static String urlDJI= "https://investir.lesechos.fr/cours/historique-indice-dow-jones-ia,xnys,us2605661048,dji,tick.html";
    public static String urlNDX= "https://investir.lesechos.fr/cours/historique-indice-nasdaq-comp,xnas,xc0009694271,comp,tick.html";
    public static String urlNI225= "https://investir.lesechos.fr/cours/historique-indice-nikkei-225,windx,xc0009692440,in225,iso.html";


    public static String urlSENSEX= "https://fr.investing.com/indices/sensex-historical-data";
    public static String urlINX= "https://fr.investing.com/indices/us-spx-500-historical-data";
    public static String urlHSI= "https://fr.investing.com/indices/hang-sen-40-historical-data";

    private static final Map<String, StockIndice> IndiceMap;
    static {
        Map<String, StockIndice> aMap = new HashMap<>();
        aMap.put("PX1", new StockIndice("PX1", "cac 40 french","INDEXEURO", urlPX1,  INDICE_PX1_POS));
        aMap.put("PX4", new StockIndice("PX4", "sbf 120 french","INDEXEURO", urlPX4, INDICE_PX4_POS));
        aMap.put("UKX", new StockIndice("UKX", "ftse britain","INDEXFTSE", urlUKX, INDICE_FTSE_POS));
        //aMap.put("DAX", new StockIndice("DAX", "dax deutch","INDEXDB", INDICE_DAX_POS));
        aMap.put("SMI", new StockIndice("SMI", "swiss","INDEXSWX",urlSMI, INDICE_SMI_POS));
        aMap.put("DJI", new StockIndice("DJI", "dji US","INDEXDJX", urlDJI,INDICE_DJI_POS));
        aMap.put("INX", new StockIndice("INX", "S&P 500 US","INDEXSP", urlINX, INDICE_INX_POS));
        aMap.put("NDX", new StockIndice("NDX", "nasdaq US","INDEXNASDAQ",urlNDX, INDICE_NDX_POS));
        aMap.put("NI225", new StockIndice("NI225", "nikkei japon","INDEXNIKKEI", urlNI225, INDICE_NI225_POS));
        aMap.put("HSI", new StockIndice("HSI", "china","INDEXHANGSENG", urlSENSEX ,INDICE_HSI_POS));
        aMap.put("SENSEX", new StockIndice("SENSEX", "india","INDEXBOM", urlHSI ,INDICE_SENSEX_POS));


        IndiceMap = Collections.unmodifiableMap(aMap);
    }


    public static Map<String,StockIndice> getIndiceCache() {
        return IndiceMap;
    }


}
