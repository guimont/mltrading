package com.mltrading.web.rest;

import com.mltrading.ml.CacheMLActivities;
import com.mltrading.models.util.MLActivities;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * Created by gmo on 10/02/2017.
 */

@RestController
@RequestMapping("/api")
public class ForecastResource {

    @RequestMapping(value = "/MLActivities/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<MLActivities> findAll() {

        return CacheMLActivities.getActivities();
    }

}
