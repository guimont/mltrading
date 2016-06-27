package com.mltrading.web.rest;


import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLPredictor;
import com.mltrading.ml.MlForecast;
import com.mltrading.models.parser.impl.CheckConsistency;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.Stock;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockPrediction;
import com.mltrading.repository.ArticleRepository;
import com.mltrading.repository.StockRepository;
import com.mltrading.service.ExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gmo on 07/01/2016.
 */

@RestController
@RequestMapping("/api")
public class ExtractionResource {

    private static final Logger log = LoggerFactory.getLogger(ExtractionResource.class);

    @javax.inject.Inject
    private StockRepository stockRepository;

    @javax.inject.Inject
    private ArticleRepository articleRepository;

    @javax.inject.Inject
    private  MlForecast forecast;


    private static ExtractionService service = new ExtractionService();

    @RequestMapping(value = "/extractionAction",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionAction() {
        stockRepository.deleteAll();
        service.extractStock(stockRepository);
        return "ok";
    }

    @RequestMapping(value = "/extractionSpecific",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSpecific() {
        stockRepository.deleteAll();
        service.extractionSpecific("FR0000121220");
        return "ok";
    }


    @RequestMapping(value = "/extractionSeries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeries() {
        service.extractFull(articleRepository);
        return "ok";
    }

    @RequestMapping(value = "/extractionSeriesDailly",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeriesDailly() {
        log.info("Processing perdiod to update");
        int diff = service.getLastUpdateRef();
        log.info("Perdiod to update is: " + diff);
        if (diff > 0)
            service.extractionCurrent(diff);
        return "ok";
    }

    @RequestMapping(value = "/extractionSeriesWeekly",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeriesWeekly() {
        service.extractionCurrent(5);
        return "ok";
    }

    @RequestMapping(value = "/extractionRaw",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionRaw() {

        service.extractRawFull("localhost:8090");
        return "ok";
    }

    @RequestMapping(value = "/extractionVCac",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionVCac() {

        service.extractVcacFull();
        return "ok";
    }



    @RequestMapping(value = "/extractionSector",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSector() {
        service.extractSectorFull();
        return "ok";
    }

    @RequestMapping(value = "/extractionIndice",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionIndice() {
        service.extractIndiceFull();
        return "ok";
    }





    @RequestMapping(value = "/processAT",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String processAT() {
        service.processAT();
        return "ok";
    }





    @RequestMapping(value = "/checkML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkProcessML() {

        List<Stock> sl = stockRepository.findAll();

        for (Stock s : sl) {
            CheckConsistency.consistency(s);
        }

        return "ok";
    }


    @RequestMapping(value = "/processML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String processML() {

        forecast.processList(new ArrayList(CacheStockGeneral.getIsinCache().values()));

        return "ok";
    }

    @RequestMapping(value = "/optimizeML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String optimizeML() {

        int loop = 1;

        for (int i = 0 ; i < loop; i ++) {
            for (StockGeneral s : CacheStockGeneral.getIsinCache().values()) {
                if (s.getCodif().equals("ORA"))
                    forecast.optimize(s, 2, MlForecast.Method.RandomForest, MlForecast.Type.Feature);
            }
            /*for (StockGeneral s : CacheStockGeneral.getIsinCache().values()) {
                if (s.getCodif().equals("ORA"))
                    forecast.optimize(s, 10, MlForecast.Method.RandomForest, MlForecast.Type.RF);
            }*/

            log.info("evaluate");
            evaluate();
            log.info("saveML");
            saveML();
        }



        return "ok";
    }

    @RequestMapping(value = "/defaultML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String defaultML() {


        for (StockGeneral s : CacheStockGeneral.getIsinCache().values()) {
            if (s.getCodif().equals("ORA"))
                forecast.optimize(s, 1, MlForecast.Method.RandomForest, MlForecast.Type.None);
        }

        evaluate();
        saveML();

        return "ok";
    }


    @RequestMapping(value = "/optimizeMLLR",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String optimizeMLLR() {

        int loop = 5;

        for (int i = 0 ; i < loop; i ++)
            for (StockGeneral s : CacheStockGeneral.getIsinCache().values()) {
                forecast.optimize(s, loop, MlForecast.Method.LinearRegression, MlForecast.Type.Feature);
            }

        return "ok";
    }



    @RequestMapping(value = "/saveML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveML() {

        List<Stock> sl = stockRepository.findAll();
        CacheMLStock.save();

        return "ok";
    }


    @RequestMapping(value = "/loadML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadML() {

        List<StockGeneral> sl = new ArrayList(CacheStockGeneral.getIsinCache().values());

        CacheMLStock.load(sl);
        MlForecast ml = new MlForecast();
        ml.processList(sl);

        return "ok";
    }


    @RequestMapping(value = "/evaluate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String evaluate() {

        MLPredictor predictor = new MLPredictor();

        for (StockGeneral s: CacheStockGeneral.getCache().values()) {
            StockPrediction p = predictor.prediction(s);
            s.setPrediction(p);
        }


        return "ok";
    }



}
