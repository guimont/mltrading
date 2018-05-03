package com.mltrading.models.stock;


import com.mltrading.models.stock.cache.CacheStockIndice;

/**
 * Created by gmo on 06/12/2015.
 */
public class StockIndice extends StockHistory {

    private String urlInvestir;

    public StockIndice(String code, String name,String place,  String url,int row) {
        this.setCode(code);
        this.setCodif(code);
        this.setName(name);
        this.setPlace(place);
        this.setUrlInvestir(url);
        this.setRow(row);
    }

    /**
     * methode to translate INX (s&p500) because key .INX forbidden in database
     * @return
     */
    public String getCodeUrl() {
        if (getCode().equals("INX")) return ".INX";
        return getCode();
    }

    public StockIndice(String code) {
        this.setCode(code);
    }

//TODO update this for real indice
    //probleme with belgium indice
    static public String translate(String investir) {

        for (StockIndice si: CacheStockIndice.getIndiceCache().values()) {
            if ("cac 40".equalsIgnoreCase(si.getName()))
                return si.getCode();
        }

        return null;
    }


    public String getUrlInvestir() {
        return urlInvestir;
    }

    public void setUrlInvestir(String urlInvestir) {
        this.urlInvestir = urlInvestir;
    }
}
