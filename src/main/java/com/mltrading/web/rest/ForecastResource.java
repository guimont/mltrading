package com.mltrading.web.rest;

import com.mltrading.ml.CacheMLActivities;
import com.mltrading.ml.MlForecast;
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

    @javax.inject.Inject
    private MlForecast forecast;

    @RequestMapping(value = "/MLActivities/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<MLActivities> findAll() {

        return CacheMLActivities.getActivities();
    }


    @RequestMapping(value = "/MLActivities/count",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    public long count() {

        return CacheMLActivities.getCountGlobal();
    }


    @RequestMapping(value = "/optimizeML",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String optimizeML(@Valid @RequestBody ForecastDTO fcDTO) {

        if (fcDTO.getValidator().contains("optimizeModel"))
            forecast.optimizeModel();
        else
            forecast.optimize(fcDTO.getGlobalLoop(), fcDTO.getInputLoop(), fcDTO.getValidator(), fcDTO.getTarget());

        return "ok";
    }

}
