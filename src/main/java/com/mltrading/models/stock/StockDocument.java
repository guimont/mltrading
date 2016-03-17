package com.mltrading.models.stock;

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

}
