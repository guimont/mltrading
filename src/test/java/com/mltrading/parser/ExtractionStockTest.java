package com.mltrading.parser;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.config.MLProperties;
import com.mltrading.models.parser.Analyse;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.ServiceParser;
import com.mltrading.models.stock.StockGeneral;
import org.junit.Test;

/**
 * Created by gmo on 20/03/2017.
 */
public class ExtractionStockTest {

    @Test
    public void testExtractionDaily() {
        MLProperties.load();

        Injector injector = Guice.createInjector(new ServiceParser());
        HistoryParser histParser = injector.getInstance(HistoryParser.class);

        StockGeneral testSg = new StockGeneral("FR0000051732","Atos","ATO","xpar","PA");


        histParser.fetchSpecific(testSg);

        Analyse a = new Analyse();
        try {
            a.processAnalysisAll(testSg.getCodif());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
