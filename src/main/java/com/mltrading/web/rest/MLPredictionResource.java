package com.mltrading.web.rest;

import com.mltrading.ml.*;

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

/**
 * Created by gmo on 01/02/2016.
 */
@RestController
@RequestMapping("/api")
public class MLPredictionResource {

    private static final Logger log = LoggerFactory.getLogger(MLPredictionResource.class);

    @RequestMapping(value = "/ml/stat",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)

    public MLStatus findAll(@RequestParam(value = "key") String key) {

        MLStocks ms = CacheMLStock.getMLStockCache().get(key);
        if (ms != null) {
            MLStatus l = ms.getStatus().clone();
            List pList = l.getPerfList();
            try {
                Collections.sort(pList);
            }catch (Exception e) {
                log.error(e.getMessage());
            }

            for (MLPerformances mlPerformances : l.getPerfList()) {
                if (mlPerformances.getMl(PredictionPeriodicity.D5) != null && mlPerformances.getMl(PredictionPeriodicity.D5).getValue() ==0.) mlPerformances.setMl(null, PredictionPeriodicity.D5);
                if (mlPerformances.getMl(PredictionPeriodicity.D20) != null && mlPerformances.getMl(PredictionPeriodicity.D20).getValue() ==0.) mlPerformances.setMl(null, PredictionPeriodicity.D20);
                if (mlPerformances.getMl(PredictionPeriodicity.D40) != null && mlPerformances.getMl(PredictionPeriodicity.D40).getValue() ==0.) mlPerformances.setMl(null, PredictionPeriodicity.D40);
            }

            return l;
        } else
            return null;

    }



}
