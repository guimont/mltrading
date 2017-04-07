package com.mltrading.models.stock;

import org.joda.time.DateTime;


/**
 * Created by gmo on 06/12/2015.
 */
public class StockRawMat extends StockHistory {

    private String url;

    public StockRawMat(String code, String name, String url, int row) {
        this.url =  url;
        this.setCode(code);
        this.setCodif(code);
        this.setName(name);
        this.setRow(row);
    }



    public void setDayInvest(String day) {

        this.day = day;
        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,10);
        timeInsert = new DateTime( YY + "-" + MM + "-" + DD);
    }


    public String getUrl() {
        return url;
    }

}
