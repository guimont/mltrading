package com.mltrading.models.stock;

import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gmo on 21/03/2017.
 */
public class StockPerformanceList {

    List <StockPerformance> perfList = new ArrayList<>();

    public StockPerformanceList processingList() {

        //CacheStockGeneral.getIsinCache().values().stream().map(s -> perfList.add(new StockPerformance().load(s.getCodif()))).collect(Collectors.toList());

        CacheStockGeneral.getIsinCache().values().forEach(s -> {
            StockPerformance p = new StockPerformance().load(s.getCodif());
            if (p != null) perfList.add(p);
        });

        //CacheStockSector.getSectorCache().values().forEach(s -> perfList.add(new StockPerformance().load(s.getCodif())));*/

        return this;
    }

}
