package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.InfluxDaoConnectorDocument;
import com.mltrading.influxdb.dto.QueryResult;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 14/03/2016.
 */
public class StockDocument {
    private String code;
    private String day;
    private String ref;

    DateTime timeInsert;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return day;
    }

    public void setDate(String date) {
        this.day = date;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public DateTime getTimeInsert() {
        return timeInsert;
    }

    public void setTimeInsert(DateTime timeInsert) {
        this.timeInsert = timeInsert;
    }

    public void setDayInvestir(String day, String hour) {

        this.day = day;
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,8);
        timeInsert = new DateTime( "20" + YY + "-" + MM + "-" + DD+"T" +hour );
    }

    static public int DATE_COLUMN = 0;
    //static public int COLUMN_CODE = 1;
    static public int COLUMN_REF = 1;


    public static void populate(StockDocument sd, QueryResult meanQ, int i) {
        sd.setDate((String) meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(DATE_COLUMN));
        //sd.setCode(meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(COLUMN_CODE).toString());
        sd.setRef(meanQ.getResults().get(0).getSeries().get(0).getValues().get(i).get(COLUMN_REF).toString());
    }

    public static List<StockDocument> getStockDocument(final String code) {

        List<StockDocument> docList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + "R";
        QueryResult list = InfluxDaoConnectorDocument.getPoints(query);

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        for (int i = 0; i < size; i++) {
            StockDocument sd = new StockDocument();
            sd.setCode(code);
            populate(sd, list, i);
            docList.add(sd);
        }

        return docList;

    }

    public static List<StockDocument> getStockDocumentInvert(final String code) {

        List<StockDocument> docList = new ArrayList<>();
        //offset is mult by 2 because it is no dense data
        String query = "SELECT * FROM " + code + "R";
        QueryResult list = InfluxDaoConnectorDocument.getPoints(query);

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        for (int i = size-1; i > 0; i--) {
            StockDocument sd = new StockDocument();
            sd.setCode(code);
            populate(sd, list, i);
            docList.add(sd);
        }

        return docList;

    }


    public static List<Double> getStockDocument(final String code, String date, int offset) {

        try {

            List<Double> stockDocuments = new ArrayList<>();
            //offset is mult by 2 because it is no dense data
            //String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+ Integer.toString(offset)  +"d";
            String query = "SELECT count(ref) FROM " + code + " where time <= '" + date + "' and time > '" + date + "' - " + Integer.toString(offset) + "d group by time(1d)";
            QueryResult list = InfluxDaoConnectorDocument.getPoints(query);


            if (list.getResults().get(0).getSeries() == null || list.getResults().get(0).getSeries().get(0) == null) {
                for (int i = 0; i< offset; i++)
                    stockDocuments.add(new Double(0));
            } else {

                int size = list.getResults().get(0).getSeries().get(0).getValues().size();
                if (size < offset)
                    return null;

                for (int i = size - 1; stockDocuments.size() < offset; i--) {
                    Double I = new Double(list.getResults().get(0).getSeries().get(0).getValues().get(i).get(1).toString());
                    stockDocuments.add(I);
                }
            }

            return stockDocuments;
        }catch (Exception e) {
            System.out.print(e);
            return null;
        }

    }

    public static String getLastDateHistory(final String code) {

        //suppose base is filled
        String query = "SELECT * FROM "+ code +" where time > '2015-06-01T00:00:00Z'";
        QueryResult list = InfluxDaoConnectorDocument.getPoints(query);

        if (list.getResults().get(0).getSeries() == null) return null;
        int size = list.getResults().get(0).getSeries().get(0).getValues().size();

        return (String) list.getResults().get(0).getSeries().get(0).getValues().get(size-1).get(0);

    }


}
