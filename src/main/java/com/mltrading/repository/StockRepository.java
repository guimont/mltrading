package com.mltrading.repository;

import com.mltrading.models.stock.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by gmo on 05/01/2016.
 */
public interface StockRepository extends MongoRepository<Stock, String> {

}
