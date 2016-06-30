package com.mltrading.models.stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 29/06/2016.
 */


public class DatabaseInfoList {


static final int RANGE = 300 ;
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

        for (StockGeneral s:sg) {
            List<StockHistory> l = StockHistory.getStockHistoryLastInvert(s.getCode(), RANGE);
            stockList.add(DatabaseInfo.populate(s.getCodif(), l));
        }

        List<StockSector> ss = new ArrayList(CacheStockSector.getSectorCache().values());

        for (StockSector s:ss) {
            List<StockSector> l = StockSector.getStockSectorLastInvert(s.getCode(), RANGE);
            sectorList.add(DatabaseInfo.populate(s.getCode(), l));
        }

        List<StockIndice> si = new ArrayList(CacheStockIndice.getIndiceCache().values());

        for (StockIndice s:si) {
            List<StockIndice> l = StockIndice.getStockIndiceLastInvert(s.getCode(), RANGE);
            indiceList.add(DatabaseInfo.populate(s.getCode(), l));
        }


        List<StockRawMat> sr = new ArrayList(CacheRawMaterial.getCache().values());

        for (StockRawMat s:sr) {
            List<StockRawMat> l = StockRawMat.getStockIndiceLastInvert(s.getCode(), RANGE);
            rawList.add(DatabaseInfo.populate(s.getCode(), l));
        }

        return this;

    }


}
