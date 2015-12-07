package com.mltrading.models.stock;

import java.util.List;

/**
 * Created by gmo on 06/12/2015.
 */
public class Stock extends StockHistory {

    private Bilan bilan;
    private Company company;
    private List<String> indice;
    private String sector;
    private List<StockRawMat> rawMat;



    public Bilan getBilan() {
        return bilan;
    }

    public void setBilan(Bilan bilan) {
        this.bilan = bilan;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<String> getIndice() {
        return indice;
    }

    public void setIndice(List<String> indice) {
        this.indice = indice;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public List<StockRawMat> getRawMat() {
        return rawMat;
    }

    public void setRawMat(List<StockRawMat> rawMat) {
        this.rawMat = rawMat;
    }
}
