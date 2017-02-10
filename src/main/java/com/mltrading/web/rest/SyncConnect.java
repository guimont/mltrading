package com.mltrading.web.rest;


import com.mltrading.ml.SynchWorker;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;




/**
 * Created by gmo on 10/02/2017.
 */

@RestController
@RequestMapping("/api")
public class SyncConnect {

    @RequestMapping(value = "/subscribe",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    public void addServer(@RequestParam(value = "ip") String ip) {

        SynchWorker.add(ip);

    }

}
