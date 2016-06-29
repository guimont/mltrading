package com.mltrading.web.rest;


import com.mltrading.models.stock.DatabaseInfoList;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;


/**
 * Created by gmo on 29/06/2016.
 */


@RestController
@RequestMapping("/api")
public class DatabaseResource {

    @RequestMapping(value = "/database/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public DatabaseInfoList findAll() {

        return new DatabaseInfoList().processingList();
    }

}
