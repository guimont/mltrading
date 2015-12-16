package com.mltrading.models.stock;

/**
 * Created by gmo on 15/12/2015.
 */
public class StockSector extends StockHistory{

    public StockSector(String code, String name, String place) {
        this.setCode(code);
        this.setPlace(place);
        this.setName(name);
    }
}
