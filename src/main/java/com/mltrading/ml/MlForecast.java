package com.mltrading.ml;

import com.google.inject.Inject;
import com.mltrading.models.stock.Stock;
import com.mltrading.repository.StockRepository;

import java.util.List;

/**
 * Created by gmo on 08/01/2016.
 */
public class MlForecast {

    @Inject
    private StockRepository stockRepository;

    public void processList() {

        List<Stock> l = stockRepository.findAll();

        for (Stock s : l) {


        }

    }

}
