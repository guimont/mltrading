package com.mltrading.web.rest;

import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 20/01/2016.
 */
@RestController
@RequestMapping("/api")
public class RealTimeResource {

    @RequestMapping(value = "/rt/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<StockGeneral> findAll() {

        List<StockGeneral> l = new ArrayList<>(CacheStockGeneral.getCache().values());
        return l;
    }

}
