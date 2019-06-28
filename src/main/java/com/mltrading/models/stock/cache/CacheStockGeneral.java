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

        aMap.put("FR0000120404", new StockGeneral("FR0000120404", "ACCORHOTELS","ACCORHOTELS","AC","xpar","PA","FRCS", "FRCS"));
        aMap.put("FR0000120073", new StockGeneral("FR0000120073","AIR LIQUIDE","AIR LIQUIDE","AI","xpar","PA", "FRBM",  "FRBM"));
        aMap.put("NL0000235190", new StockGeneral("NL0000235190", "AIRBUS","AIRBUS GROUP (EX-EADS)","AIR","xpar","PA", "FRIN" , "FRIN"));
        aMap.put("lu1598757687", new StockGeneral("lu1598757687","ARCELORMITTAL","ARCELORMITTAL","MT","xams","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000051732", new StockGeneral("FR0000051732","ATOS","ATOS","ATO","xpar","PA", "FRTEC" ,  "FRSCS"));
        aMap.put("FR0000120628", new StockGeneral("FR0000120628","AXA","AXA","CS","xpar","PA",  "FRFIN" , "FRFIN"));
        aMap.put("FR0000131104", new StockGeneral("FR0000131104","BNP PARIBAS","BNP PARIBAS","BNP","xpar","PA", "FRFIN" , "FRFIN"));
        aMap.put("FR0000120503", new StockGeneral("FR0000120503","BOUYGUES","BOUYGUES","EN","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000125338", new StockGeneral("FR0000125338","CAPGEMINI","CAPGEMINI","CAP","xpar","PA", "FRTEC" ,  "FRSCS"));
        aMap.put("FR0000120172", new StockGeneral("FR0000120172","CARREFOUR","CARREFOUR","CA","xpar","PA", "FRCS" ,  "FRFDR"));
        aMap.put("FR0000045072", new StockGeneral("FR0000045072","CREDIT AGRICOLE","CREDIT AGRICOLE","ACA","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000120644", new StockGeneral("FR0000120644","DANONE","DANONE","BN","xpar","PA", "FRCG" ,  "FRFPR"));
        aMap.put("FR0000130650", new StockGeneral("FR0000130650","DASSAULT SYSTEMES","DASSAULT SYSTEMES","DSY","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0010208488", new StockGeneral("FR0010208488","ENGIE","ENGIE (EX GDF SUEZ)","ENGI","xpar","PA", "FRUT" ,  "FRGWM"));
        aMap.put("FR0000121667", new StockGeneral("FR0000121667","ESSILORLUXOTTICA","ESSILORLUXOTTICA","EL","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000052292", new StockGeneral("FR0000052292","HERMES INTERNATIONAL","HERMES INTERNATIONAL","RMS","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000121485", new StockGeneral("FR0000121485","KERING","KERING (EX-PPR)","KER","xpar","PA", "FRCS" ,  "FRGR"));
        aMap.put("FR0000120321", new StockGeneral("FR0000120321","LOREAL","L'OREAL","OREAL","xpar","PA", "FRCG" ,  "FRPG"));
        aMap.put("FR0010307819", new StockGeneral("FR0010307819","LEGRAND","LEGRAND","LR","xpar","PA", "FRIN" ,  "FREEE"));
        aMap.put("FR0000121014", new StockGeneral("FR0000121014","LVMH","LVMH","MC","xpar","PA", "FRCG" , "FRPG"));
        aMap.put("FR0000121261", new StockGeneral("FR0000121261","MICHELIN","MICHELIN","ML","xpar","PA", "FRCG" ,  "FRAP"));
        aMap.put("FR0000133308", new StockGeneral("FR0000133308","ORANGE","ORANGE","ORA","xpar","PA", "FRTEL" ,  "FRTEL"));
        aMap.put("FR0000120693", new StockGeneral("FR0000120693","PERNOD RICARD","PERNOD RICARD","RI","xpar","PA", "FRCG" ,  "FRBEV"));
        aMap.put("FR0000121501", new StockGeneral("FR0000121501","PEUGEOT","PSA GROUPE (EX-PEUGEOT)","UG","xpar","PA", "FRCG" ,  "FRAP"));
        aMap.put("FR0000130577", new StockGeneral("FR0000130577","PUBLICIS","PUBLICIS","PUB","xpar","PA", "FRCS" ,  "FRMED"));
        aMap.put("FR0000131906", new StockGeneral("FR0000131906","RENAULT","RENAULT","RNO","xpar","PA",  "FRCG" , "FRAP"));
        aMap.put("FR0000073272", new StockGeneral("FR0000073272","SAFRAN","SAFRAN","SAF","xpar","PA",  "FRIN" , "FRAD"));
        aMap.put("FR0000125007", new StockGeneral("FR0000125007","SAINT-GOBAIN","SAINT-GOBAIN","SGO","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000120578", new StockGeneral("FR0000120578","SANOFI","SANOFI","SAN","xpar","PA", "FRHC" ,  "FRPB"));
        aMap.put("FR0000121972", new StockGeneral("FR0000121972","SCHNEIDER ELECTRIC","SCHNEIDER ELECTRIC","SU","xpar","PA", "FRIN" ,  "FREEE"));
        aMap.put("FR0000130809", new StockGeneral("FR0000130809","SOCIETE GENERALE","SOCIETE GENERALE","GLE","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000121220", new StockGeneral("FR0000121220","SODEXO","SODEXO","SW","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("NL0000226223", new StockGeneral("NL0000226223","STMICROELECTRONICS","STMICROELECTRONICS","STM","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("gb00bdsfg982", new StockGeneral("gb00bdsfg982","TECHNIPFMC","TECHNIPFMC","FTI","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0000120271", new StockGeneral("FR0000120271","TOTAL","TOTAL","FP","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0013326246", new StockGeneral("FR0013326246","UNIBAIL-RODAMCO-WE","UNIBAIL-WFD","URW","xams","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0013176526", new StockGeneral("FR0013176526","VALEO","VALEO","FR","xpar","PA", "FRCG" ,  "FRAP")); // FR0000130338
        aMap.put("FR0000124141", new StockGeneral("FR0000124141","VEOLIA ENVIRONNEMENT","VEOLIA ENVIRONNEMENT","VIE","xpar","PA", "FRUT" ,  "FRGWM"));
        aMap.put("FR0000125486", new StockGeneral("FR0000125486","VINCI","VINCI","DG","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000127771", new StockGeneral("FR0000127771","VIVENDI","VIVENDI","VIV","xpar","PA", "FRCS" ,  "FRMED"));


        ISINMap = Collections.unmodifiableMap(aMap);
    }


    static {
        Map<String, StockGeneral> aMap = new HashMap<>();

        aMap.put("FR0010340141", new StockGeneral("FR0010340141", "ADP","GROUPE ADP (EX-AEROPORTS DE PARIS)","ADP","xpar","PA","FRIN", "FRIN"));
        aMap.put("FR0000031122", new StockGeneral("FR0000031122", "AIR FRANCE KLM","AIR FRANCE KLM","AF","xpar","PA","FRCS", "FRTL"));
        aMap.put("FR0013258662", new StockGeneral("FR0013258662", "ALD","ALD","ALD","xpar","PA", "FRIN" , "FRIN"));
        aMap.put("FR0010220475", new StockGeneral("FR0010220475","ALSTOM","ALSTOM","ALO","xpar","PA", "FRIN" ,  "FRIE"));
        aMap.put("FR0000071946", new StockGeneral("FR0000071946","ALTEN","ALTEN","ATE","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000034639", new StockGeneral("FR0000034639","ALTRAN-TECHNOLOGIES","ALTRAN TECHNOLOGIES","ALT","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0004125920", new StockGeneral("FR0004125920","AMUNDI","AMUNDI","AMUN","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0010313833", new StockGeneral("FR0010313833","ARKEMA","ARKEMA","AKE","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000120966", new StockGeneral("FR0000120966","BIC","BIC","BB","xpar","PA",  "FRCG" , "FRCG"));
        aMap.put("FR0013280286", new StockGeneral("FR0013280286","BIOMERIEUX","BIOMERIEUX","BIM","xpar","PA",  "FRHC" , "FRHC"));
        aMap.put("FR0000039299", new StockGeneral("FR0000039299","BOLLORE","BOLLORE","BOL","xpar","PA", "FRIN" , "FRIN"));
        aMap.put("FR0006174348", new StockGeneral("FR0006174348","BUREAU-VERITAS","BUREAU VERITAS","BVI","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000125585", new StockGeneral("FR0000125585","CASINO-GUICHARD","CASINO GUICHARD PERRACHON","CO","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0013181864", new StockGeneral("FR0013181864","CGG","CGG (EX-CGGVERITAS)","CGG","xpar","PA", "FROG" ,  "FROG"));
        https://investir.lesechos.fr/cours/profil-societe-action-coface,xpar,cofa,fr0010667147,isin.html
        aMap.put("FR0010667147", new StockGeneral("FR0010667147","COFACE","COFACE","COFA","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000120222", new StockGeneral("FR0000120222","CNP-ASSURANCES","CNP ASSURANCES","CNP","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("LU0569974404", new StockGeneral("LU0569974404","APERAM","APERAM","APAM","xams","PA", "FRBM" ,  "FRBM"));
        aMap.put("BE0003470755", new StockGeneral("BE0003470755","SOLVAY","SOLVAY","SOLB","xbru","BR", "FRBM" ,  "FRBM"));
        aMap.put("FR0000064578", new StockGeneral("FR0000064578", "COVIVIO","COVIVIO (EX FONCIÈRE DES REGIONS)","COV","xpar","PA","FRFIN", "FRFIN"));
        aMap.put("FR0000121725", new StockGeneral("FR0000121725","DASSAULT AVIATION","DASSAULT AVIATION","AM","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010417345", new StockGeneral("FR0010417345","DBV-TECHNOLOGIES","DBV TECHNOLOGIES","DBV","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0010908533", new StockGeneral("FR0010908533","EDENRED","EDENRED","EDEN","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010242511", new StockGeneral("FR0010242511","EDF","EDF","EDF","xpar","PA", "FRUT" ,  "FRUT"));
        aMap.put("FR0000130452", new StockGeneral("FR0000130452","EIFFAGE","EIFFAGE","FGR","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0011950732", new StockGeneral("FR0011950732","ELIOR","ELIOR","ELIOR","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0012435121", new StockGeneral("FR0012435121","ELIS","ELIS","ELIS","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000131757", new StockGeneral("FR0000131757","ERAMET","ERAMET","ERA","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000121121", new StockGeneral("FR0000121121","EURAZEO","EURAZEO","RF","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000038259", new StockGeneral("FR0000038259","EUROFINS-SCIENTIFIC","EUROFINS SCIENTIFIC","ERF","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("NL0006294274", new StockGeneral("NL0006294274","EURONEXT","EURONEXT","ENX","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0012789949", new StockGeneral("FR0012789949","EUROPCAR","EUROPCAR","EUCAR","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0010221234", new StockGeneral("FR0010221234","EUTELSAT","EUTELSAT","ETL","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000121147", new StockGeneral("FR0000121147","FAURECIA","FAURECIA","EO","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0011476928", new StockGeneral("FR0011476928","FNAC DARTY","FNAC DARTY","FNAC","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0011726835", new StockGeneral("FR0011726835","GTT","GTT","GTT","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010040865", new StockGeneral("FR0010040865","GECINA","GECINA","GFC","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0004163111", new StockGeneral("FR0004163111","GENFIT","GENFIT","GNFT","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0010533075", new StockGeneral("FR0010533075","GETLINK SE","GETLINK (EX EUROTUNNEL)","GET","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000035081", new StockGeneral("FR0000035081","ICADE","ICADE","ICAD","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0004035913", new StockGeneral("FR0004035913","ILIAD","ILIAD","ILD","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000120859", new StockGeneral("FR0000120859","IMERYS","IMERYS","NK","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000125346", new StockGeneral("FR0000125346","INGENICO","INGENICO","ING","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0010259150", new StockGeneral("FR0010259150","IPSEN","IPSEN","IPN","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000073298", new StockGeneral("FR0000073298","IPSOS","IPSOS","IPS","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000077919", new StockGeneral("FR0000077919","JCDECAUX","JCDECAUX","DEC","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000121964", new StockGeneral("FR0000121964","KLEPIERRE","KLEPIERRE","LI","xpar","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0010386334", new StockGeneral("FR0010386334","KORIAN","KORIAN","KORI","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000130213", new StockGeneral("FR0000130213","LAGARDERE","LAGARDERE","MMB","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0013153541", new StockGeneral("FR0013153541","MAISONS DU MONDE","MAISONS DU MONDE","MDM","xpar","PA", "FRCG" , "FRCG"));
        aMap.put("FR0010241638", new StockGeneral("FR0010241638","MERCIALYS","MERCIALYS","MERY","xpar","PA", "FRFIN" , "FRFIN"));
        aMap.put("FR0000053225", new StockGeneral("FR0000053225","M6 METROPOLE TELEVISION","M6 METROPOLE TELEVISION","MMT","xpar","PA", "FRCS" , "FRCS"));
        aMap.put("FR0000120685", new StockGeneral("FR0000120685","NATIXIS","NATIXIS","KN","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000120560", new StockGeneral("FR0000120560","NEOPOST","NEOPOST","NEO","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000044448", new StockGeneral("FR0000044448","NEXANS","NEXANS","NEX","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010112524", new StockGeneral("FR0010112524","NEXITY", "NEXITY","NXI","xpar","PA", "FRFIN" ,  "FRFIN"));

        aMap.put("FR0000184798", new StockGeneral("FR0000184798","ORPEA","ORPEA","ORP","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000124570", new StockGeneral("FR0000124570","PLASTIC OMNIUM","PLASTIC OMNIUM","POM","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000130395", new StockGeneral("FR0000130395","REMY COINTREAU","REMY COINTREAU","RCO","xpar","PA", "FRCS" ,  "FRMED"));
        aMap.put("FR0010451203", new StockGeneral("FR0010451203","REXEL","REXEL","RXL","xpar","PA",  "FRIN" , "FRIN"));
        aMap.put("FR0000031684", new StockGeneral("FR0000031684","ROTHSCHILD & CO", "ROTHSCHILD ET CIE (EX-PARIS ORLÉANS)","ROTH","xpar","PA",  "FRFIN" , "FRFIN"));
        aMap.put("FR0013269123", new StockGeneral("FR0013269123","RUBIS","RUBIS","RUI","xpar","PA",  "FRUT" , "FRUT"));
        aMap.put("FR0013154002", new StockGeneral("FR0013154002","SARTORIUS STEDIM BIOTECH","SARTORIUS STEDIM BIOTECH","DIM","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000121709", new StockGeneral("FR0000121709","SEB","SEB","SK","xpar","PA",  "FRCG" , "FRCG"));
        aMap.put("FR0010411983", new StockGeneral("FR0010411983","SCOR SE","SCOR SE","SCR","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("LU0088087324", new StockGeneral("LU0088087324","SES","SES","SESG","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0013227113", new StockGeneral("FR0013227113","SOITEC","SOITEC","SOI","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0000050809", new StockGeneral("FR0000050809","SOPRA STERIA GROUP","SOPRA STERIA GROUP","SOP","xpar","PA", "FRTEC" ,  "FRTEC"));
        aMap.put("FR0012757854", new StockGeneral("FR0012757854","SPIE","SPIE","SPIE","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010613471", new StockGeneral("FR0010613471","SUEZ","SUEZ","SEV","xpar","PA", "FRUT" ,  "FRUT"));
        aMap.put("FR0004188670", new StockGeneral("FR0004188670","TARKETT","TARKETT","TKTT","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010918292", new StockGeneral("FR0010918292","TECHNICOLOR","TECHNICOLOR","TCH","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000051807", new StockGeneral("FR0000051807","TELEPERFORMANCE","TELEPERFORMANCE","TEP","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000054900", new StockGeneral("FR0000054900","TF1","TF1","TFI","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000121329", new StockGeneral("FR0000121329","THALES","THALES","HO","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0005691656", new StockGeneral("FR0005691656","TRIGANO","TRIGANO","TRI","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000054470", new StockGeneral("FR0000054470","UBISOFT","UBISOFT","UBI","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000120354", new StockGeneral("FR0000120354","VALLOUREC","VALLOUREC","VK","xpar","PA", "FRIN" ,  "FRIN")); // FR0000130338
        aMap.put("FR0000031775", new StockGeneral("FR0000031775","VICAT","VICAT","VCT","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000121204", new StockGeneral("FR0000121204","WENDEL","WENDEL","MF","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0011981968", new StockGeneral("FR0011981968","WORLDLINE","WORLDLINE","WLN","xpar","PA", "FRIN" ,  "FRIN"));
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


    public static String getCode(Map<String, StockGeneral> ref , String codif) {
        for (StockGeneral g: ref.values()) {
            if (g.getCodif().equals(codif)) return g.getCode();
        }
        return null;
    }

    public static String getPlace(Map<String, StockGeneral> ref ,String codif) {
        for (StockGeneral g: ref.values()) {
            if (g.getCodif().equals(codif)) return g.getPlace();
        }
        return null;
    }


    public static String getSector(Map<String, StockGeneral> ref ,String codif) {
        for (StockGeneral g: ref.values()) {
            if (g.getCodif().equals(codif)) return g.getSector();
        }
        return null;
    }



    public static String getCodeByName(String name) {
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {
            if (g.getNameBoursier().compareToIgnoreCase(name) == 0) return g.getCodif();
        }
        return null;
    }

    public static String getCodeExByName(String name) {
        for (StockGeneral g: CacheStockGeneral.getIsinExCache().values()) {
            if (g.getNameBoursier().compareToIgnoreCase(name) == 0) return g.getCodif();
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
