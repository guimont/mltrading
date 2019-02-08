package com.mltrading.asset;

import com.mltrading.assetmanagement.AssetManagement;
import com.mltrading.assetmanagement.AssetProperties;
import com.mltrading.assetmanagement.AssetStock;
import com.mltrading.assetmanagement.RulingSimple;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockPrediction;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RulingTest {

    static int INVEST = 10000;

    @Test
    public void processTest() {

        CacheStockGeneral.getCache().clear();

        StockGeneral sg = new StockGeneral();
        sg.setCode("FR0000133308");
        sg.setCodif("ORA");
        sg.setSector("FRFIN");
        sg.setValue(44.72);
        sg.setPrediction(new StockPrediction(sg.getCodif()).setYieldD20(4.2));
        CacheStockGeneral.getCache().put(sg.getCode(), sg);


        Map<String,AssetStock> curentAssetStock = new HashMap<String, AssetStock>();


        RulingSimple ruling = new RulingSimple();
        //AssetManagement assetManagement = new AssetManagement();
        //ruling.process(curentAssetStock,new AssetProperties("bink", 0, true, 9), INVEST);

        assertThat(curentAssetStock.size()).isEqualTo(1);
    }


    @Test
    public void processTwoEntryTest() {

        CacheStockGeneral.getCache().clear();

        StockGeneral sg = new StockGeneral();
        sg.setCode("FR0000133308");
        sg.setCodif("ORA");
        sg.setSector("FRFIN");
        sg.setValue(44.72);
        StockPrediction pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.7);
        pred.setYieldD20(4.2);
        sg.setPrediction(pred);

        CacheStockGeneral.getCache().put(sg.getCode(), sg);

        sg = new StockGeneral();
        sg.setCode("FR0000120404");
        sg.setCodif("AC");
        sg.setSector("FRCS");
        sg.setValue(69.12);
        pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.65);
        pred.setYieldD20(-5.4);
        sg.setPrediction(pred);
        CacheStockGeneral.getCache().put(sg.getCode(), sg);


        Map<String,AssetStock> currentAssetStock = new HashMap<String, AssetStock>();


        RulingSimple ruling = new RulingSimple();
        //ruling.process(currentAssetStock,new AssetProperties("bink", 0, true, 9), INVEST);

        assertThat(currentAssetStock.size()).isEqualTo(2);
        assertThat(currentAssetStock.get("AC").getPriceStopWin()).isEqualTo(69.12*0.946);
    }


    @Test
    public void processStockUPTest() {

        CacheStockGeneral.getCache().clear();

        StockGeneral sg = new StockGeneral();
        sg.setCode("FR0000133308");
        sg.setCodif("ORA");
        sg.setSector("FRFIN");
        sg.setValue(44.72);
        StockPrediction pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.7);
        pred.setYieldD20(4.2);
        sg.setPrediction(pred);
        CacheStockGeneral.getCache().put(sg.getCode(), sg);

        Map<String,AssetStock> curentAssetStock = new HashMap<String, AssetStock>();


        RulingSimple ruling = new RulingSimple();
        //ruling.process(curentAssetStock,new AssetProperties("bink", 0, true, 9), INVEST);

        assertThat(curentAssetStock.size()).isEqualTo(1);
        assertThat(curentAssetStock.get("ORA").getPriceStopWin()).isEqualTo(44.72*1.042);
        assertThat(curentAssetStock.get("ORA").getPriceStopLose()).isEqualTo(44.72*0.95);

    }

    @Test
    public void processStockDownTest() {

        CacheStockGeneral.getCache().clear();

        StockGeneral sg = new StockGeneral();
        sg.setCode("FR0000133308");
        sg.setCodif("ORA");
        sg.setSector("FRFIN");
        sg.setValue(44.72);
        StockPrediction pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.7);
        pred.setYieldD20(-4.2);
        sg.setPrediction(pred);
        CacheStockGeneral.getCache().put(sg.getCode(), sg);

        Map<String,AssetStock> curentAssetStock = new HashMap<String, AssetStock>();


        RulingSimple ruling = new RulingSimple();
        //ruling.process(curentAssetStock,new AssetProperties("bink", 0, true, 9), INVEST);

        assertThat(curentAssetStock.size()).isEqualTo(1);
        assertThat(curentAssetStock.get("ORA").getPriceStopWin()).isEqualTo(44.72*0.958);
        assertThat(curentAssetStock.get("ORA").getPriceStopLose()).isEqualTo(44.72*1.05);

    }

    @Test
    public void processMaxEntryTest() {

        CacheStockGeneral.getCache().clear();

        StockGeneral sg = new StockGeneral();
        sg.setCode("FR0000133308");
        sg.setCodif("ORA");
        sg.setSector("FRFIN");
        sg.setValue(44.72);
        StockPrediction pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.7);
        pred.setYieldD20(4.2);
        sg.setPrediction(pred);

        CacheStockGeneral.getCache().put(sg.getCode(), sg);

        sg = new StockGeneral();
        sg.setCode("FR0000120404");
        sg.setCodif("AC");
        sg.setSector("FRCS");
        sg.setValue(69.12);
        pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.65);
        pred.setYieldD20(-5.4);
        sg.setPrediction(pred);
        CacheStockGeneral.getCache().put(sg.getCode(), sg);


        Map<String,AssetStock> currentAssetStock = new HashMap<String, AssetStock>();


        RulingSimple ruling = new RulingSimple();
        /*limit invest to one part*/
        //ruling.process(currentAssetStock,new AssetProperties("bink", 0, true, 9), 3000);

        assertThat(currentAssetStock.size()).isEqualTo(1);

    }



}
