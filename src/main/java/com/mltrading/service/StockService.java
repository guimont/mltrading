package com.mltrading.service;

import com.mltrading.models.stock.Stock;
import com.mltrading.repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by gmo on 05/01/2016.
 */
@Service
public class StockService {

    @Inject
    private StockRepository stockRepository;

    public void createStock(Stock stock) {

        stockRepository.save(stock);
    }

}
