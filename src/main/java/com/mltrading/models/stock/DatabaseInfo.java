package com.mltrading.models.stock;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gmo on 29/06/2016.
 */
public class DatabaseInfo implements Serializable{


    public String name;
    public String lastdate;
    public String firstdate;
    public Integer nbElements;


    public DatabaseInfo(String name, String lastdate, String firstdate, Integer nbElements) {
        this.name = name;
        this.lastdate = lastdate;
        this.firstdate = firstdate;
        this.nbElements = nbElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    public String getFirstdate() {
        return firstdate;
    }

    public void setFirstdate(String firstdate) {
        this.firstdate = firstdate;
    }

    public Integer getNbElements() {
        return nbElements;
    }

    public void setNbElements(Integer nbElements) {
        this.nbElements = nbElements;
    }

    /**
     *
     * @param l stock last RANGE extraction
     * @return
     */
    public static DatabaseInfo populate(String code, List<? extends StockHistory> l) {

        if (null == l)
            return new DatabaseInfo(code,"Empty","Empty", 0);

        return new DatabaseInfo(code,l.get(0).day,l.get(l.size()-1).day, l.size());
    }


    /**
     *
     * @param l stock last RANGE extraction
     * @return
     */
    public static DatabaseInfo populateDocument(String code, List<? extends StockDocument> l) {

        if (null == l)
            return new DatabaseInfo(code,"Empty","Empty", 0);

        return new DatabaseInfo(code,l.get(0).day,l.get(l.size()-1).day, l.size());
    }



    @Override
    public String toString() {
        return "DatabaseInfo{" +
            "name='" + name + '\'' +
            ", lastdate='" + lastdate + '\'' +
            ", firstdate='" + firstdate + '\'' +
            ", nbElements=" + nbElements +
            '}';
    }
}
