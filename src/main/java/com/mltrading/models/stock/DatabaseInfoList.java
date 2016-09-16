package com.mltrading.models.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by gmo on 29/06/2016.
 */


public class DatabaseInfoList {


static final int RANGE = 600 ;
    private List<DatabaseInfo> stockList = new ArrayList<>();
    private List<DatabaseInfo> indiceList = new ArrayList<>();
    private List<DatabaseInfo> sectorList = new ArrayList<>();
    private List<DatabaseInfo> rawList = new ArrayList<>();


    public List<DatabaseInfo> getStockList() {
        return stockList;
    }

    public List<DatabaseInfo> getIndiceList() {
        return indiceList;
    }

    public List<DatabaseInfo> getSectorList() {
        return sectorList;
    }

    public List<DatabaseInfo> getRawList() {
        return rawList;
    }

    public DatabaseInfoList processingList() {

        List<StockGeneral> sg = new ArrayList(CacheStockGeneral.getIsinCache().values());

        sg.stream().map(s -> stockList.add(DatabaseInfo.populate(s.getCodif(), StockHistory.getStockHistoryLastInvert(s.getCodif(), RANGE)))).collect(Collectors.toList());

        List<? extends StockHistory> ss = new ArrayList(CacheStockSector.getSectorCache().values());
        List<? extends StockHistory> si = new ArrayList(CacheStockIndice.getIndiceCache().values());
        List<? extends StockHistory> sr = new ArrayList(CacheRawMaterial.getCache().values());

        /* source are mixed with only one stream* display are not wished
        Stream<StockHistory> stream = Stream.concat(Stream.concat(ss.stream(), si.stream()),sr.stream());
        stream.map(s -> stockList.add(DatabaseInfo.populate(s.getCode(), StockHistory.getStockHistoryLastInvert(s.getCode(), RANGE)))).collect(Collectors.toList());
        */

        ss.stream().map(s -> indiceList.add(DatabaseInfo.populate(s.getCode(), StockHistory.getStockHistoryLastInvert(s.getCode(), RANGE)))).collect(Collectors.toList());

        si.stream().map(s -> sectorList.add(DatabaseInfo.populate(s.getCode(), StockHistory.getStockHistoryLastInvert(s.getCode(), RANGE)))).collect(Collectors.toList());

        sr.stream().map(s -> rawList.add(DatabaseInfo.populate(s.getCode(), StockHistory.getStockHistoryLastInvert(s.getCode(), RANGE)))).collect(Collectors.toList());


        return this;
    }


}
