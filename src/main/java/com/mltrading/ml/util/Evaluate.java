package com.mltrading.ml.util;

import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gmo on 19/06/2017.
 */
public abstract class Evaluate {

    private static final Logger log = LoggerFactory.getLogger(Evaluate.class);


    public int getRowSector(String codif) {
        int rowSector = CacheStockSector.NO_SECTOR;
        try {
            StockGeneral sg = CacheStockGeneral.getCache().get(CacheStockGeneral.getCode(codif));
            String sector = sg.getSector();
            if (sector != null)
                rowSector = CacheStockSector.getSectorCache().get(sector).getRow();
        } catch (Exception e) {
            log.error("Cannot find sector for: "+ codif);
        }

        return rowSector;
    }

}
