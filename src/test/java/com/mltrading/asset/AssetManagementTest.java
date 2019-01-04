package com.mltrading.asset;

import com.mltrading.assetmanagement.*;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockPrediction;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AssetManagementTest {

    @Test
    public void assetManagementTest() {
        StockGeneral sg = new StockGeneral();
        sg.setCode("FR0000133308");
        sg.setCodif("ORA");
        sg.setSector("FRFIN");
        sg.setValue(10.);
        StockPrediction pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.7);
        pred.setYieldD20(5);
        sg.setPrediction(pred);


        AssetManagement assetManagement = new AssetManagement(new RulingSimple(),10000);



        AssetStock assetStock = new AssetStock(sg.getCodif(),sg.getSector(),new AssetProperties("bink", 0, true, 9));
        assetManagement.setAssetValue(assetManagement.getAssetValue() - assetStock.buyIt(sg));
        assetManagement.getCurentAssetStock().put(sg.getCodif(), assetStock);


        CacheStockGeneral.getCache().put(sg.getCode(), sg);
        sg.setValue(11.);

        assetManagement.evaluate();
        /* part is 2000 so volume is 2000/price = 100, asset value is 8000 after invest marge is so new price * volume - commission */
        assertThat(assetManagement.getAssetValue()).isEqualTo(8000+11*200-9);
        assertThat(assetManagement.getCurentAssetStock().size()).isEqualTo(0);

    }


    @Test
    public void assetManagementNoChangeTest() {
        StockGeneral sg = new StockGeneral();
        sg.setCode("FR0000133308");
        sg.setCodif("ORA");
        sg.setSector("FRFIN");
        sg.setValue(10.);
        StockPrediction pred = new StockPrediction(sg.getCodif());
        pred.setConfidenceD20(0.7);
        pred.setYieldD20(5);
        sg.setPrediction(pred);


        AssetManagement assetManagement = new AssetManagement(new RulingSimple(),10000);



        AssetStock assetStock = new AssetStock(sg.getCodif(),sg.getSector(),new AssetProperties("bink", 0, true, 9));
        assetManagement.setAssetValue(assetManagement.getAssetValue() - assetStock.buyIt(sg));
        assetManagement.getCurentAssetStock().put(sg.getCodif(), assetStock);


        CacheStockGeneral.getCache().put(sg.getCode(), sg);
        sg.setValue(10.4);

        assetManagement.evaluate();
        /* part is 2000 so volume is 2000/price = 100, asset value is 8000 after invest marge is so new price * volume - commission */
        assertThat(assetManagement.getAssetValue()).isEqualTo(8000);
        assertThat(assetManagement.getCurentAssetStock().size()).isEqualTo(1);

    }

}
