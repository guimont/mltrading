package com.mltrading.models.util.impl;


import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.cache.CacheRawMaterial;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockIndice;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.util.CsvFileReader;
import com.mltrading.models.util.CsvFileWriter;
import com.mltrading.models.util.ExportTools;

import java.util.ArrayList;
import java.util.List;


public class ExportToolsImpl implements ExportTools {


    @Override
    public void exportRaw() {
        CsvFileWriter fileWriter = new CsvFileWriter("raw.csv");
        List<? extends StockHistory> sr = new ArrayList(CacheRawMaterial.getCache().values());
        sr.stream().forEach(s -> StockHistory.getAllHistory(s.getCode()).stream().forEach(e -> fileWriter.writeData(e)));
        fileWriter.close();
    }

    @Override
    public void exportStock() {
        CsvFileWriter fileWriter = new CsvFileWriter("stock.csv");
        List<? extends StockHistory> sr = new ArrayList(CacheStockGeneral.getCache().values());
        sr.stream().forEach(s -> StockHistory.getAllHistory(s.getCodif()).stream().forEach(e -> fileWriter.writeData(e)));
        fileWriter.close();
    }

    @Override
    public void exportSector() {
        CsvFileWriter fileWriter = new CsvFileWriter("sector.csv");
        List<? extends StockHistory> sr = new ArrayList(CacheStockSector.getSectorCache().values());
        sr.stream().forEach(s -> StockHistory.getAllHistory(s.getCode()).stream().forEach(e -> fileWriter.writeData(e)));
        fileWriter.close();
    }

    @Override
    public void exportIndice() {
        CsvFileWriter fileWriter = new CsvFileWriter("indice.csv");
        List<? extends StockHistory> sr = new ArrayList(CacheStockIndice.getIndiceCache().values());
        sr.stream().forEach(s -> StockHistory.getAllHistory(s.getCode()).stream().forEach(e -> fileWriter.writeData(e)));
        fileWriter.close();
    }

    @Override
    public void exportVcac() {
        CsvFileWriter fileWriter = new CsvFileWriter("vcac.csv");
        StockHistory.getAllHistory("VCAC").stream().forEach(e -> fileWriter.writeData(e));
        fileWriter.close();
    }


    @Override
    public void exportAT() {
        CsvFileWriter fileWriter = new CsvFileWriter("at.csv");

        List<? extends StockHistory> sr = new ArrayList(CacheRawMaterial.getCache().values());
        sr.stream().forEach(s -> StockAnalyse.getAllAnalyse(s.getCode()).stream().forEach(e -> fileWriter.writeData(s.getCode(),e)));

        List<? extends StockHistory> sh = new ArrayList(CacheStockGeneral.getCache().values());
        sh.stream().filter(s -> !s.getCodif().equalsIgnoreCase("STM")).forEach(s -> StockAnalyse.getAllAnalyse(s.getCodif()).stream().forEach(e -> fileWriter.writeData(s.getCodif(),e)));

        List<? extends StockHistory> ss = new ArrayList(CacheStockSector.getSectorCache().values());
        ss.stream().forEach(s ->  StockAnalyse.getAllAnalyse(s.getCode()).stream().forEach(e -> fileWriter.writeData(s.getCode(),e)));

        List<? extends StockHistory> si = new ArrayList(CacheStockIndice.getIndiceCache().values());
        si.stream().forEach(s -> StockAnalyse.getAllAnalyse(s.getCode()).stream().forEach(e -> fileWriter.writeData(s.getCode(),e)));

        StockAnalyse.getAllAnalyse("VCAC").stream().forEach(e -> fileWriter.writeData("VCAC",e));
        fileWriter.close();
    }


    @Override
    public void importRaw() {
        new CsvFileReader("raw.csv", false);
    }

    @Override
    public void importSector() {
        new CsvFileReader("sector.csv",false);
    }

    @Override
    public void importIndice() {
        new CsvFileReader("indice.csv" , false);
    }



    @Override
    public void importVcac() {
        new CsvFileReader("vcac.csv", false);
    }

    @Override
    public void importStock() {
        new CsvFileReader("stock.csv", false);
    }


    @Override
    public void importAT() {
        new CsvFileReader("at.csv", true);
    }


}
