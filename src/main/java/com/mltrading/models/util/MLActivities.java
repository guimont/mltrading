package com.mltrading.models.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gmo on 10/02/2017.
 */
public class MLActivities implements Serializable {
    public String action;
    public String model;
    public String startDate;
    public String endDate;
    public String status;
    public int loop;
    public double upturn;

    public MLActivities() {
    }

    public MLActivities(String action, String model, String startDate, String endDate, String status, int loop, double upturn) {
        this.action = action;
        this.model = model;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.loop = loop;
        this.upturn = upturn;
    }


    public MLActivities(String action, String model, String status, int loop, double upturn, boolean ended) {
        this.action = action;
        this.model = model;
        this.status = status;
        this.loop = loop;
        this.upturn = upturn;

        if (ended == false)
            setStartDate();
        else
            setEndDate();

    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate() {
        String format = "dd/MM/yy H:mm:ss";

        SimpleDateFormat formater = new SimpleDateFormat( format );
        Date date = new Date();

        this.startDate = formater.format(date);

    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public MLActivities setEndDate() {
        String format = "dd/MM/yy H:mm:ss";

        SimpleDateFormat formater = new SimpleDateFormat( format );
        Date date = new Date();

        this.endDate = formater.format(date);
        return this;
    }


    public String getStatus() {
        return status;
    }

    public MLActivities setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public double getUpturn() {
        return upturn;
    }

    public void setUpturn(double upturn) {
        this.upturn = upturn;
    }
}
