package com.mltrading.models.stock;


import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.QueryResult;
import org.joda.time.DateTime;

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

    /**
     * last price of stock
     */
    private Float value;

    /**
     * opening price
     */
    private Float opening;

    /**
     * highest price
     */

    private Float highest;

    /**
     * lowest price
     */
    private Float lowest;

    /**
     * volume
     */
    private Integer volume;

    public StockHistory() {

    }


    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getOpening() {
        return opening;
    }

    public void setOpening(Float opening) {
        this.opening = opening;
    }

    public Float getHighest() {
        return highest;
    }

    public void setHighest(Float highest) {
        this.highest = highest;
    }

    public Float getLowest() {
        return lowest;
    }

    public void setLowest(Float lowest) {
        this.lowest = lowest;
    }


    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
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


        /*a.setMma20((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(1));
        a.setMma50((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(2));
        a.setMme12((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(3));
        a.setMme26((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(4));
        a.setMomentum((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(5));
        a.setStdDev((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(0).get(6));*/


        sh.setAnalyse_tech(StockAnalyse.getAnalyse(code, date));


        return sh;
    }
}
