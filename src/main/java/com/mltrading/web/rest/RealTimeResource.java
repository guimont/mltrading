package com.mltrading.web.rest;

import akka.japi.Pair;
import com.mltrading.models.stock.*;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.repository.StockRepository;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Comparator;
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
    @RolesAllowed(AuthoritiesConstants.USER)
    public List<StockGeneral> findAll() {

        List<StockGeneral> l = new ArrayList<>(CacheStockGeneral.getCache().values());
        return l;
    }

    @RequestMapping(value = "/rt/sector",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.USER)
    public List<StockSector> findAllSector() {

        List<StockSector> l = new ArrayList<>(CacheStockSector.getSectorCache().values());
        return l;
    }

    @RequestMapping(value = "/rt/px1",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.USER)
    public List<StockHistory> findPx1() {

        return StockHistory.getStockHistoryLast("PX1", 60);

    }

    @RequestMapping(value = "/rt/selected",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.USER)
    public List<StockDetail> findSelected() {

        List<StockDetail> output = new ArrayList<>();

        List<StockGeneral> l = new ArrayList<>(CacheStockGeneral.getCache().values());

        List<Pair<Double, String>> predState = new ArrayList();
        //
        l.forEach(s -> {
            if (s.getPrediction() != null)
                predState.add(new Pair<>((Math.abs(s.getPrediction().getYieldD20()*s.getPrediction().getConfidenceD20())), s.getRealCodif()));
        });

        predState.sort(Comparator.comparingDouble(Pair::first));

        int size = predState.size();
        if (size > 5) {
            for (int i = 1; i< 5; i++) {
                StockGeneral sg = CacheStockGeneral.getCache().get(CacheStockGeneral.getCode(predState.get(size - i).second()));
                StockDetail detail = StockDetail.populateLight(sg);
                output.add(detail);
            }
        }

        return output;

    }



    @RequestMapping(value = "/rt/detail",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.USER)
    public StockDetail getDetail(@RequestParam(value = "key") String key) {

        Stock s = stockRepository.findOne(key);



        StockHistory sh;
        if (CacheStockGeneral.getIsinCache().get( CacheStockGeneral.getCode(key)) != null ) sh = CacheStockGeneral.getIsinCache().get( CacheStockGeneral.getCode(key));
        else sh = CacheStockSector.getSectorCache().get(key);

        StockDetail detail = StockDetail.populate(s,sh);

        return detail;
    }



}
