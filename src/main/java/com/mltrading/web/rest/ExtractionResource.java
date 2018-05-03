package com.mltrading.web.rest;


import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.ranking.MLStockRanking;
import com.mltrading.ml.MlForecast;
import com.mltrading.models.stock.CheckConsistency;
import com.mltrading.models.stock.Stock;
import com.mltrading.repository.ArticleRepository;
import com.mltrading.repository.StockRepository;
import com.mltrading.service.ExportService;
import com.mltrading.service.ExtractionService;
import com.mltrading.web.rest.dto.ExtractDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by gmo on 07/01/2016.
 */
@RestController
@RequestMapping("/api")
public class ExtractionResource {

    private static final Logger log = LoggerFactory.getLogger(ExtractionResource.class);
    private static final String DIARY = "Diary";
    private static String ALL = "Full";
    private static String SERIES = "Series";
    private static String RAW = "Raw";
    private static String AT = "At";
    private static String SECTOR = "Sector";
    private static String INDICE = "Indice";
    private static String ARTICLE = "Article";
    private static String VCAC = "Vcac";

    public static int AUTO = 0;
    public static int FULL = -1;
    public static int EXPORT = -2;
    public static int IMPORT = -3;


    @javax.inject.Inject
    private StockRepository stockRepository;

    @javax.inject.Inject
    private ArticleRepository articleRepository;

    @javax.inject.Inject
    private  MlForecast forecast;


    @javax.inject.Inject
    private MLStockRanking ranking;


    private static ExtractionService service = new ExtractionService();
    private static ExportService export = new ExportService();

    @RequestMapping(value = "/extract",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String extract(@Valid @RequestBody ExtractDTO extDTO) {

        if (extDTO.getTarget().equalsIgnoreCase(ALL)) {
            if (extDTO.getPeriod() == AUTO) {
                log.info("Processing perdiod to update");
                int diff = service.getLastUpdateRef();
                log.info("Perdiod to update is: " + diff);
                if (diff > 0)
                    service.extractionCurrent(articleRepository,diff);
            }
            else if (extDTO.getPeriod() == FULL) {
                service.extractFull(articleRepository);
            } else {
                service.extractionCurrent(articleRepository,extDTO.getPeriod());
            }
        } else if (extDTO.getTarget().equalsIgnoreCase(SERIES)) {
            if (extDTO.getPeriod() == FULL)
                service.extractSeriesFull();
            else if (extDTO.getPeriod() == EXPORT)
                export.exportStock();
            else if (extDTO.getPeriod() == IMPORT)
                export.importStock();
            else
                service.extractSeriesPeriod(extDTO.getPeriod());
        }
        else if (extDTO.getTarget().equalsIgnoreCase(SECTOR)) {
            if (extDTO.getPeriod() == FULL)
                service.extractSectorFull();
            else if (extDTO.getPeriod() == EXPORT)
                export.exportSector();
            else if (extDTO.getPeriod() == IMPORT)
                export.importSector();
            else
                service.extractSectorPeriod(extDTO.getPeriod());
        } else if (extDTO.getTarget().equalsIgnoreCase(INDICE)) {
            if (extDTO.getPeriod() == FULL)
                service.extractIndiceFull();
            else if (extDTO.getPeriod() == EXPORT)
                export.exportIndice();
            else if (extDTO.getPeriod() == IMPORT)
                export.importIndice();
            else
                service.extractIndicePeriod(extDTO.getPeriod());
        }
        else if (extDTO.getTarget().equalsIgnoreCase(VCAC)) {
            if (extDTO.getPeriod() == FULL)
                service.extractVcacFull();
            else if (extDTO.getPeriod() == EXPORT)
                export.exportVcac();
            else if (extDTO.getPeriod() == IMPORT)
                export.importVcac();
            else
                service.extractVcacPeriod(extDTO.getPeriod());
        }

        else if (extDTO.getTarget().equalsIgnoreCase(RAW)) {
            if (extDTO.getPeriod() == FULL)
                service.extractRawFull("localhost:7090");
            else if (extDTO.getPeriod() == EXPORT)
                export.exportRaw();
            else if (extDTO.getPeriod() == IMPORT)
                export.importRaw();
            else
                service.extractRawPeriod(extDTO.getPeriod());
        }


        else if (extDTO.getTarget().equalsIgnoreCase(AT)) {
            if (extDTO.getPeriod() == FULL)
                service.processAT();
            else
                service.processATPeriod(extDTO.getPeriod());
        }

        else if (extDTO.getTarget().equalsIgnoreCase(DIARY)) {
            if (extDTO.getPeriod() == FULL)
                service.extractDiaryFull();
            else
                service.extractDiaryPeriod(extDTO.getPeriod());
        }

        else if (extDTO.getTarget().equalsIgnoreCase(ARTICLE)) {
            if (extDTO.getPeriod() == FULL) {
                service.extractArticlesFull();
                service.extractArticleFull(articleRepository);
            } else {
                service.extractArticlesPeriod();
                service.extractArticlePeriod(articleRepository);
            }

        }









            return "ok";
    }



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
        //<stockRepository.deleteAll();
        service.extractionSpecific("FR0000051732");
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
            service.extractionCurrent(articleRepository,diff);
        return "ok";
    }

    @RequestMapping(value = "/extractionSeriesWeekly",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeriesWeekly() {
        service.extractionCurrent(articleRepository,5);
        return "ok";
    }

    @RequestMapping(value = "/extractionRaw",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionRaw() {

        service.extractRawFull("localhost:7090");
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

        CheckConsistency.consistency();


        return "ok";
    }


    @RequestMapping(value = "/processML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String processML() {

        ranking.optimize();

        return "ok";
    }



    @RequestMapping(value = "/defaultML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String defaultML() {


        return "ok";
    }


    @RequestMapping(value = "/optimizeMLLR",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String optimizeMLLR() {


        return "ok";
    }



    @RequestMapping(value = "/saveML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveML() {



        return "ok";
    }


    @RequestMapping(value = "/loadML",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadML() {


        CacheMLStock.load();
        MlForecast ml = new MlForecast();
        ml.processList(ModelType.RANDOMFOREST);

        return "ok";
    }


    @RequestMapping(value = "/evaluate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String evaluate() {

        MlForecast.updatePredictor(ModelType.RANDOMFOREST);


        return "ok";
    }



}
