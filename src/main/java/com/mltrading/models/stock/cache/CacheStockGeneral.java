package com.mltrading.models.stock.cache;

import com.mltrading.models.stock.StockGeneral;

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
        //old cac
        //aMap.put("FR0000130007", new StockGeneral("FR0000130007","Alcatel-Lucent","ALU","xpar","PA"));
        //aMap.put("FR0010242511", new StockGeneral("FR0010242511","EDF","EDF","xpar","PA"));

        aMap.put("FR0000120404", new StockGeneral("FR0000120404", "Accor","AC","xpar","PA","FRCS", "FRCS"));
        aMap.put("FR0000120073", new StockGeneral("FR0000120073","Air Liquide","AI","xpar","PA", "FRBM",  "FRBM"));
        aMap.put("NL0000235190", new StockGeneral("NL0000235190", "Airbus","AIR","xpar","PA", "FRIN" , "FRIN"));
        //aMap.put("FR0010220475", new StockGeneral("FR0010220475","Alstom","ALO","xpar","PA", "FRIN" ,  "FRIE"));
        aMap.put("lu1598757687", new StockGeneral("lu1598757687","ArcelorMittal-sa","MT","xams","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000051732", new StockGeneral("FR0000051732","Atos","ATO","xpar","PA", "FRTEC" ,  "FRSCS"));
        aMap.put("FR0000120628", new StockGeneral("FR0000120628","Axa","CS","xpar","PA",  "FRFIN" , "FRFIN"));
        aMap.put("FR0000131104", new StockGeneral("FR0000131104","Bnp Paribas","BNP","xpar","PA", "FRFIN" , "FRFIN"));
        aMap.put("FR0000120503", new StockGeneral("FR0000120503","Bouygues","EN","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000125338", new StockGeneral("FR0000125338","Capgemini","CAP","xpar","PA", "FRTEC" ,  "FRSCS"));
        aMap.put("FR0000120172", new StockGeneral("FR0000120172","Carrefour","CA","xpar","PA", "FRCS" ,  "FRFDR"));
        aMap.put("FR0000045072", new StockGeneral("FR0000045072","Credit Agricole","ACA","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000120644", new StockGeneral("FR0000120644","Danone","BN","xpar","PA", "FRCG" ,  "FRFPR"));
        aMap.put("FR0010208488", new StockGeneral("FR0010208488","Engie","ENGI","xpar","PA", "FRUT" ,  "FRGWM"));
        aMap.put("FR0000121667", new StockGeneral("FR0000121667","Essilor Intl","EI","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000121485", new StockGeneral("FR0000121485","Kering","KER","xpar","PA", "FRCS" ,  "FRGR"));
        //aMap.put("FR0000121964", new StockGeneral("FR0000121964","klepierre","LI","xpar","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0000120321", new StockGeneral("FR0000120321","Loreal","OREAL","xpar","PA", "FRCG" ,  "FRPG"));
        aMap.put("CH0012214059", new StockGeneral("CH0012214059","LafargeHolcim-ltd","LHN","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0010307819", new StockGeneral("FR0010307819","Legrand","LR","xpar","PA", "FRIN" ,  "FREEE"));
        aMap.put("FR0000121014", new StockGeneral("FR0000121014","Lvmh","MC","xpar","PA", "FRCG" , "FRPG"));
        aMap.put("FR0000121261", new StockGeneral("FR0000121261","Michelin","ML","xpar","PA", "FRCG" ,  "FRAP"));
        //aMap.put("FI0009000681", new StockGeneral("FI0009000681","Nokia","NOKIA","xpar","PA", "FRTEC" ,  "FRTHF"));
        aMap.put("FR0000133308", new StockGeneral("FR0000133308","Orange","ORA","xpar","PA", "FRTEL" ,  "FRTEL"));
        aMap.put("FR0000120693", new StockGeneral("FR0000120693","Pernod Ricard","RI","xpar","PA", "FRCG" ,  "FRBEV"));
        aMap.put("FR0000121501", new StockGeneral("FR0000121501","Peugeot","UG","xpar","PA", "FRCG" ,  "FRAP"));
        aMap.put("FR0000130577", new StockGeneral("FR0000130577","Publicis Groupe","PUB","xpar","PA", "FRCS" ,  "FRMED"));
        aMap.put("FR0000131906", new StockGeneral("FR0000131906","Renault","RNO","xpar","PA",  "FRCG" , "FRAP"));
        aMap.put("FR0000073272", new StockGeneral("FR0000073272","Safran","SAF","xpar","PA",  "FRIN" , "FRAD"));
        aMap.put("FR0000125007", new StockGeneral("FR0000125007","Saint Gobain","SGO","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000120578", new StockGeneral("FR0000120578","Sanofi","SAN","xpar","PA", "FRHC" ,  "FRPB"));
        aMap.put("FR0000121972", new StockGeneral("FR0000121972","Schneider Electric","SU","xpar","PA", "FRIN" ,  "FREEE"));
        aMap.put("FR0000130809", new StockGeneral("FR0000130809","Societe Generale","GLE","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("BE0003470755", new StockGeneral("BE0003470755","Solvay","SOLB","xbru","BR", "FRBM" ,  "FRBM"));
        aMap.put("FR0000121220", new StockGeneral("FR0000121220","Sodexo","SW","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("gb00bdsfg982", new StockGeneral("gb00bdsfg982","technipfmc","FTI","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0000120271", new StockGeneral("FR0000120271","Total","FP","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0000124711", new StockGeneral("FR0000124711","Unibail-Rodamco","UL","xams","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0013176526", new StockGeneral("FR0013176526","Valeo","FR","xpar","PA", "FRCG" ,  "FRAP")); // FR0000130338
        aMap.put("FR0000124141", new StockGeneral("FR0000124141","Veolia environnement","VIE","xpar","PA", "FRUT" ,  "FRGWM"));
        aMap.put("FR0000125486", new StockGeneral("FR0000125486","Vinci","DG","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000127771", new StockGeneral("FR0000127771","Vivendi","VIV","xpar","PA", "FRCS" ,  "FRMED"));


        ISINMap = Collections.unmodifiableMap(aMap);
    }


    static {
        Map<String, StockGeneral> aMap = new HashMap<>();
        aMap.put("FR0000120404", new StockGeneral("FR0000120404", "Accor","AC","xpar","PA","FRCS", "FRCS"));
        //https://investir.lesechos.fr/cours/action-aeroports-de-paris,xpar,adp,fr0010340141,isin.html
        //https://investir.lesechos.fr/cours/profil-societe-action-aeroports-de-paris,xpar,adp,fr0010340141,isin.html
        aMap.put("FR0010340141", new StockGeneral("FR0010340141", "AEROPORTS DE PARIS","ADP","xpar","PA","FRIN", "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-air-france-klm,xpar,af,fr0000031122,isin.html
        aMap.put("FR0000031122", new StockGeneral("FR0000031122", "AIR FRANCE-KLM","AF","xpar","PA","FRCS", "FRTL"));
        aMap.put("FR0000120073", new StockGeneral("FR0000120073","Air Liquide","AI","xpar","PA", "FRBM",  "FRBM"));
        aMap.put("NL0000235190", new StockGeneral("NL0000235190", "Airbus","AIR","xpar","PA", "FRIN" , "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-ald,xpar,ald,fr0013258662,isin.html
        aMap.put("FR0013258662", new StockGeneral("FR0013258662", "ALD","ALD","xpar","PA", "FRIN" , "FRIN"));
        aMap.put("FR0010220475", new StockGeneral("FR0010220475","Alstom","ALO","xpar","PA", "FRIN" ,  "FRIE"));
        //https://investir.lesechos.fr/cours/profil-societe-action-altarea,xpar,alta,fr0000033219,isin.html
        aMap.put("FR0000033219", new StockGeneral("FR0000033219","ALTAREA","ALTA","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-alten,xpar,ate,fr0000071946,isin.html
        aMap.put("FR0000071946", new StockGeneral("FR0000071946","ALTEN","ATE","xpar","PA", "FRTEC" ,  "FRTEC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-altran-technologies,xpar,alt,fr0000034639,isin.html
        aMap.put("FR0000034639", new StockGeneral("FR0000034639","ALTRAN","ALT","xpar","PA", "FRTEC" ,  "FRTEC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-amundi,xpar,amun,fr0004125920,isin.html
        aMap.put("FR0004125920", new StockGeneral("FR0004125920","AMUNDI","AMUN","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-arkema,xpar,ake,fr0010313833,isin.html
        aMap.put("FR0010313833", new StockGeneral("FR0010313833","ARKEMA","AKE","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000051732", new StockGeneral("FR0000051732","Atos","ATO","xpar","PA", "FRTEC" ,  "FRSCS"));
        aMap.put("FR0000120628", new StockGeneral("FR0000120628","Axa","CS","xpar","PA",  "FRFIN" , "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-beneteau,xpar,ben,fr0000035164,isin.html
        aMap.put("FR0000035164", new StockGeneral("FR0000035164","BENETEAU","BEN","xpar","PA",  "FRCG" , "FRCG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-bic,xpar,bb,fr0000120966,isin.html
        aMap.put("FR0000120966", new StockGeneral("FR0000120966","BIC","BB","xpar","PA",  "FRCG" , "FRCG"));
        //https://investir.lesechos.fr/cours/action-biomerieux,xpar,bim,fr0013280286,isin.html => 4
        aMap.put("FR0013280286", new StockGeneral("FR0013280286","BIOMERIEUX","BIM","xpar","PA",  "FRHC" , "FRHC"));




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

    public static String getPlace(String codif) {
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            if (g.getCodif().equals(codif)) return g.getPlace();
        }
        return null;
    }


    public static String getSector(String codif) {
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            if (g.getCodif().equals(codif)) return g.getSector();
        }
        return null;
    }

    public static void removeCache() {
        CacheStockGeneralHolder.instance.CSCache.clear();
    }

}
