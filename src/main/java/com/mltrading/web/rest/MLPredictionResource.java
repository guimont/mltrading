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
                if (mlPerformances.getMlD5() != null && mlPerformances.getMlD5().getValue() ==0.) mlPerformances.setMlD5(null);
                if (mlPerformances.getMlD20() != null && mlPerformances.getMlD20().getValue() ==0.) mlPerformances.setMlD20(null);
                if (mlPerformances.getMlD40() != null && mlPerformances.getMlD40().getValue() ==0.) mlPerformances.setMlD40(null);
            }

            return l;
        } else
            return null;

    }



}
