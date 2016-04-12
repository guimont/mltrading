package com.mltrading.models.stock;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.InfluxDaoConnectorDocument;
import com.mltrading.dao.InfluxDaoConnectorNotation;
import com.mltrading.influxdb.dto.QueryResult;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gmo on 14/03/2016.
 */
public class HistogramDocument {

    private static final Logger log = LoggerFactory.getLogger(HistogramDocument.class);

    DateTime timeInsert;
    private String code;
    private String day;
    private int lvl_L4 = 0;
    private int lvl_L3 = 0;
    private int lvl_L2 = 0;
    private int lvl_L1 = 0;
    private int lvl_0 = 0;
    private int lvl_P1 = 0;
    private int lvl_P2 = 0;
    private int lvl_P3 = 0;
    private int lvl_P4 = 0;


    public HistogramDocument( String code, String date) {
        this.day = date;
        this.code = code;
        timeInsert = new DateTime(date);
    }

    public DateTime getTimeInsert() {
        return timeInsert;
    }

    public void setTimeInsert(DateTime timeInsert) {
        this.timeInsert = timeInsert;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getLvl_L4() {
        return lvl_L4;
    }

    public void setLvl_L4(int lvl_L4) {
        this.lvl_L4 = lvl_L4;
    }

    public int getLvl_L3() {
        return lvl_L3;
    }

    public void setLvl_L3(int lvl_L3) {
        this.lvl_L3 = lvl_L3;
    }

    public int getLvl_L2() {
        return lvl_L2;
    }

    public void setLvl_L2(int lvl_L2) {
        this.lvl_L2 = lvl_L2;
    }

    public int getLvl_L1() {
        return lvl_L1;
    }

    public void setLvl_L1(int lvl_L1) {
        this.lvl_L1 = lvl_L1;
    }

    public int getLvl_0() {
        return lvl_0;
    }

    public void setLvl_0(int lvl_0) {
        this.lvl_0 = lvl_0;
    }

    public int getLvl_P1() {
        return lvl_P1;
    }

    public void setLvl_P1(int lvl_P1) {
        this.lvl_P1 = lvl_P1;
    }

    public int getLvl_P2() {
        return lvl_P2;
    }

    public void setLvl_P2(int lvl_P2) {
        this.lvl_P2 = lvl_P2;
    }

    public int getLvl_P3() {
        return lvl_P3;
    }

    public void setLvl_P3(int lvl_P3) {
        this.lvl_P3 = lvl_P3;
    }

    public int getLvl_P4() {
        return lvl_P4;
    }

    public void setLvl_P4(int lvl_P4) {
        this.lvl_P4 = lvl_P4;
    }

    private void setHisto(int note) {
        switch (note) {
            case -4:
                lvl_L4++;
                break;
            case -3:
                lvl_L3++;
                break;
            case -2:
                lvl_L2++;
                break;
            case -1:
                lvl_L1++;
                break;

            case 4:
                lvl_P4++;
                break;
            case 3:
                lvl_P3++;
                break;
            case 2:
                lvl_P2++;
                break;
            case 1:
                lvl_P1++;
                break;
        }
    }

    private String lem(String w) {
        String r  = w;
        if (r.endsWith("s")) r  = r.substring(0,r.length()-1);
        if (r.endsWith("ée")) r = r.substring(0,r.length()-1);

        return r;
    }


    public void test() {
        String test = lem ("infligées");
        System.out.println(test);
    }

    public void analyseDocument(String doc, Map<String, Integer> dico) {
        String[] l = doc.split(" ");

        for (String w:l) {
            Integer note = dico.get(lem(w));
            if (note != null) {
                log.info("word find: " + w + " for note: " + note);
                setHisto(note.intValue());
            }
        }



    }

    public int sum() {
        return lvl_L4*-4 + lvl_L3*-3 + lvl_L2*-2 + lvl_L1*-1 + lvl_0 + lvl_P1 + lvl_P2*2 + lvl_P3*3 + lvl_P4*4;
    }

    public static List<Double> getSumDocument(final String code, String date, int offset) {

        List<Double> stockDocuments = new ArrayList<>();


        //offset is mult by 2 because it is no dense data
        //String query = "SELECT * FROM " + code + " where time <= '" + date + "' and time > '"+ date + "' - "+ Integer.toString(offset)  +"d";
        String query = "SELECT sum(sum) FROM " + code + "R where time <= '" + date + "' and time > '"+ date + "' - "+ Integer.toString(offset)  +"d group by time(1d)";
        QueryResult list = InfluxDaoConnectorNotation.getPoints(query);


        if (list.getResults().get(0).getSeries() == null || list.getResults().get(0).getSeries().get(0) == null) {
            for (int i = 0; i< offset; i++)
                stockDocuments.add(new Double(0));
        } else {

            int size = list.getResults().get(0).getSeries().get(0).getValues().size();
            if (size < offset)
                return null;

            for (int i = size - 1; stockDocuments.size() < offset; i--) {
                Double d;
                if (list.getResults().get(0).getSeries().get(0).getValues().get(i).get(1) == null)
                    d = new Double(0.0);
                else
                    d = new Double(list.getResults().get(0).getSeries().get(0).getValues().get(i).get(1).toString());
                stockDocuments.add(d);
            }
        }

        return stockDocuments;

    }


    public static String getLastDateHistory( String code) {


         //suppose base is filled
        String query = "SELECT * FROM "+ code +"R where time > '2015-06-01T00:00:00Z'";
        QueryResult list = InfluxDaoConnectorNotation.getPoints(query);

        int size = list.getResults().get(0).getSeries().get(0).getValues().size();


        return (String) list.getResults().get(0).getSeries().get(0).getValues().get(size-1).get(0);

    }


}
