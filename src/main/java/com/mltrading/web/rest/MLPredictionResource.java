package com.mltrading.web.rest;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLPerformance;
import com.mltrading.ml.MLStock;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 01/02/2016.
 */
@RestController
@RequestMapping("/api")
public class MLPredictionResource {

    @RequestMapping(value = "/ml/stat",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<MLPerformance> findAll(@RequestParam(value = "key") String key) {
        return  new ArrayList(CacheMLStock.getMLStockCache().get(key).getMlD1().getPerfList());
    }
}
