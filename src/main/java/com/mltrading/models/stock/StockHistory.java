package com.mltrading.models.stock;



import com.mltrading.dao.Requester;
import com.mltrading.influxdb.dto.QueryRequest;

import com.mltrading.models.stock.cache.CacheStockHistory;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 24/06/2015.
 */
public class StockHistory extends StockBase implements Serializable , Comparable<StockHistory>{

    public static String dbName = "history";

    private String code;

    private String name;

    private String nameBoursier;

    private String place;

    DateTime timeInsert;

    private StockAnalyse analyse_tech;

    private String codif;

    private String placeCodif;

    private int row;

    private static CacheStockHistory cache = CacheStockHistory.CacheStockHistoryHolder();


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

    /**
     * volatility Historic
     */
    private Double volatiltyHist;

    /**
     * volatility Implicite
     */
    private Double volatibiltyImpl;

    private Double consensusNote;

    private StockPrediction prediction;
    private StockPrediction predictionShort;

    private double performanceEstimate;

    public double getPerformanceEstimate() {
        return performanceEstimate;
    }

    public StockPrediction getPredictionShort() {
        return predictionShort;
    }

    public void setPredictionShort(StockPrediction predictionShort) {
        this.predictionShort = predictionShort;
    }

    public void setPerformanceEstimate(double performanceEstimate) {
        this.performanceEstimate = performanceEstimate;
    }


    public StockPrediction getPrediction() {
        return prediction;
    }

    public void setPrediction(StockPrediction prediction) {
        this.prediction = prediction;
    }

    public String getNameBoursier() {
        return nameBoursier;
    }

    public void setNameBoursier(String nameBoursier) {
        this.nameBoursier = nameBoursier;
    }

    @Override
    public int compareTo(StockHistory o) {
        return o.day.compareTo(this.day);
    }






    public StockHistory(StockGeneral g) {
        this.setCode(g.getCode());
        this.setName(g.getName());
        this.setPlace(g.getPlace());
        this.setCodif(g.getCodif());
        this.setPlaceCodif(g.getPlaceCodif());
    }

    public StockHistory() {

        this.consensusNote = 0.;
        this.setHighest(0.);
        this.setLowest(0.);
        this.setOpening(0.);
        this.setValue(0.);
        this.setVolume(0.);

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Double getVolatiltyHist() {
        return volatiltyHist;
    }

    public void setVolatiltyHist(Double volatiltyHist) {
        this.volatiltyHist = volatiltyHist;
    }

    public Double getVolatibiltyImpl() {
        return volatibiltyImpl;
    }

    public void setVolatibiltyImpl(Double volatibiltyImpl) {
        this.volatibiltyImpl = volatibiltyImpl;
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



    public Double getConsensusNote() {
        return consensusNote;
    }

    public void setConsensusNote(Double consensusNote) {
        this.consensusNote = consensusNote;
    }


    /**
     * import date
     * 2010-01-04T09:00:00Z
     * @param day
     */
    public void setDayImport(String day) {
        this.day = day;
        timeInsert = new DateTime( day.substring(0,10));
    }


    /**
     * investir (echos) web site
     * @param day
     */
    public void setDayInvestir(String day) {
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,8);
        this.day = "20" + YY + "-" + MM + "-" + DD;
        timeInsert = new DateTime(  this.day);
    }

    /**
     * invest web site for raw
     * @param day
     */
    public void setDayInvest(String day) {
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,10);
        this.day = YY + "-" + MM + "-" + DD;
        timeInsert = new DateTime( this.day);
    }


    /**
     * yahoo web site
     * @param day
     */
    public void setDayYahoo(String day) {
        String convert[] = day.split(" ");
        String DD = convert[1];
        String MM = convertYahooMont(convert[2]);
        String YY = convert[3];
        this.day = YY + "-" + MM + "-" + DD;
        timeInsert = new DateTime( this.day);
    }

