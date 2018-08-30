package com.mltrading.web.rest;

import com.mltrading.ml.*;

import com.mltrading.ml.model.ModelType;
import com.mltrading.models.stock.StockValidator;
import com.mltrading.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gmo on 01/02/2016.
 */
@RestController
@RequestMapping("/api")
public class MLPredictionResource {

    private static final Logger log = LoggerFactory.getLogger(MLPredictionResource.class);

    @RequestMapping(value = "/ml/getPerformance",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)

    public MLStatus getPerformance(@RequestParam(value = "key") String key, @RequestParam(value = "model") String model) {

        MLStocks ms = CacheMLStock.getMLStockCache().get(key);
        if (ms != null) {
            MLStatus l = ms.getStatus(ModelType.get(model)).clone();
            List pList = l.getPerfList();
            try {
                Collections.sort(pList);
            }catch (Exception e) {
                log.error(e.getMessage());
            }

            for (MLPerformances mlPerformances : l.getPerfList()) {

                if (mlPerformances.getMl(PredictionPeriodicity.D1).getRealvalue() ==0.)  mlPerformances.setMl(null, PredictionPeriodicity.D1);
                if (mlPerformances.getMl(PredictionPeriodicity.D5) != null && mlPerformances.getMl(PredictionPeriodicity.D5).getRealvalue() ==0.) mlPerformances.setMl(null, PredictionPeriodicity.D5);
                if (mlPerformances.getMl(PredictionPeriodicity.D20) != null && mlPerformances.getMl(PredictionPeriodicity.D20).getRealvalue() ==0.) mlPerformances.setMl(null, PredictionPeriodicity.D20);
                if (mlPerformances.getMl(PredictionPeriodicity.D40) != null && mlPerformances.getMl(PredictionPeriodicity.D40).getRealvalue() ==0.) mlPerformances.setMl(null, PredictionPeriodicity.D40);

                mlPerformances.convertUI();
            }

            return l;
        } else
            return null;
    }

    @RequestMapping(value = "/ml/getValidator",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)

    public List<StockValidator> getValidator(@RequestParam(value = "key") String key) {

        MLStocks ms = CacheMLStock.getMLStockCache().get(key);
        if (ms != null) {
           // Map<PredictionPeriodicity, MatrixValidator> mvList = ms.getModel().getValidators();
            //List<StockValidator>  res = mvList.entrySet().stream().map(p -> new StockValidator().filled(key, p.getKey(),p.getCurrentValue())).collect(Collectors.toList());
            //return res;

            return ms.getValidators(ModelType.RANDOMFOREST).entrySet().stream()
                .map(p -> new StockValidator().filled(key, p.getKey(), p.getValue()))
                .collect(Collectors.toList());
        } else
            return null;
    }


}
