package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gmo on 17/06/2015.
 */






public class CacheStockGeneral {

    private static final Map<String, StockGeneral> ISINMap;
    static {
        Map<String, StockGeneral> aMap = new HashMap<>();
        aMap.put("BE0003470755", new StockGeneral("BE0003470755","Solvay","SOLB","","BR"));
        aMap.put("CH0012214059", new StockGeneral("CH0012214059","LafargeHolcim","LG","","PA"));
        aMap.put("FR0000045072", new StockGeneral("FR0000045072","Credit Agricole","ACA","","PA"));
        aMap.put("FR0000073272", new StockGeneral("FR0000073272","Safran","SAF","","PA"));
        aMap.put("FR0000120073", new StockGeneral("FR0000120073","Air Liquide","AI","","PA"));
        aMap.put("FR0000120172", new StockGeneral("FR0000120172","Carrefour","CA","","PA"));
        aMap.put("FR0000120271", new StockGeneral("FR0000120271","Total","FP","","PA"));
        aMap.put("FR0000120321", new StockGeneral("FR0000120321","L'oreal","OR","","PA"));
        aMap.put("FR0000120404", new StockGeneral("FR0000120404", "Accor Hotels","AC","","PA"));
        aMap.put("FR0000120503", new StockGeneral("FR0000120503","Bouygues","EN","","PA"));
        aMap.put("FR0000120578", new StockGeneral("FR0000120578","Sanofi","SAN","","PA"));
        aMap.put("FR0000120628", new StockGeneral("FR0000120628","Axa","CS","","PA"));
        aMap.put("FR0000120644", new StockGeneral("FR0000120644","Danone","BN","","PA"));
        aMap.put("FR0000120693", new StockGeneral("FR0000120693","Pernod Ricard","RI","","PA"));
        aMap.put("FR0000121014", new StockGeneral("FR0000121014","Lvmh","MC","","PA"));
        aMap.put("FR0000121261", new StockGeneral("FR0000121261","Michelin","ML","","PA"));
        aMap.put("FR0000121485", new StockGeneral("FR0000121485","Kering","KER","","PA"));
        aMap.put("FR0000121501", new StockGeneral("FR0000121501","Peugeot","UG","","PA"));
        aMap.put("FR0000121667", new StockGeneral("FR0000121667","Essilor Intl","EI","","PA"));
        aMap.put("FR0000121972", new StockGeneral("FR0000121972","Schneider Electric","SU","","PA"));
        aMap.put("FR0000124141", new StockGeneral("FR0000124141","Veolia Environ.","VIE","","PA"));
        aMap.put("FR0000124711", new StockGeneral("FR0000124711","Unibail-Rodamco","UL","","PA"));
        aMap.put("FR0000125007", new StockGeneral("FR0000125007","Saint Gobain","SGO","","PA"));
        aMap.put("FR0000125338", new StockGeneral("FR0000125338","Cap Gemini","CAP","","PA"));
        aMap.put("FR0000125486", new StockGeneral("FR0000125486","Vinci","DG","","PA"));
        aMap.put("FR0000127771", new StockGeneral("FR0000127771","Vivendi","VIV","","PA"));
        aMap.put("FR0000130007", new StockGeneral("FR0000130007","Alcatel-Lucent","ALU","","PA"));
        aMap.put("FR0000130338", new StockGeneral("FR0000130338","Valeo","FR","","PA"));
        aMap.put("FR0000130577", new StockGeneral("FR0000130577","Publicis Groupe","PUB","","PA"));
        aMap.put("FR0000130809", new StockGeneral("FR0000130809","Societe Generale","GLE","","PA"));
        aMap.put("FR0000131104", new StockGeneral("FR0000131104","Bnp Paribas","BNP","","PA"));
        aMap.put("FR0000131708", new StockGeneral("FR0000131708","Technip","TEC","","PA"));
        aMap.put("FR0000131906", new StockGeneral("FR0000131906","Renault","RNO","","PA"));
        aMap.put("FR0000133308", new StockGeneral("FR0000133308","Orange","ORA","","PA"));
        aMap.put("FR0010208488", new StockGeneral("FR0010208488","Engie","GSZ","","PA"));
        aMap.put("FR0010220475", new StockGeneral("FR0010220475","Alstom","ALO","","PA"));
        aMap.put("FR0010242511", new StockGeneral("FR0010242511","EDF","EDF","","PA"));
        aMap.put("FR0010307819", new StockGeneral("FR0010307819","Legrand SA","LR","","PA"));
        aMap.put("LU0323134006", new StockGeneral("LU0323134006","Arcelor Mittal","MT","","PA"));
        aMap.put("NL0000235190", new StockGeneral("NL0000235190", "Airbus","AIR","","PA"));

        ISINMap = Collections.unmodifiableMap(aMap);
    }



    private Map<String,StockGeneral> CSCache = new TreeMap<>();






    private CacheStockGeneral() {
    }

    private static class CacheStockGeneralHolder {
        /** Instance unique non pre-initialise */
        private final static CacheStockGeneral instance = new CacheStockGeneral();
    }


    public static Map<String,StockGeneral> getCache() {
        return CacheStockGeneralHolder.instance.CSCache;
    }

    public static Map<String,StockGeneral> getIsinCache() {
        return ISINMap;
    }

    public static String getCode(String codif) {
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            if (g.getCodif().equals(codif)) return g.getCode();
        }
        return null;
    }

    public static void removeCache() {
        CacheStockGeneralHolder.instance.CSCache.clear();
    }

}
