package com.mltrading.models.stock;



import com.mltrading.dao.Requester;
import com.mltrading.influxdb.dto.QueryRequest;
import com.mltrading.influxdb.dto.QueryResult;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 24/06/2015.
 */
public class StockHistory extends Object implements Serializable{

    public static String dbName = "history";

    private String code;

    private String name;

    private String place;

    protected String day;

    DateTime timeInsert;

    private StockAnalyse analyse_tech;

    private String codif;

    private String placeCodif;

    private int row;


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

    public String getDay() {
        return day;
    }


    public Double getConsensusNote() {
        return consensusNote;
    }

    public void setConsensusNote(Double consensusNote) {
        this.consensusNote = consensusNote;
    }


    public void setDay(String day) {
        this.day = day;
    }


    /**
     * investir (echos) web site
     * @param day
     */
    public void setDayInvestir(String day) {
        this.day = day;
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,8);
        timeInsert = new DateTime( "20" + YY + "-" + MM + "-" + DD);
    }

    /**
     * invest web site for raw
     * @param day
     */
    public void setDayInvest(String day) {

        this.day = day;
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,10);
        timeInsert = new DateTime( YY + "-" + MM + "-" + DD);
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
        timeInsert = new DateTime( YY + "-" + MM + "-" + DD);
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

    static public int DATE_COLUMN = 0;
    static public int CONSENSUS_COLUMN_HIST = 1;
    static public int HIGHEST_COLUMN_HIST = 2;
    static public int LOWEST_COLUMN_HIST = 3;
    static public int OPENING_COLUMN_HIST = 4;
    static public int VALUE_COLUMN_HIST = 5;
    static public int VOLUME_COLUMN_HIST = 6;

    public static void populate(StockHistory sh, QueryResult meanQ, int i) {
        sh.setDay((String) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(DATE_COLUMN));
        sh.setConsensusNote((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(CONSENSUS_COLUMN_HIST));
        sh.setHighest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(HIGHEST_COLUMN_HIST));
        sh.setLowest((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(LOWEST_COLUMN_HIST));
        sh.setOpening((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(OPENING_COLUMN_HIST));
        sh.setValue((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VALUE_COLUMN_HIST));
        sh.setVolume((Double) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(VOLUME_COLUMN_HIST));
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
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        sh.setCode(code);
        populate(sh, list, 0);

        //sh.setAnalyse_tech(StockAnalyse.getAnalyse(code, date));

        return sh;
    }

    public static StockHistory getStockHistoryDayAfter(final String code, String date) {
        StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time > '" + date + "' limit 1";
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        sh.setCode(code);
        populate(sh, list, 0);

        //sh.setAnalyse_tech(StockAnalyse.getAnalyse(code, date));

        return sh;
    }

    public static StockHistory getStockHistoryDayOffset(final String code, String date, int offset) {
        StockHistory sh = new StockHistory();

        String query = "SELECT * FROM "+code+" where time >= '" + date + "' limit " + offset;
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        if (list == null || list.getResults() == null || list.getResults().get(0).getSeries().get(0) == null)
            return null;

        if (list.getResults().get(0).getSeries().get(0).getValues().size() < offset)
            return null;

        sh.setCode(code);
        populate(sh, list, offset-1);
        return sh;
    }




    public static List<String> getDateHistoryListOffsetLimit(final String code, int offset, int max) {
        List<String> dateList = new ArrayList<>();
        //bug .. dont get all data .. so make filter to have date only since 2013
        String query = "SELECT * FROM "+code +" where time > '2010-01-01T00:00:00Z'";
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));
        int series = max + offset;
        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size< series)
            return null;

        for (int i = size-max; i < size; i++) {
            dateList.add((String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0));
        }
        return dateList;
    }

    public static String getLastDateHistory(final String code) {

        //suppose base is filled
        String query = "SELECT * FROM "+code +" where time > '2015-06-01T00:00:00Z'";
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();


        return (String) list.getResults().get(0).getSeries().get(0).getValues().get(size-1).get(0);

    }


    public static List<StockHistory> getStockHistoryDateInvert(final String code, final String date, int offset) {

        List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+  Integer.toString(offset*4) +"d";
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size < offset)
            return null;

        for (int i = size-1; stockList.size() < offset; i--) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;

    }


    /**
     * return last max StockHistory
     * @param code
     * @param max
     * @return O or max last StockHistory
     */
    public static List<StockHistory> getStockHistoryLast(final String code, int max) {

        List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2015-06-01T00:00:00Z'";
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size < max)
            return null;

        for (int i = size-max; i < size; i++) {
            StockHistory sh = new StockHistory();
            sh.setCode(code);
            populate(sh, list, i);
            stockList.add(sh);
        }

        return stockList;

    }


    /**
     * return last max StockHistory
     * @param code
     * @param max
     * @return O or max last StockHistory
     */
    public static List<StockHistory> getStockHistoryLastInvert(final String code, int max) {

        List<StockHistory> stockList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM "+code +" where time > '2010-01-01T00:00:00Z'";

        QueryResult list = Requester.sendRequest(new QueryRequest(query,dbName));
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


    /**
     * NOT USE RANGE FUNCTION
     */

    @Deprecated
    public static List<StockHistory> getStockAnalyseList(final String code) {
        List<StockHistory> stockList = new ArrayList<StockHistory>();
        String query = "SELECT * FROM "+code+ "T";
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        for (int i =0;i<list.getResults().get(0).getSeries().get(0).getValues().size();i++)
            stockList.add(getStockHistory(code, (String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0)));

        return stockList;
    }

    @Deprecated
    public static List<StockHistory> getStockHistoryList(final String code) {
        List<StockHistory> stockList = new ArrayList<StockHistory>();
        String query = "SELECT * FROM "+code;
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        for (int i =0;i<list.getResults().get(0).getSeries().get(0).getValues().size();i++)
            stockList.add(getStockHistory(code, (String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0)));

        return stockList;
    }

    @Deprecated
    public static List<StockHistory> getStockHistoryListOffsetWithAT(final String code, int offset) {
        List<StockHistory> stockList = new ArrayList<>();
        String query = "SELECT * FROM "+code;
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        if (size < offset)
            return null;

        for (int i = size-1; i < list.getResults().get(0).getSeries().get(0).getValues().size(); i++) {
            StockHistory sh = new StockHistory();
            String date = (String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0);
            sh.setCode(code);
            populate(sh, list, i);  sh.setAnalyse_tech(StockAnalyse.getAnalyse(code, date));
            stockList.add(sh);
        }

        return stockList;
    }


    @Deprecated
    public static List<String> getDateHistoryListOffset(final String code, int offset) {
        List<String> dateList = new ArrayList<>();
        String query = "SELECT * FROM "+code;
        QueryResult list = Requester.sendRequest(new QueryRequest(query, dbName));

        if (list.getResults().get(0).getSeries().get(0).getValues().size()< offset)
            return null;

        for (int i = offset; i < list.getResults().get(0).getSeries().get(0).getValues().size(); i++) {
            StockHistory sh = new StockHistory();
            dateList.add((String) list.getResults().get(0).getSeries().get(0).getValues().get(i).get(0));

        }
        return dateList;
    }

}
