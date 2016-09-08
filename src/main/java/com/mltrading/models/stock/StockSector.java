package com.mltrading.models.stock;



/**
 * Created by gmo on 15/12/2015.
 */
public class StockSector extends StockHistory{


    private String urlInvestir;



    public StockSector(String code, String name, String place, String url, int row) {
        this.setCode(code);
        this.setPlace(place);
        this.setName(name);
        this.setUrlInvestir(url);
        this.setRow(row);
    }


    static public String translate(String investir) {

        return null;
    }


    public void setUrlInvestir(String urlInvestir) {
        this.urlInvestir = urlInvestir;
    }




}
