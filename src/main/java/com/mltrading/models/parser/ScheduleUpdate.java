package com.mltrading.models.parser;


import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MLPredictor;
import com.mltrading.ml.MlForecast;
import com.mltrading.models.parser.impl.RealTimeParserYahoo;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockPrediction;
import com.mltrading.service.ExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gmo on 19/06/2015.
 */
public class ScheduleUpdate {

    private static final Logger log = LoggerFactory.getLogger(ScheduleUpdate.class);
    protected AtomicBoolean extractionIsRunning = new AtomicBoolean(false);

    protected Timer timer;
    protected TimerTask timerTask;
    private static ExtractionService service = new ExtractionService();


    protected class GlobalTimerTask extends TimerTask {

        @Override
        public void run() {
            if(extractionIsRunning.compareAndSet(false, true)) {
            } else {
                runUpdate();
            }
        }
    }


    protected void runUpdate() {
        //System.out.print("now ?");
        updateBase();
    }


    public void start() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 8);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        this.timerTask = new GlobalTimerTask();
        this.timer = new Timer("UpdateProcess", true);
        Date t = today.getTime();
        long u = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        this.timer.schedule(this.timerTask, t, u); // 60*60*24*100 = 8640000ms


        //updateBase();
    }

    public void stop() {
        if (this.timerTask != null){
            this.timerTask.cancel();
            this.timerTask = null;
        }
        if (this.timer != null){
            this.timer.cancel();
            this.timer = null;
        }
    }


    /**
     * take 3 last date for one element in indice/sector/history
     */
    void updateBase() {
        try {
            int diff = service.getLastUpdateRef();
            log.info("Have to extract " +  diff + " days in influxdb base");

            if (diff > 0)
                service.extractionCurrent(diff);
        } catch (Exception e) {
            log.error("cannot get history last date, base perhaps empty: " +e);
            return;
        }

    }

}


