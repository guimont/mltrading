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
        aMap.put("FRINT", new StockSector("FRINT", "sectorTransport", "PA"));
        aMap.put("FRCM", new StockSector("FRCM", "sectorConstruction", "PA"));
        aMap.put("FRAD", new StockSector("FRAD", "sectorDefenseAero", "PA"));
        aMap.put("FREEE", new StockSector("FREEE", "sectorElecEquip", "PA"));
        aMap.put("FRIE", new StockSector("FRIE", "sectorIngIndus", "PA"));
        aMap.put("FRSS", new StockSector("FRSS", "sectorSupService", "PA"));

        aMap.put("FROGP", new StockSector("FROGP", "sectorProdPetrol", "PA"));

        aMap.put("FRBM", new StockSector("FRBM", "sectorBasicMat", "PA"));

        aMap.put("FRAP", new StockSector("FRAP", "sectorAutoEquip", "PA"));
        aMap.put("FRBEV", new StockSector("FRBEV", "sectorBoisson", "PA"));
        aMap.put("FRFPR", new StockSector("FRFPR", "sectorAgro", "PA"));
        aMap.put("FRHG", new StockSector("FRHG", "sectorProdMena", "PA"));
        aMap.put("FRLEG", new StockSector("FRLEG", "sectorLoisirEquip", "PA"));
        aMap.put("FRPG", new StockSector("FRPG", "sectorArtPer", "PA"));

        aMap.put("FRHC", new StockSector("FRHC", "sectorSante", "PA"));
        aMap.put("FRPB", new StockSector("FRPB", "sectorPharma", "PA"));

        aMap.put("FRFDR", new StockSector("FRFDR", "sectorDistribAlim", "PA"));
        aMap.put("FRGR", new StockSector("FRGR", "sectorDistribGen", "PA"));

        aMap.put("FRMED", new StockSector("FRMED", "sectorMediaPub", "PA"));
        aMap.put("FRTL", new StockSector("FRTL", "sectorVoyage", "PA"));

        aMap.put("FRTEL", new StockSector("FRTEL", "sectorTelecom", "PA"));

        aMap.put("FRUT", new StockSector("FRUT", "sectorServiCollect", "PA"));
        aMap.put("FRGWM", new StockSector("FRGWM", "sectorGazEau", "PA"));
        aMap.put("FRFIN", new StockSector("FRFIN", "sectorFinance", "PA"));
        aMap.put("FRRE", new StockSector("FRRE", "sectorImmo", "PA"));
        aMap.put("FRSCS", new StockSector("FRSCS", "sectorSoftInfo", "PA"));
        aMap.put("FRTHF", new StockSector("FRTHF", "sectorEquiInfo", "PA"));

        SectorMap = Collections.unmodifiableMap(aMap);
    }


    public static Map<String,StockSector> getSectorCache() {
        return SectorMap;
    }

}
