package com.mltrading.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.models.parser.*;
import org.springframework.stereotype.Service;
import javax.inject.Inject;

/**
 * Created by gmo on 04/01/2016.
 */
@Service
public class ExtractionService {

    Injector injector = Guice.createInjector(new ServiceParser());
    RealTimeParser rtPrice = injector.getInstance( RealTimeParser.class );
    HistoryParser histParser = injector.getInstance(HistoryParser.class);
    HistoryRawMaterialsParser rawParser = injector.getInstance(HistoryRawMaterialsParser.class);
    HistoryIndiceParser indiceParser= injector.getInstance(HistoryIndiceParser.class);
    HistorySectorParser sectorParser = injector.getInstance(HistorySectorParser.class);
    VolatilityParser vola = injector.getInstance(VolatilityParser.class);



    public void extractFull() {
        histParser.fetch();
        indiceParser.fetch();
        sectorParser.fetch();
        vola.fetch();
        Analyse a = new Analyse();
        a.processAll();
    }


    public void extractionCurrent(String date) {
        //TODO
    }


}