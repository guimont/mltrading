package com.mltrading.models.stock;



/**
 * Created by gmo on 15/12/2015.
 */
public class StockSector extends StockHistory{

    /**
     * current variation with opening price
     */
    private Float variation;

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


    public Float getVariation() {
        return variation;
    }

    public void setVariation(Float variation) {
        this.variation = variation;
    }
}
