package com.mltrading.asset;

import com.mltrading.assetmanagement.AssetManagement;
import com.mltrading.assetmanagement.RulingSimple;
import com.mltrading.assetmanagement.Simulation;
import com.mltrading.config.MLProperties;
import org.junit.Test;

public class SimulationTest {

    @Test
    public void processTest() {
        MLProperties.load();
        Simulation sim = new Simulation();
        AssetManagement assetManagement = new AssetManagement(new RulingSimple(),10000);
    }

}