    /**
     * google web site
     * @param day
     */
    public void setDayGoogle(String day) {
        String MM = convertGoolgeMont(day.substring(0, 3));
        String DD = day.substring(4,6).replace(",", "");
        String YY = day.substring(day.length()-4);
        this.day = YY + "-" + MM + "-" + DD;
        timeInsert = new DateTime( this.day );
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

    private String convertGoolgeMont(String yahooMonth) {
        if (yahooMonth.equals("Jan")) return "01";
        if (yahooMonth.equals("Feb")) return "02";
        if (yahooMonth.equals("Mar")) return "03";
        if (yahooMonth.equals("Apr")) return "04";
        if (yahooMonth.equals("May")) return "05";
        if (yahooMonth.equals("Jun")) return "06";
        if (yahooMonth.equals("Jul")) return "07";
        if (yahooMonth.equals("Aug")) return "08";
        if (yahooMonth.equals("Sep")) return "09";
        if (yahooMonth.equals("Oct")) return "10";
        if (yahooMonth.equals("Nov")) return "11";
        if (yahooMonth.equals("Dec")) return "12";

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
        return cache.getStockHistory(code, date);

        /*StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time = '" + date + "'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        sh.setCode(code);
        populate(sh, list, 0);

        //sh.setAnalyse_tech(StockAnalyse.getAnalyse(code, date));

        return sh;*/
    }

    public static StockHistory getStockHistoryDayAfter(final String code, String date) {
        return cache.getStockHistoryDayAfter(code, date);

        /*StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time > '" + date + "' limit 1";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        sh.setCode(code);
        populate(sh, list, 0);

        //sh.setAnalyse_tech(StockAnalyse.getAnalyse(code, date));

        return sh;*/
    }

    public static StockHistory getStockHistoryDayOffset(final String code, String date, int offset) {

        return cache.getStockHistoryDayOffset(code, date, offset);

       /* StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time >= '" + date + "' limit " + offset;
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        if (list == null || list.getResults() == null || list.getResults().get(0).getSeries().get(0) == null)
            return null;

        if (list.getResults().get(0).getSeries().get(0).getValues().size() < offset)
            return null;

        sh.setCode(code);
        populate(sh, list, offset-1);
        return sh;*/
    }

    public static List<StockHistory> getAllHistory(final String code) {
        return cache.getAllStockHistory(code);

    }



    public static List<String> getDateHistoryListOffsetLimit(final String code, int max) {
        return cache.getDateHistoryListOffsetLimit(code, max);
        /*List<String> dateList = new ArrayList<>();
        //bug .. dont get all data .. so make filter to have date only since 2013
        String query = "SELECT * FROM "+code +" where time > '2010-01-01T00:00:00Z'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));
        int series = max + offset;
        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size< series)
            return null;

        for (int i = size-max; i < size; i++) {
            dateList.add((String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0));
        }
        return dateList;*/
    }

    public static String getLastDateHistory(final String code) {

        return cache.getLastDateHistory(code);

        //suppose base is filled
        /*String query = "SELECT * FROM "+code +" where time > '2015-06-01T00:00:00Z'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();


        return (String) list.getResults().get(0).getSeries().get(0).getValues().get(size-1).get(0);*/

    }


    public static List<StockHistory> getStockHistoryDateInvert(final String code, final String date, int offset) {

        return cache.getStockHistoryDateInvert(code, date,offset);
        /*List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+  Integer.toString(offset*4) +"d";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size < offset)
            return null;

        for (int i = size-1; stockList.size() < offset; i--) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;*/

    }


    /**
     * return last max StockHistory
     * @param code
     * @param max
     * @return O or max last StockHistory
     */
    public static List<StockHistory> getStockHistoryLast(final String code, int max) {

        return cache.getStockHistoryLast(code, max);

        /*List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2015-06-01T00:00:00Z'";
        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size < max)
            return null;

        for (int i = size-max; i < size; i++) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;*/

    }


    /**
     * return last max StockHistory
     * @param code
     * @param max
     * @return O or max last StockHistory
     */
    public static List<StockHistory> getStockHistoryLastInvert(final String code, int max) {

        return cache.getStockHistoryLastInvert(code,max);
        /*List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2010-01-01T00:00:00Z'";

        QueryResult list = (QueryResult) Requester.sendRequest(new QueryRequest(query,dbName));
        if (list == null || list.getResults() == null || list.getResults().get(0).getSeries() == null)
            return null;

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        int current = size < max ?  0 : size-max-1;


        for (int i = size-1; i > current ; i--) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;*/

    }


    @Override
    public String toString() {
        return "StockHistory{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", place='" + place + '\'' +
            ", day='" + day + '\'' +
            ", timeInsert=" + timeInsert +
            ", consensus=" + consensusNote +
            ", codif='" + codif + '\'' +
            ", placeCodif='" + placeCodif + '\'' +
            ", value=" + value +
            ", opening=" + opening +
            ", highest=" + highest +
            ", lowest=" + lowest +
            ", volume=" + volume +
            '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        StockHistory copy = new StockHistory();
        copy.setCodif(this.getCodif());
        copy.setName(this.getName());
        copy.setConsensusNote(this.consensusNote);
        copy.setValue(this.value);
        copy.setHighest(this.highest);
        copy.setLowest(this.lowest);
        copy.setVolume(this.volume);
        copy.setOpening(this.opening);
        copy.setAnalyse_tech((StockAnalyse) this.getAnalyse_tech().clone());
        return copy;
    }



}
