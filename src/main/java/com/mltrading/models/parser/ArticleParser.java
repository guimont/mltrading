package com.mltrading.models.parser;


import com.mltrading.models.stock.StockGeneral;
import com.mltrading.repository.ArticleRepository;


/**
 * Created by gmo on 09/03/2016.
 */
public interface ArticleParser {

    void fetch(ArticleRepository repository);

    public void fetchCurrent(ArticleRepository repository);

    public void fetchSpecific(ArticleRepository repository, StockGeneral g);




}
