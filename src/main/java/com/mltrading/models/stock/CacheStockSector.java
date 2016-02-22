package com.mltrading.models.stock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gmo on 15/12/2015.
 */
public class CacheStockSector {

    private static final Map<String, StockSector> SectorMap;
    static {
        Map<String, StockSector> aMap = new HashMap<>();
        aMap.put("FRIN", new StockSector("FRIN", "Industrie", "PA"));
        /*aMap.put("FRINT", new StockSector("FRINT", "Transport industriel", "PA"));
        aMap.put("FRCM", new StockSector("FRCM", "Bât et matériaux de constr.", "PA"));
        aMap.put("FRAD", new StockSector("FRAD", "Aérospatiale et Défense", "PA"));
        aMap.put("FREEE", new StockSector("FREEE", "Electronique et équipmts élec", "PA"));
        aMap.put("FRIE", new StockSector("FRIE", "Ingénierie industrielle", "PA"));
        aMap.put("FRSS", new StockSector("FRSS", "CAC Sup. Services", "PA"));*/

        aMap.put("FRBM", new StockSector("FRBM", "Matériaux de base", "PA"));


        aMap.put("FROGP", new StockSector("FROGP", "Prod. de Pétrole et de Gaz", "PA"));


        aMap.put("FRCG", new StockSector("FRCG", "Biens de Consommation", "PA"));
        /*aMap.put("FRAP", new StockSector("FRAP", "Automobiles et équipementiers", "PA"));
        aMap.put("FRBEV", new StockSector("FRBEV", "Boissons", "PA"));
        aMap.put("FRFPR", new StockSector("FRFPR", "Agro-alimentaire", "PA"));
        aMap.put("FRHG", new StockSector("FRHG", "Produits ménagers", "PA"));
        aMap.put("FRLEG", new StockSector("FRLEG", "Equipements de loisirs", "PA"));
        aMap.put("FRPG", new StockSector("FRPG", "Articles Personnels", "PA"));*/

        aMap.put("FRHC", new StockSector("FRHC", "Santé", "PA"));
        /*aMap.put("FRPB", new StockSector("FRPB", "CAC PHARMA. & BIO.", "PA"));*/



        aMap.put("FRCS", new StockSector("FRCS", "Services aux consommateurs", "PA")); //media voyage
        /*aMap.put("FRMED", new StockSector("FRMED", "Médias et publicité", "PA"));
        aMap.put("FRTL", new StockSector("FRTL", "Voyages et loisirs.", "PA"));
        aMap.put("FRFDR", new StockSector("FRFDR", "Distribution - Alimentation et", "PA"));
        aMap.put("FRGR", new StockSector("FRGR", "Distributeurs généralistes", "PA"));*/

        aMap.put("FRTEL", new StockSector("FRTEL", "Télécommunications", "PA"));

        aMap.put("FRUT", new StockSector("FRUT", "Services aux collectivités", "PA"));
        /*aMap.put("FRGWM", new StockSector("FRGWM", "Gaz, eau et services multiples", "PA"));*/


        aMap.put("FRFIN", new StockSector("FRFIN", "Sociétés Financières", "PA"));
        /*aMap.put("FRRE", new StockSector("FRRE", "Immobilier", "PA"));*/

        aMap.put("FRTEC", new StockSector("FRTEC", "Technologie", "PA"));
        //aMap.put("FRTHF", new StockSector("FRTHF", "Matériel et équipements destin", "PA"));
        //aMap.put("FRSCS", new StockSector("FRSCS", "Logiciels et services informat", "PA"));*/


        SectorMap = Collections.unmodifiableMap(aMap);
    }


    public static Map<String,StockSector> getSectorCache() {
        return SectorMap;
    }

}
