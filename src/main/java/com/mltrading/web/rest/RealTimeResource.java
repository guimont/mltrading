package com.mltrading.web.rest;

import com.mltrading.models.stock.*;
import com.mltrading.repository.StockRepository;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @javax.inject.Inject
    private StockRepository stockRepository;

    @RequestMapping(value = "/rt/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<StockGeneral> findAll() {

        List<StockGeneral> l = new ArrayList<>(CacheStockGeneral.getCache().values());
        return l;
    }

    @RequestMapping(value = "/rt/sector",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<StockSector> findAllSector() {

        List<StockSector> l = new ArrayList<>(CacheStockSector.getSectorCache().values());
        return l;
    }

    @RequestMapping(value = "/rt/px1",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<StockHistory> findPx1() {

        return StockHistory.getStockHistoryLast("PX1",60);

    }



    @RequestMapping(value = "/rt/detail",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public StockDetail getDetail(@RequestParam(value = "key") String key) {

        Stock s = stockRepository.findOne(key);

        StockDetail detail = StockDetail.populate(s);

        return detail;
    }

}
