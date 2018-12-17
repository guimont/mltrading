package com.mltrading.web.rest;


import com.mltrading.assetmanagement.AssetManagement;
import com.mltrading.assetmanagement.AssetProperties;
import com.mltrading.assetmanagement.CacheAssetMemory;
import com.mltrading.assetmanagement.Simulation;
import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MatrixValidator;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.model.ModelTypeList;
import com.mltrading.ml.ranking.MLStockRanking;
import com.mltrading.ml.MlForecast;
import com.mltrading.models.stock.CheckConsistency;
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
import java.util.ArrayList;
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


    private static ExtractionService extractionService = new ExtractionService();
    private static ExportService exportService = new ExportService();

    @RequestMapping(value = "/extract",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String extract(@Valid @RequestBody ExtractDTO extDTO) {

        try {


            if (extDTO.getTarget().equalsIgnoreCase(ALL)) {
                if (extDTO.getPeriod() == AUTO) {
                    log.info("Processing perdiod to update");
                    int diff = extractionService.getLastUpdateRef();
                    log.info("Perdiod to update is: " + diff);
                    if (diff > 0)
                        extractionService.extractionCurrent(articleRepository,diff);
                }
                else if (extDTO.getPeriod() == FULL) {
                    extractionService.extractFull(articleRepository);
                } else {
                    extractionService.extractionCurrent(articleRepository,extDTO.getPeriod());
                }
            } else if (extDTO.getTarget().equalsIgnoreCase(SERIES)) {
                if (extDTO.getPeriod() == FULL)
                    extractionService.extractSeriesFull();
                else if (extDTO.getPeriod() == EXPORT)
                    exportService.exportStock();
                else if (extDTO.getPeriod() == IMPORT)
                    exportService.importStock();
                else
                    extractionService.extractSeriesPeriod(extDTO.getPeriod());
            }
            else if (extDTO.getTarget().equalsIgnoreCase(SECTOR)) {
                if (extDTO.getPeriod() == FULL)
                    extractionService.extractSectorFull();
                else if (extDTO.getPeriod() == EXPORT)
                    exportService.exportSector();
                else if (extDTO.getPeriod() == IMPORT)
                    exportService.importSector();
                else
                    extractionService.extractSectorPeriod(extDTO.getPeriod());
            } else if (extDTO.getTarget().equalsIgnoreCase(INDICE)) {
                if (extDTO.getPeriod() == FULL)
                    extractionService.extractIndiceFull();
                else if (extDTO.getPeriod() == EXPORT)
                    exportService.exportIndice();
                else if (extDTO.getPeriod() == IMPORT)
                    exportService.importIndice();
                else
                    extractionService.extractIndicePeriod(extDTO.getPeriod());
            }
            else if (extDTO.getTarget().equalsIgnoreCase(VCAC)) {
                if (extDTO.getPeriod() == FULL)
                    extractionService.extractVcacFull();
                else if (extDTO.getPeriod() == EXPORT)
                    exportService.exportVcac();
                else if (extDTO.getPeriod() == IMPORT)
                    exportService.importVcac();
                else
                    extractionService.extractVcacPeriod(extDTO.getPeriod());
            }

            else if (extDTO.getTarget().equalsIgnoreCase(RAW)) {
                if (extDTO.getPeriod() == FULL)
                    extractionService.extractRawFull("localhost:7090");
                else if (extDTO.getPeriod() == EXPORT)
                    exportService.exportRaw();
                else if (extDTO.getPeriod() == IMPORT)
                    exportService.importRaw();
                else
                    extractionService.extractRawPeriod(extDTO.getPeriod());
            }


            else if (extDTO.getTarget().equalsIgnoreCase(AT)) {
                if (extDTO.getPeriod() == FULL)
                    extractionService.processAT();
                else if (extDTO.getPeriod() == EXPORT)
                    exportService.exportAT();
                else if (extDTO.getPeriod() == IMPORT)
                    exportService.importAT();
                else
                    extractionService.processATPeriod(extDTO.getPeriod());
            }

            else if (extDTO.getTarget().equalsIgnoreCase(DIARY)) {
                if (extDTO.getPeriod() == FULL)
                    extractionService.extractDiaryFull();
                else
                    extractionService.extractDiaryPeriod(extDTO.getPeriod());
            }

            else if (extDTO.getTarget().equalsIgnoreCase(ARTICLE)) {
                if (extDTO.getPeriod() == FULL) {
                    extractionService.extractArticlesFull();
                    extractionService.extractArticleFull(articleRepository);
                } else {
                    extractionService.extractArticlesPeriod();
                    extractionService.extractArticlePeriod(articleRepository);
                }

            }



        } catch (Exception e) {
            log.error(e.toString());
        }





        return "ok";
    }



    @RequestMapping(value = "/extractionAction",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionAction() {
        stockRepository.deleteAll();
        extractionService.extractStock(stockRepository);
        return "ok";
    }

    @RequestMapping(value = "/extractionSpecific",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSpecific() {
        //<stockRepository.deleteAll();

        try {
            extractionService.extractionSpecific("FR0000051732");
        } catch (Exception e) {
            log.error(e.toString());
        }

        return "ok";
    }


    @RequestMapping(value = "/extractionSeries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeries() {
        extractionService.extractFull(articleRepository);
        return "ok";
    }

    @RequestMapping(value = "/extractionSeriesDailly",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeriesDailly() {
        try {

            log.info("Processing perdiod to update");
            int diff = extractionService.getLastUpdateRef();
            log.info("Perdiod to update is: " + diff);
            if (diff > 0)
                extractionService.extractionCurrent(articleRepository,diff);
        } catch (Exception e) {
            log.error(e.toString());
        }

        return "ok";
    }

    @RequestMapping(value = "/extractionSeriesWeekly",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSeriesWeekly() {
        try {
            extractionService.extractionCurrent(articleRepository,5);
        } catch (Exception e) {
            log.error(e.toString());
        }

        return "ok";
    }

    @RequestMapping(value = "/extractionRaw",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionRaw() {

        extractionService.extractRawFull("localhost:7090");
        return "ok";
    }

    @RequestMapping(value = "/extractionVCac",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionVCac() {

        extractionService.extractVcacFull();
        return "ok";
    }



    @RequestMapping(value = "/extractionSector",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionSector() {
        extractionService.extractSectorFull();
        return "ok";
    }

    @RequestMapping(value = "/extractionIndice",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionIndice() {
        extractionService.extractIndiceFull();
        return "ok";
    }





    @RequestMapping(value = "/processAT",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String processAT() {
        try {
            extractionService.processAT();
        } catch (Exception e) {
            log.error(e.toString());
        }

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


        Simulation simulation = new Simulation();
        List<AssetManagement> assetManagementList = CacheAssetMemory.getInstance().getAssetManagementList();
        assetManagementList.clear();

        AssetProperties properties = new AssetProperties("bink", 0, true, 9);
        properties.setPart(10000);
        AssetManagement assetToSim = new AssetManagement(100000,properties);

        AssetManagement assetToSimLess = new AssetManagement(10000);
        assetManagementList.add(assetToSim);
        assetManagementList.add(assetToSimLess);
        simulation.run(assetManagementList);

        simulation.cleanAsset(assetToSim);
        simulation.cleanAsset(assetToSimLess);

        return "ok";
    }


    @RequestMapping(value = "/evaluate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String evaluate() {

        //CacheMLStock.load(); not need !!
        MlForecast ml = new MlForecast();
        /*InfluxDaoConnector.deleteDB(MatrixValidator.dbNameModelPerf);

        ModelTypeList.modelTypes.forEach(t -> {
            ml.processList(t);
            CacheMLStock.savePerf(t);
        });

*/
        ml.updateEnsemble();
        //      CacheMLStock.savePerf(ModelType.ENSEMBLE);
        //      ml.updatePredictor();

        return "ok";
    }



}
