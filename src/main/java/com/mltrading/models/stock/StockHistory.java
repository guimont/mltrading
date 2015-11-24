package com.mltrading.models.stock;


import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 24/06/2015.
 */
public class StockHistory {

    private String code;

    private String name;

    private String place;

    private String day;

    DateTime timeInsert;

    private StockAnalyse analyse_tech;

    private String codif;

    private String placeCodif;


    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public String getPlaceCodif() {
        return placeCodif;
    }

    public void setPlaceCodif(String placeCodif) {
        this.placeCodif = placeCodif;
    }

    /**
     * last price of stock
     */
    private Double value;

    /**
     * opening price
     */
    private Double opening;

    /**
     * highest price
     */

    private Double highest;

    /**
     * lowest price
     */
    private Double lowest;

    /**
     * volume
     */
    private Double volume;

    public StockHistory(StockGeneral g) {
        this.setCode(g.getCode());
        this.setName(g.getName());
        this.setPlace(g.getPlace());
        this.setCodif(g.getCodif());
        this.setPlaceCodif(g.getPlaceCodif());
    }

    public StockHistory() {

    }



    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getOpening() {
        return opening;
    }

    public void setOpening(Double opening) {
        this.opening = opening;
    }

    public Double getHighest() {
        return highest;
    }

    public void setHighest(Double highest) {
        this.highest = highest;
    }

    public Double getLowest() {
        return lowest;
    }

    public void setLowest(Double lowest) {
        this.lowest = lowest;
    }


    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {

        this.day = day;
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,8);
        timeInsert = new DateTime( "20" + YY + "-" + MM + "-" + DD);
    }

    public void setDayYahoo(String day) {
        String convert[] = day.split(" ");
        String DD = convert[1];
        String MM = convertYahooMont(convert[2]);
        String YY = convert[3];
        this.day = YY + "-" + MM + "-" + DD;
        timeInsert = new DateTime( YY + "-" + MM + "-" + DD);
    }


    private String convertYahooMont(String yahooMonth) {
        if (yahooMonth.equals("janv.")) return "01";
        if (yahooMonth.equals("févr.")) return "02";
        if (yahooMonth.equals("mars")) return "03";
        if (yahooMonth.equals("avr.")) return "04";
        if (yahooMonth.equals("mai")) return "05";
        if (yahooMonth.equals("juin")) return "06";
        if (yahooMonth.equals("juil.")) return "07";
        if (yahooMonth.equals("août")) return "08";
        if (yahooMonth.equals("sept.")) return "09";
        if (yahooMonth.equals("oct.")) return "10";
        if (yahooMonth.equals("nov.")) return "11";
        if (yahooMonth.equals("déc.")) return "12";

        return null;
    }

    public DateTime getTimeInsert() {
        return timeInsert;
    }


    public StockAnalyse getAnalyse_tech() {
        return analyse_tech;
    }

    public void setAnalyse_tech(StockAnalyse analyse_tech) {
        this.analyse_tech = analyse_tech;
    }

    public static StockHistory getStockHistory(final String code, String date) {
        StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time = '" + date + "'";
        QueryResult meanQ = InfluxDaoConnector.getPoints(query);

        sh.setCode(code);
        sh.setHighest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1));
        sh.setLowest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(2));
        sh.setOpening((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(3));
        sh.setValue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(4));
        sh.setVolume((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(5));

        sh.setAnalyse_tech(StockAnalyse.getAnalyse(code, date));

        return sh;
    }

    public static List<StockHistory> getStockHistoryList(final String code) {
        List<StockHistory> stockList = new ArrayList<StockHistory>();
        String query = "SELECT * FROM "+code+ "T";
        QueryResult list = InfluxDaoConnector.getPoints(query);

        for (int i =0;i<list.getResults().get(0).getSeries().get(0).getValues().size();i++)
            stockList.add(getStockHistory(code, (String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0)));


        return stockList;
    }


    @Override
    public String toString() {
        return "StockHistory{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", place='" + place + '\'' +
            ", day='" + day + '\'' +
            ", timeInsert=" + timeInsert +
            ", analyse_tech=" + analyse_tech +
            ", codif='" + codif + '\'' +
            ", placeCodif='" + placeCodif + '\'' +
            ", value=" + value +
            ", opening=" + opening +
            ", highest=" + highest +
            ", lowest=" + lowest +
            ", volume=" + volume +
            '}';
    }
}
