package com.mltrading.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.ml.FeaturesStock;
import com.mltrading.ml.MlForecast;
import com.mltrading.ml.RandomForestStock;
import com.mltrading.models.parser.ServiceParser;
import com.mltrading.models.parser.StockParser;
import com.mltrading.models.parser.impl.CheckConsistency;
import com.mltrading.models.stock.Stock;
import com.mltrading.repository.StockRepository;
import com.mltrading.service.ExtractionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by gmo on 07/01/2016.
 */

@RestController
@RequestMapping("/api")
public class ExtractionResource {

    @javax.inject.Inject
    private StockRepository stockRepository;

    @javax.inject.Inject
    private  MlForecast forecast;


    private static ExtractionService service = new ExtractionService();

    @RequestMapping(value = "/extractionAction",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionAction() {
        service.extractStock(stockRepository);
        return "ok";
    }


    @RequestMapping(value = "/extractionSeries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeries() {
        service.extractFull();
        return "ok";
    }

    @RequestMapping(value = "/extractionSeriesDailly",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeriesDailly() {
        service.extractionCurrent();
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

        List<Stock> sl = stockRepository.findAll();

        forecast.processList(sl);

        return "ok";
    }

    @RequestMapping(value = "/optimizeML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String optimizeML() {

        List<Stock> sl = stockRepository.findAll();

        for (Stock s : sl) {
            forecast.optimizeFeature(s, 100);
        }

        return "ok";
    }


}
