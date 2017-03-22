package com.mltrading.models.stock;

import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gmo on 21/03/2017.
 */
public class StockPerformanceList implements Serializable {

    boolean running = false;
    boolean saving = false;
    List <StockPerformance> perfList = new ArrayList<>();

    public StockPerformanceList processingList() {

        //java 8 style
        //CacheStockGeneral.getIsinCache().values().stream().map(s -> perfList.add(new StockPerformance().load(s.getCodif()))).filter(p-> p != null).collect(Collectors.toList());

        CacheStockGeneral.getIsinCache().values().forEach(s -> {
            StockPerformance p = new StockPerformance().load(s.getCodif());
            if (p != null) perfList.add(p);
        });

        CacheStockSector.getSectorCache().values().forEach(s -> {
            StockPerformance p = new StockPerformance().load(s.getCodif());
            if (p != null) perfList.add(p);
        });


        return this;
    }


    public List<StockPerformance> getPerfList() {
        return perfList;
    }

    public void setPerfList(List<StockPerformance> perfList) {
        this.perfList = perfList;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isSaving() {
        return saving;
    }

    public void setSaving(boolean saving) {
        this.saving = saving;
    }
}
