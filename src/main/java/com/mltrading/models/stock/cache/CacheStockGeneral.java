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
    private static final Map<String, StockGeneral> ISINExMap;
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
        aMap.put("FR0000130650", new StockGeneral("FR0000130650","DASSAULT SYSTEMES","DSY","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0010208488", new StockGeneral("FR0010208488","Engie","ENGI","xpar","PA", "FRUT" ,  "FRGWM"));
        //changement de nom ... super
        //aMap.put("FR0000121667", new StockGeneral("FR0000121667","Essilor Intl","EI","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000121667", new StockGeneral("FR0000121667","Essilorluxottica","EL","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000052292", new StockGeneral("FR0000052292","HERMES INTL","RMS","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000121485", new StockGeneral("FR0000121485","Kering","KER","xpar","PA", "FRCS" ,  "FRGR"));
        //aMap.put("FR0000121964", new StockGeneral("FR0000121964","klepierre","LI","xpar","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0000120321", new StockGeneral("FR0000120321","Loreal","OREAL","xpar","PA", "FRCG" ,  "FRPG"));
        //aMap.put("CH0012214059", new StockGeneral("CH0012214059","LafargeHolcim-ltd","LHN","xpar","PA", "FRIN" ,  "FRCM"));
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
        //aMap.put("BE0003470755", new StockGeneral("BE0003470755","Solvay","SOLB","xbru","BR", "FRBM" ,  "FRBM"));
        aMap.put("FR0000121220", new StockGeneral("FR0000121220","Sodexo","SW","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("NL0000226223", new StockGeneral("NL0000226223","stmicroelectronics","STM","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("gb00bdsfg982", new StockGeneral("gb00bdsfg982","technipfmc","FTI","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0000120271", new StockGeneral("FR0000120271","Total","FP","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0013326246", new StockGeneral("FR0013326246","Unibail-Rodamco-we","URW","xams","PA", "FRFIN" ,  "FRRE"));
        //aMap.put("FR0000124711", new StockGeneral("FR0000124711","Unibail-Rodamco","UL","xams","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0013176526", new StockGeneral("FR0013176526","Valeo","FR","xpar","PA", "FRCG" ,  "FRAP")); // FR0000130338
        aMap.put("FR0000124141", new StockGeneral("FR0000124141","Veolia environnement","VIE","xpar","PA", "FRUT" ,  "FRGWM"));
        aMap.put("FR0000125486", new StockGeneral("FR0000125486","Vinci","DG","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000127771", new StockGeneral("FR0000127771","Vivendi","VIV","xpar","PA", "FRCS" ,  "FRMED"));


        ISINMap = Collections.unmodifiableMap(aMap);
    }


    static {
        Map<String, StockGeneral> aMap = new HashMap<>();

        aMap.put("FR0010340141", new StockGeneral("FR0010340141", "AEROPORTS DE PARIS","ADP","xpar","PA","FRIN", "FRIN"));
        aMap.put("FR0000031122", new StockGeneral("FR0000031122", "AIR FRANCE-KLM","AF","xpar","PA","FRCS", "FRTL"));
        aMap.put("FR0013258662", new StockGeneral("FR0013258662", "ALD","ALD","xpar","PA", "FRIN" , "FRIN"));
        aMap.put("FR0010220475", new StockGeneral("FR0010220475","Alstom","ALO","xpar","PA", "FRIN" ,  "FRIE"));
        aMap.put("FR0000071946", new StockGeneral("FR0000071946","ALTEN","ATE","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000034639", new StockGeneral("FR0000034639","ALTRAN","ALT","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0004125920", new StockGeneral("FR0004125920","AMUNDI","AMUN","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0010313833", new StockGeneral("FR0010313833","ARKEMA","AKE","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000120966", new StockGeneral("FR0000120966","BIC","BB","xpar","PA",  "FRCG" , "FRCG"));
        aMap.put("FR0013280286", new StockGeneral("FR0013280286","BIOMERIEUX","BIM","xpar","PA",  "FRHC" , "FRHC"));
        aMap.put("FR0000039299", new StockGeneral("FR0000039299","BOLLORE","BOL","xpar","PA", "FRIN" , "FRIN"));
        aMap.put("FR0006174348", new StockGeneral("FR0006174348","BUREAU VERITAS","BVI","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000125585", new StockGeneral("FR0000125585","CASINO GUICHARD-PERRACHON","CO","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0013181864", new StockGeneral("FR0013181864","CGG","CGG","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0000120222", new StockGeneral("FR0000120222","CNP ASSURANCES","CNP","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("LU0569974404", new StockGeneral("LU0569974404","APERAM","APAM","xams","PA", "FRBM" ,  "FRBM"));
        aMap.put("BE0003470755", new StockGeneral("BE0003470755","Solvay","SOLB","xbru","BR", "FRBM" ,  "FRBM"));
        aMap.put("FR0000064578", new StockGeneral("FR0000064578", "COVIVIO","COV","xpar","PA","FRFIN", "FRFIN"));
        aMap.put("FR0000121725", new StockGeneral("FR0000121725","DASSAULT AVIATION","AM","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010417345", new StockGeneral("FR0010417345","DBV TECHNOLOGIES","DBV","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0010908533", new StockGeneral("FR0010908533","EDENRED ","EDEN","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010242511", new StockGeneral("FR0010242511","EDF","EDF","xpar","PA", "FRUT" ,  "FRUT"));
        aMap.put("FR0000130452", new StockGeneral("FR0000130452","EIFFAGE","FGR","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0011950732", new StockGeneral("FR0011950732","ELIOR","ELIOR","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0012435121", new StockGeneral("FR0012435121","ELIS","ELIS","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000131757", new StockGeneral("FR0000131757","ERAMET ","ERA","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000121121", new StockGeneral("FR0000121121","EURAZEO","RF","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000038259", new StockGeneral("FR0000038259","EUROFINS SCIENTIFIC","ERF","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("NL0006294274", new StockGeneral("NL0006294274","EURONEXT","ENX","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0012789949", new StockGeneral("FR0012789949","EUROPCAR","EUCAR","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0010221234", new StockGeneral("FR0010221234","EUTELSAT COMMUNICATIONS","ETL","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000121147", new StockGeneral("FR0000121147","FAURECIA","EO","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0011476928", new StockGeneral("FR0011476928","FNAC DARTY","FNAC","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0011726835", new StockGeneral("FR0011726835","GTT - GAZTRANSPORT ET TECHNIGAZ","GTT","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010040865", new StockGeneral("FR0010040865","GECINA ","GFC","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0004163111", new StockGeneral("FR0004163111","GENFIT","GNFT","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0010533075", new StockGeneral("FR0010533075","getlink EUROTUNNEL","GET","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000035081", new StockGeneral("FR0000035081","ICADE","ICAD","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0004035913", new StockGeneral("FR0004035913","ILIAD","ILD","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000120859", new StockGeneral("FR0000120859","IMERYS ","NK","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000125346", new StockGeneral("FR0000125346","INGENICO ","ING","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0010259150", new StockGeneral("FR0010259150","IPSEN ","IPN","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000073298", new StockGeneral("FR0000073298","IPSOS ","IPS","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000077919", new StockGeneral("FR0000077919","JCDECAUX  ","DEC","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000121964", new StockGeneral("FR0000121964","klepierre","LI","xpar","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0010386334", new StockGeneral("FR0010386334","KORIAN  ","KORI","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000130213", new StockGeneral("FR0000130213","LAGARDERE","MMB","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0013153541", new StockGeneral("FR0013153541","MAISONS DU MONDE","MDM","xpar","PA", "FRCG" , "FRCG"));
        aMap.put("FR0010241638", new StockGeneral("FR0010241638","MERCIALYS ","MERY","xpar","PA", "FRFIN" , "FRFIN"));
        aMap.put("FR0000053225", new StockGeneral("FR0000053225","METROPOLE TV ","MMT","xpar","PA", "FRCS" , "FRCS"));
        aMap.put("FR0000120685", new StockGeneral("FR0000120685","NATIXIS","KN","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000120560", new StockGeneral("FR0000120560","NEOPOST","NEO","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000044448", new StockGeneral("FR0000044448","NEXANS","NEX","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010112524", new StockGeneral("FR0010112524","NEXITY","NXI","xpar","PA", "FRFIN" ,  "FRFIN"));

        aMap.put("FR0000184798", new StockGeneral("FR0000184798","ORPEA","ORP","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000124570", new StockGeneral("FR0000124570","PLASTIC OMNIUM","POM","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000130395", new StockGeneral("FR0000130395","REMY COINTREAU","RCO","xpar","PA", "FRCS" ,  "FRMED"));
        aMap.put("FR0010451203", new StockGeneral("FR0010451203","REXEL","RXL","xpar","PA",  "FRIN" , "FRIN"));
        aMap.put("FR0000031684", new StockGeneral("FR0000031684","ROTHSCHILD & CO","ROTH","xpar","PA",  "FRFIN" , "FRFIN"));
        aMap.put("FR0013269123", new StockGeneral("FR0013269123","RUBIS","RUI","xpar","PA",  "FRUT" , "FRUT"));
        aMap.put("FR0013154002", new StockGeneral("FR0013154002","SARTORIUS STED BIO","DIM","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000121709", new StockGeneral("FR0000121709","SEB","SK","xpar","PA",  "FRCG" , "FRCG"));
        aMap.put("FR0010411983", new StockGeneral("FR0010411983","SCOR SE","SCR","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("LU0088087324", new StockGeneral("LU0088087324","SES","SESG","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0013227113", new StockGeneral("FR0013227113","SOITEC","SOI","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000050809", new StockGeneral("FR0000050809","SOPRA STERIA GROUP","SOP","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0012757854", new StockGeneral("FR0012757854","SPIE","SPIE","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010613471", new StockGeneral("FR0010613471","SUEZ","SEV","xpar","PA", "FRUT" ,  "FRUT"));
        aMap.put("FR0004188670", new StockGeneral("FR0004188670","TARKETT","TKTT","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010918292", new StockGeneral("FR0010918292","TECHNICOLOR","TCH","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000051807", new StockGeneral("FR0000051807","TELEPERFORMANCE","TEP","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000054900", new StockGeneral("FR0000054900","TF1","TFI","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000121329", new StockGeneral("FR0000121329","THALES","HO","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0005691656", new StockGeneral("FR0005691656","TRIGANO","TRI","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000054470", new StockGeneral("FR0000054470","UBISOFT ENTERTAINMENT","UBI","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000120354", new StockGeneral("FR0000120354","VALLOUREC","VK","xpar","PA", "FRIN" ,  "FRIN")); // FR0000130338
        aMap.put("FR0000031775", new StockGeneral("FR0000031775","VICAT","VCT","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000121204", new StockGeneral("FR0000121204","WENDEL","MF","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0011981968", new StockGeneral("FR0011981968","WORLDLINE","WLN","xpar","PA", "FRIN" ,  "FRIN"));

/*

        //https://investir.lesechos.fr/cours/profil-societe-action-schlumberger,xpar,slb,an8068571086,isin.html
        aMap.put("AN8068571086", new StockGeneral("AN8068571086","SCHLUMBERGER","SLB","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0000121972", new StockGeneral("FR0000121972","Schneider Electric","SU","xpar","PA", "FRIN" ,  "FREEE"));
        //https://investir.lesechos.fr/cours/profil-societe-action-scor-se,xpar,scr,fr0010411983,isin.html

        //https://investir.lesechos.fr/cours/profil-societe-action-ses,xpar,sesg,lu0088087324,isin.html
        //https://investir.lesechos.fr/cours/profil-societe-action-smcp,xpar,smcp,fr0013214145,isin.html

        aMap.put("FR0013214145", new StockGeneral("FR0013214145","SMCP","SMCP","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0013199916", new StockGeneral("FR0013199916","SOMFY SA","SO","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-sopra-steria-group,xpar,sop,fr0000050809,isin.html
*/
        ISINExMap = Collections.unmodifiableMap(aMap);

    }



    private Map<String,StockGeneral> CSCache = new TreeMap<>();



    private CacheStockGeneral() {
    }


    private static class CacheStockGeneralHolder {
        /** Instance unique non pre-initialise */
        private final static CacheStockGeneral instance = new CacheStockGeneral();
        private final static CacheStockGeneral instanceEx = new CacheStockGeneral();
    }


    public static Map<String,StockGeneral> getCache() {
        return CacheStockGeneralHolder.instance.CSCache;
    }

    public static Map<String,StockGeneral> getCacheEx() {
        return CacheStockGeneralHolder.instanceEx.CSCache;
    }

    public static Map<String,StockGeneral> getIsinCache() {
        return ISINMap;
    }

    public static Map<String,StockGeneral> getIsinExCache() {
        return ISINExMap;
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


    public static String getCodeEx(String codif) {
        for (StockGeneral g: CacheStockGeneral.getIsinExCache().values()) {
            if (g.getCodif().equals(codif)) return g.getCode();
        }
        return null;
    }

    public static String getPlaceEx(String codif) {
        for (StockGeneral g: CacheStockGeneral.getIsinExCache().values()) {
            if (g.getCodif().equals(codif)) return g.getPlace();
        }
        return null;
    }


    public static String getSectorEx(String codif) {
        for (StockGeneral g: CacheStockGeneral.getIsinExCache().values()) {
            if (g.getCodif().equals(codif)) return g.getSector();
        }
        return null;
    }


    public static void removeCache() {
        CacheStockGeneralHolder.instance.CSCache.clear();
    }

}
