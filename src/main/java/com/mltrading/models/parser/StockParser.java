package com.mltrading.models.parser;

import com.mltrading.repository.StockRepository;

/**
 * Created by gmo on 05/01/2016.
 */
public interface StockParser {

    void fetch(StockRepository repository);
}
