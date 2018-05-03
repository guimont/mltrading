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
        aMap.put("NL0000226223", new StockGeneral("NL0000226223","STEF","STM","xpar","PA", "FRTEC" ,  "FRTEC"));
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
        aMap.put("FR0000131104", new StockGeneral("FR0000131104","Bnp Paribas","BNP","xpar","PA", "FRFIN" , "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-boiron,xpar,boi,fr0000061129,isin.html
        aMap.put("FR0000061129", new StockGeneral("FR0000061129","BOIRON","BOI","xpar","PA", "FRHC" , "FRHC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-bollore,xpar,bol,fr0000039299,isin.html
        aMap.put("FR0000039299", new StockGeneral("FR0000039299","BOLLORÉ","BOL","xpar","PA", "FRIN" , "FRIN"));
        aMap.put("FR0000120503", new StockGeneral("FR0000120503","Bouygues","EN","xpar","PA", "FRIN" ,  "FRCM"));
        //https://investir.lesechos.fr/cours/profil-societe-action-bureau-veritas,xpar,bvi,fr0006174348,isin.html
        aMap.put("FR0006174348", new StockGeneral("FR0006174348","BUREAU VERITAS","BVI","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-burelle,xpar,bur,fr0000061137,isin.html
        aMap.put("FR0000061137", new StockGeneral("FR0000061137","BURELLE","BUR","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000125338", new StockGeneral("FR0000125338","Capgemini","CAP","xpar","PA", "FRTEC" ,  "FRSCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-carmila,xpar,carm,fr0010828137,isin.html
        aMap.put("FR0010828137", new StockGeneral("FR0010828137","CARMILA","CARM","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000120172", new StockGeneral("FR0000120172","Carrefour","CA","xpar","PA", "FRCS" ,  "FRFDR"));
        //https://investir.lesechos.fr/cours/profil-societe-action-casino-guichard,xpar,co,fr0000125585,isin.html
        aMap.put("FR0000125585", new StockGeneral("FR0000125585","CASINO GUICHARD-PERRACHON","CO","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-cnp-assurances,xpar,cnp,fr0000120222,isin.html
        aMap.put("FR0000120222", new StockGeneral("FR0000120222","CNP ASSURANCES","CNP","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-colas,xpar,re,fr0000121634,isin.html
        aMap.put("FR0000121634", new StockGeneral("FR0000121634","COLAS","RE","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000045072", new StockGeneral("FR0000045072","Credit Agricole","ACA","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FR0000120644", new StockGeneral("FR0000120644","Danone","BN","xpar","PA", "FRCG" ,  "FRFPR"));
        //https://investir.lesechos.fr/cours/profil-societe-action-dassault-aviation,xpar,am,fr0000121725,isin.html
        aMap.put("FR0000121725", new StockGeneral("FR0000121725","DASSAULT AVIATION","AM","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-dassault-systemes,xpar,dsy,fr0000130650,isin.html
        aMap.put("FR0000130650", new StockGeneral("FR0000130650","DASSAULT SYSTÈMES","DSY","xpar","PA", "FRTEC" ,  "FRTEC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-dbv-technologies,xpar,dbv,fr0010417345,isin.html
        aMap.put("FR0010417345", new StockGeneral("FR0010417345","DBV TECHNOLOGIES","DBV","xpar","PA", "FRHC" ,  "FRHC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-direct-energie,xpar,diren,fr0004191674,isin.html
        aMap.put("FR0004191674", new StockGeneral("FR0004191674","DIRECT ENERGIE","DIREN","xpar","PA", "FRUT" ,  "FRUT"));
        //https://investir.lesechos.fr/cours/profil-societe-action-edenred,xpar,eden,fr0010908533,isin.html
        aMap.put("FR0010908533", new StockGeneral("FR0010908533","EDENRED ","EDEN","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-edf,xpar,edf,fr0010242511,isin.html
        aMap.put("FR0010242511", new StockGeneral("FR0010242511","EDF","EDF","xpar","PA", "FRUT" ,  "FRUT"));
        //https://investir.lesechos.fr/cours/profil-societe-action-eiffage,xpar,fgr,fr0000130452,isin.html
        aMap.put("FR0000130452", new StockGeneral("FR0000130452","EIFFAGE","FGR","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-elior-group,xpar,elior,fr0011950732,isin.html
        aMap.put("FR0011950732", new StockGeneral("FR0011950732","ELIOR","ELIOR","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-elis,xpar,elis,fr0012435121,isin.html
        aMap.put("FR0012435121", new StockGeneral("FR0012435121","ELIS","ELIS","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0010208488", new StockGeneral("FR0010208488","Engie","ENGI","xpar","PA", "FRUT" ,  "FRGWM"));
        //https://investir.lesechos.fr/cours/profil-societe-action-eramet,xpar,era,fr0000131757,isin.html
        aMap.put("FR0000131757", new StockGeneral("FR0000131757","ERAMET ","ERA","xpar","PA", "FRBM" ,  "FRBM"));
        aMap.put("FR0000121667", new StockGeneral("FR0000121667","Essilor Intl","EI","xpar","PA", "FRHC" ,  "FRHC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-euler-hermes-group,xpar,ele,fr0004254035,isin.html
        aMap.put("FR0004254035", new StockGeneral("FR0004254035","EULER HERMES GROUP","ELE","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-eurazeo,xpar,rf,fr0000121121,isin.html
        aMap.put("FR0000121121", new StockGeneral("FR0000121121","EURAZEO","RF","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-eurofins-scientific,xpar,erf,fr0000038259,isin.html
        aMap.put("FR0000038259", new StockGeneral("FR0000038259","EUROFINS SCIENTIFIC","ERF","xpar","PA", "FRHC" ,  "FRHC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-euronext,xpar,enx,nl0006294274,isin.html
        aMap.put("NL0006294274", new StockGeneral("NL0006294274","EURONEXT","ENX","xpar","PA", "FRHC" ,  "FRHC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-europcar,xpar,eucar,fr0012789949,isin.html
        aMap.put("FR0012789949", new StockGeneral("FR0012789949","EUROPCAR","EUCAR","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-eutelsat-communications,xpar,etl,fr0010221234,isin.html
        aMap.put("FR0010221234", new StockGeneral("FR0010221234","EUTELSAT COMMUNICATIONS","ETL","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-faurecia,xpar,eo,fr0000121147,isin.html
        aMap.put("FR0000121147", new StockGeneral("FR0000121147","FAURECIA","EO","xpar","PA", "FRCG" ,  "FRCG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-ffp,xpar,ffp,fr0000064784,isin.html
        aMap.put("FR0000064784", new StockGeneral("FR0000064784","FFP","FFP","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-financiere-odet,xpar,odet,fr0000062234,isin.html
        aMap.put("FR0000062234", new StockGeneral("FR0000062234","ODET","ODET","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-fnac-darty,xpar,fnac,fr0011476928,isin.html
        aMap.put("FR0011476928", new StockGeneral("FR0011476928","FNAC DARTY","FNAC","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-fonciere-des-regions,xpar,fdr,fr0000064578,isin.html
        aMap.put("FR0000064578", new StockGeneral("FR0000064578","FONCIERE DES REGIONS","FDR","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-fonciere-lyonnaise,xpar,fly,fr0000033409,isin.html
        aMap.put("FR0000033409", new StockGeneral("FR0000033409","FONCIERE LYONNAISE","FLY","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-gecina,xpar,gfc,fr0010040865,isin.html
        aMap.put("FR0010040865", new StockGeneral("FR0010040865","GECINA ","GFC","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-general-electric,xpar,gne,us3696041033,isin.html
        aMap.put("US3696041033", new StockGeneral("US3696041033","GENERAL ELECTRIC","GNE","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-groupe-eurotunnel,xpar,get,fr0010533075,isin.html
        aMap.put("FR0010533075", new StockGeneral("FR0010533075","GROUPE EUROTUNNEL","GET","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-gtt-gaztransport-&-technigaz,xpar,gtt,fr0011726835,isin.html
        aMap.put("FR0011726835", new StockGeneral("FR0011726835","GTT - GAZTRANSPORT ET TECHNIGAZ","GTT","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-hermes-intl,xpar,rms,fr0000052292,isin.html
        aMap.put("FR0000052292", new StockGeneral("FR0000052292","HERMES INTL","RMS","xpar","PA", "FRCG" ,  "FRCG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-hsbc-holdings,xpar,hsb,gb0005405286,isin.html
        aMap.put("GB0005405286", new StockGeneral("GB0005405286","HERMES INTL","HSB","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-icade,xpar,icad,fr0000035081,isin.html
        aMap.put("FR0000035081", new StockGeneral("FR0000035081","ICADE","ICAD","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-iliad,xpar,ild,fr0004035913,isin.html
        aMap.put("FR0004035913", new StockGeneral("FR0004035913","ILIAD","ILD","xpar","PA", "FRTEC" ,  "FRTEC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-imerys,xpar,nk,fr0000120859,isin.html
        aMap.put("FR0000120859", new StockGeneral("FR0000120859","IMERYS ","NK","xpar","PA", "FRBM" ,  "FRBM"));
        //https://investir.lesechos.fr/cours/action-ingenico-group,xpar,ing,fr0000125346,isin.html
        aMap.put("FR0000125346", new StockGeneral("FR0000125346","INGENICO ","ING","xpar","PA", "FRTEC" ,  "FRTEC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-ipsen,xpar,ipn,fr0010259150,isin.html
        aMap.put("FR0010259150", new StockGeneral("FR0010259150","IPSEN ","IPN","xpar","PA", "FRHC" ,  "FRHC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-ipsos,xpar,ips,fr0000073298,isin.html
        aMap.put("FR0000073298", new StockGeneral("FR0000073298","IPSOS ","IPS","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-jcdecaux-sa,xpar,dec,fr0000077919,isin.html
        aMap.put("FR0000077919", new StockGeneral("FR0000077919","JCDECAUX  ","DEC","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("FR0000121485", new StockGeneral("FR0000121485","Kering","KER","xpar","PA", "FRCS" ,  "FRGR"));
        aMap.put("FR0000121964", new StockGeneral("FR0000121964","klepierre","LI","xpar","PA", "FRFIN" ,  "FRRE"));
        //https://investir.lesechos.fr/cours/profil-societe-action-korian,xpar,kori,fr0010386334,isin.html
        aMap.put("FR0010386334", new StockGeneral("FR0010386334","KORIAN  ","KORI","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000120321", new StockGeneral("FR0000120321","Loreal","OREAL","xpar","PA", "FRCG" ,  "FRPG"));
        aMap.put("CH0012214059", new StockGeneral("CH0012214059","LafargeHolcim-ltd","LHN","xpar","PA", "FRIN" ,  "FRCM"));
        //https://investir.lesechos.fr/cours/profil-societe-action-lagardere-sca,xpar,mmb,fr0000130213,isin.html
        aMap.put("FR0000130213", new StockGeneral("FR0000130213","LAGARDERE","MMB","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-ldc,xpar,loup,fr0013204336,isin.html
        aMap.put("FR0013204336", new StockGeneral("FR0013204336","LDC","LOUP","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0010307819", new StockGeneral("FR0010307819","Legrand","LR","xpar","PA", "FRIN" ,  "FREEE"));
        //https://investir.lesechos.fr/cours/profil-societe-action-lisi,xpar,fii,fr0000050353,isin.html
        aMap.put("FR0000050353", new StockGeneral("FR0000050353","LISI","FII","xpar","PA", "FRIN" ,  "FREEE"));
        aMap.put("FR0000121014", new StockGeneral("FR0000121014","Lvmh","MC","xpar","PA", "FRCG" , "FRPG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-maisons-du-monde,xpar,mdm,fr0013153541,isin.html
        aMap.put("FR0013153541", new StockGeneral("FR0013153541","MAISONS DU MONDE","MDM","xpar","PA", "FRCG" , "FRCG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-mercialys,xpar,mery,fr0010241638,isin.html
        aMap.put("FR0010241638", new StockGeneral("FR0010241638","MERCIALYS ","MERY","xpar","PA", "FRFIN" , "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-metropole-tv,xpar,mmt,fr0000053225,isin.html
        aMap.put("FR0000053225", new StockGeneral("FR0000053225","METROPOLE TV ","MMT","xpar","PA", "FRCS" , "FRCS"));
        aMap.put("FR0000121261", new StockGeneral("FR0000121261","Michelin","ML","xpar","PA", "FRCG" ,  "FRAP"));
        //https://investir.lesechos.fr/cours/profil-societe-action-natixis,xpar,kn,fr0000120685,isin.html
        aMap.put("FR0000120685", new StockGeneral("FR0000120685","NATIXIS","KN","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-nexans,xpar,nex,fr0000044448,isin.html
        aMap.put("FR0000044448", new StockGeneral("FR0000044448","NEXANS","NEX","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-nexity,xpar,nxi,fr0010112524,isin.html
        aMap.put("FR0010112524", new StockGeneral("FR0010112524","NEXITY","NXI","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("FI0009000681", new StockGeneral("FI0009000681","Nokia","NOKIA","xpar","PA", "FRTEC" ,  "FRTHF"));
        aMap.put("FR0000133308", new StockGeneral("FR0000133308","Orange","ORA","xpar","PA", "FRTEL" ,  "FRTEL"));
        //https://investir.lesechos.fr/cours/profil-societe-action-orpea,xpar,orp,fr0000184798,isin.html
        aMap.put("FR0000184798", new StockGeneral("FR0000184798","ORPEA","ORP","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000120693", new StockGeneral("FR0000120693","Pernod Ricard","RI","xpar","PA", "FRCG" ,  "FRBEV"));
        aMap.put("FR0000121501", new StockGeneral("FR0000121501","Peugeot","UG","xpar","PA", "FRCG" ,  "FRAP"));
        //https://investir.lesechos.fr/cours/profil-societe-action-plastic-omnium,xpar,pom,fr0000124570,isin.html
        aMap.put("FR0000124570", new StockGeneral("FR0000124570","PLASTIC OMNIUM","POM","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000130577", new StockGeneral("FR0000130577","Publicis Groupe","PUB","xpar","PA", "FRCS" ,  "FRMED"));
        //https://investir.lesechos.fr/cours/profil-societe-action-remy-cointreau,xpar,rco,fr0000130395,isin.html
        aMap.put("FR0000130395", new StockGeneral("FR0000130395","REMY COINTREAU","RCO","xpar","PA", "FRCS" ,  "FRMED"));
        aMap.put("FR0000131906", new StockGeneral("FR0000131906","Renault","RNO","xpar","PA",  "FRCG" , "FRAP"));
        //https://investir.lesechos.fr/cours/profil-societe-action-rexel,xpar,rxl,fr0010451203,isin.html
        aMap.put("FR0010451203", new StockGeneral("FR0010451203","REXEL","RXL","xpar","PA",  "FRIN" , "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-rothschild-&-co,xpar,roth,fr0000031684,isin.html
        aMap.put("FR0000031684", new StockGeneral("FR0000031684","ROTHSCHILD & CO","ROTH","xpar","PA",  "FRFIN" , "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-rubis,xpar,rui,fr0013269123,isin.html
        aMap.put("FR0013269123", new StockGeneral("FR0013269123","RUBIS","RUI","xpar","PA",  "FRUT" , "FRUT"));
        //https://investir.lesechos.fr/cours/profil-societe-action-seb,xpar,sk,fr0000121709,isin.html
        aMap.put("FR0000121709", new StockGeneral("FR0000121709","SEB","SK","xpar","PA",  "FRCG" , "FRCG"));
        aMap.put("FR0000073272", new StockGeneral("FR0000073272","Safran","SAF","xpar","PA",  "FRIN" , "FRAD"));
        aMap.put("FR0000125007", new StockGeneral("FR0000125007","Saint Gobain","SGO","xpar","PA", "FRIN" ,  "FRCM"));
        aMap.put("FR0000120578", new StockGeneral("FR0000120578","Sanofi","SAN","xpar","PA", "FRHC" ,  "FRPB"));
        //https://investir.lesechos.fr/cours/profil-societe-action-sartorius-sted-bio,xpar,dim,fr0013154002,isin.html
        aMap.put("FR0013154002", new StockGeneral("FR0013154002","SARTORIUS STED BIO","DIM","xpar","PA", "FRHC" ,  "FRHC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-schlumberger,xpar,slb,an8068571086,isin.html
        aMap.put("AN8068571086", new StockGeneral("AN8068571086","SCHLUMBERGER","SLB","xpar","PA", "FROG" ,  "FROG"));
        aMap.put("FR0000121972", new StockGeneral("FR0000121972","Schneider Electric","SU","xpar","PA", "FRIN" ,  "FREEE"));
        //https://investir.lesechos.fr/cours/profil-societe-action-scor-se,xpar,scr,fr0010411983,isin.html
        aMap.put("FR0010411983", new StockGeneral("FR0010411983","SCOR SE","SCR","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-ses,xpar,sesg,lu0088087324,isin.html
        aMap.put("LU0088087324", new StockGeneral("LU0088087324","SES","SESG","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-smcp,xpar,smcp,fr0013214145,isin.html
        aMap.put("FR0013214145", new StockGeneral("FR0013214145","SMCP","SMCP","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000130809", new StockGeneral("FR0000130809","Societe Generale","GLE","xpar","PA", "FRFIN" ,  "FRFIN"));
        aMap.put("BE0003470755", new StockGeneral("BE0003470755","Solvay","SOLB","xbru","BR", "FRBM" ,  "FRBM"));
        aMap.put("FR0000121220", new StockGeneral("FR0000121220","Sodexo","SW","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-somfy-sa,xpar,so,fr0013199916,isin.html
        aMap.put("FR0013199916", new StockGeneral("FR0013199916","SOMFY SA","SO","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-sopra-steria-group,xpar,sop,fr0000050809,isin.html
        aMap.put("FR0000050809", new StockGeneral("FR0000050809","SOPRA STERIA GROUP","SOP","xpar","PA", "FRTEC" ,  "FRTEC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-spie,xpar,spie,fr0012757854,isin.html
        aMap.put("FR0012757854", new StockGeneral("FR0012757854","SPIE","SPIE","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-stef,xpar,stf,fr0000064271,isin.html
        aMap.put("FR0000064271", new StockGeneral("FR0000064271","STEF","STF","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-stmicroelectronics,xpar,stm,nl0000226223,isin.html
        aMap.put("NL0000226223", new StockGeneral("NL0000226223","STEF","STM","xpar","PA", "FRTEC" ,  "FRTEC"));
        //https://investir.lesechos.fr/cours/profil-societe-action-suez,xpar,sev,fr0010613471,isin.html
        aMap.put("FR0010613471", new StockGeneral("FR0010613471","SUEZ","SEV","xpar","PA", "FRUT" ,  "FRUT"));
        //https://investir.lesechos.fr/cours/profil-societe-action-tarkett,xpar,tktt,fr0004188670,isin.html
        aMap.put("FR0004188670", new StockGeneral("FR0004188670","TARKETT","TKTT","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-technicolor,xpar,tch,fr0010918292,isin.html
        aMap.put("FR0010918292", new StockGeneral("FR0010918292","TECHNICOLOR","TCH","xpar","PA", "FRCS" ,  "FRCS"));
        aMap.put("gb00bdsfg982", new StockGeneral("gb00bdsfg982","technipfmc","FTI","xpar","PA", "FROG" ,  "FROG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-teleperformance,xpar,rcf,fr0000051807,isin.html
        aMap.put("FR0000051807", new StockGeneral("FR0000051807","TELEPERFORMANCE","RCF","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-tf1,xpar,tfi,fr0000054900,isin.html
        aMap.put("FR0000054900", new StockGeneral("FR0000054900","TF1","TFI","xpar","PA", "FRCS" ,  "FRCS"));
        //https://investir.lesechos.fr/cours/profil-societe-action-thales,xpar,ho,fr0000121329,isin.html
        aMap.put("FR0000121329", new StockGeneral("FR0000121329","THALES","HO","xpar","PA", "FRIN" ,  "FRIN"));
        aMap.put("FR0000120271", new StockGeneral("FR0000120271","Total","FP","xpar","PA", "FROG" ,  "FROG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-trigano,xpar,tri,fr0005691656,isin.html
        aMap.put("FR0005691656", new StockGeneral("FR0005691656","TRIGANO","TRI","xpar","PA", "FRCG" ,  "FRCG"));
        //https://investir.lesechos.fr/cours/profil-societe-action-ubisoft-entertainment,xpar,ubi,fr0000054470,isin.html
        aMap.put("FR0000054470", new StockGeneral("FR0000054470","UBISOFT ENTERTAINMENT","UBI","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000124711", new StockGeneral("FR0000124711","Unibail-Rodamco","UL","xams","PA", "FRFIN" ,  "FRRE"));
        aMap.put("FR0013176526", new StockGeneral("FR0013176526","Valeo","FR","xpar","PA", "FRCG" ,  "FRAP")); // FR0000130338
        //https://investir.lesechos.fr/cours/profil-societe-action-vallourec,xpar,vk,fr0000120354,isin.html
        aMap.put("FR0000120354", new StockGeneral("FR0000120354","VALLOUREC","VK","xpar","PA", "FRIN" ,  "FRIN")); // FR0000130338
        aMap.put("FR0000124141", new StockGeneral("FR0000124141","Veolia environnement","VIE","xpar","PA", "FRUT" ,  "FRGWM"));
        //https://investir.lesechos.fr/cours/profil-societe-action-vicat,xpar,vct,fr0000031775,isin.html
        aMap.put("FR0000031775", new StockGeneral("FR0000031775","VICAT","VCT","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-vilmorin-&-cie,xpar,rin,fr0000052516,isin.html
        aMap.put("FR0000052516", new StockGeneral("FR0000052516","VILMORIN & CIE","RIN","xpar","PA", "FRCG" ,  "FRCG"));
        aMap.put("FR0000125486", new StockGeneral("FR0000125486","Vinci","DG","xpar","PA", "FRIN" ,  "FRCM"));
        //https://investir.lesechos.fr/cours/profil-societe-action-virbac,xpar,virp,fr0000031577,isin.html
        aMap.put("FR0000031577", new StockGeneral("FR0000031577","VIRBAC","VIRP","xpar","PA", "FRHC" ,  "FRHC"));
        aMap.put("FR0000127771", new StockGeneral("FR0000127771","Vivendi","VIV","xpar","PA", "FRCS" ,  "FRMED"));
        //https://investir.lesechos.fr/cours/profil-societe-action-wendel,xpar,mf,fr0000121204,isin.html
        aMap.put("FR0000121204", new StockGeneral("FR0000121204","WENDEL","MF","xpar","PA", "FRFIN" ,  "FRFIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-worldline,xpar,wln,fr0011981968,isin.html
        aMap.put("FR0011981968", new StockGeneral("FR0011981968","WORLDLINE","WLN","xpar","PA", "FRIN" ,  "FRIN"));
        //https://investir.lesechos.fr/cours/profil-societe-action-xpo-logistics,xpar,xpo,fr0000052870,isin.html
        aMap.put("FR0000052870", new StockGeneral("FR0000052870","XPO LOGISTIC","XPO","xpar","PA", "FRIN" ,  "FRIN"));


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
