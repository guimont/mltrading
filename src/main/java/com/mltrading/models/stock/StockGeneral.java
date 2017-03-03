package com.mltrading.models.stock;

import java.io.Serializable;

/**
 * Created by gmo on 17/06/2015.
 */
public class StockGeneral extends StockHistory implements Serializable {




    private String sector;
    private Double variation;
    private Double firstJanuaryVariation;




    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }


    public String getRealCodif() {
        if (getCodif().equalsIgnoreCase("OREAL"))
            return "OR";
        return getCodif();
    }


    public StockGeneral() {
    }

    public StockGeneral(String code, String name,  String codif, String place, String placeCodif) {
        this.setCode(code);
        this.setName(name);
        this.setPlace(place);
        this.setCodif(codif);
        this.setPlaceCodif(placeCodif);
    }
    public StockGeneral(String code, String name,  String codif, String place, String placeCodif, String sector, String subsector) {
        this.setCode(code);
        this.setName(name);
        this.setPlace(place);
        this.setCodif(codif);
        this.setPlaceCodif(placeCodif);
        this.setSector(sector);
    }

    public StockGeneral(Double value, Double variation, Double opening, Double highest, Double lowest, Double firstJanuaryVaraition, Double volume) {

        this.setValue(value);
        this.setVariation(variation);
        this.setOpening(opening);
        this.setHighest(highest);
        this.setLowest(lowest);
        this.setVolume(volume);
        this.setFirstJanuaryVariation(firstJanuaryVaraition);

    }


    public void setVariation(Double variation) {
        this.variation = variation;
    }

    public Double getVariation() {
        return variation;
    }

    public void setFirstJanuaryVariation(Double firstJanuaryVariation) {
        this.firstJanuaryVariation = firstJanuaryVariation;
    }

    public Double getFirstJanuaryVariation() {
        return firstJanuaryVariation;
    }
}
