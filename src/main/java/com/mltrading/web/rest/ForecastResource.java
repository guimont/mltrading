package com.mltrading.web.rest;

import com.mltrading.ml.CacheMLActivities;
import com.mltrading.ml.MlForecast;

import com.mltrading.models.stock.StockPerformanceList;
import com.mltrading.models.util.MLActivities;
import com.mltrading.security.AuthoritiesConstants;
import com.mltrading.web.rest.dto.ForecastDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by gmo on 10/02/2017.
 */

@RestController
@RequestMapping("/api")
public class ForecastResource {


    @RequestMapping(value = "/ML/Activities/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<MLActivities> findAll() {

        return CacheMLActivities.getActivities();
    }


    @RequestMapping(value = "/ML/Activities/count",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    public long count() {

        return CacheMLActivities.getCountGlobal();
    }


    @RequestMapping(value = "/ML/optimize",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String optimizeML(@Valid @RequestBody ForecastDTO fcDTO) {

        MlForecast forecast = new MlForecast(fcDTO);
        forecast.optimize();

        return "ok";
    }

    @RequestMapping(value = "/ML/export",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public void exportMatrixValidator() {
        MlForecast forecast = new MlForecast(null);
        forecast.exportModel();
    }

    @RequestMapping(value = "/ML/import",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public void importMatrixValidator() {
        MlForecast forecast = new MlForecast(null);
        forecast.importModel();
    }


    @RequestMapping(value = "/ML/resume",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public StockPerformanceList resumePerformance() {

        StockPerformanceList l =new StockPerformanceList().processingList();

        return l;
    }

}
