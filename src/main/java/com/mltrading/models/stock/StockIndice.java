package com.mltrading.models.stock;

/**
 * Created by gmo on 06/12/2015.
 */
public class StockIndice extends StockHistory {

    public StockIndice(String code, String name) {
        this.setCode(code);
        this.setName(name);
    }
}
