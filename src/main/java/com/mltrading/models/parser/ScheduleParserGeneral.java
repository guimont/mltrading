package com.mltrading.models.parser;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mltrading.models.parser.impl.RealTimeParserYahoo;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.util.ThreadFactory;
import com.mltrading.service.ExtractionService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gmo on 19/06/2015.
 */
public class ScheduleParserGeneral  {


    private static final Logger log = LoggerFactory.getLogger(ScheduleParserGeneral.class);
    protected AtomicBoolean extractionIsRunning = new AtomicBoolean(false);
    protected long extractionCycleInMs;

    protected Timer timer;
    protected TimerTask timerTask;
    private static ExtractionService service = new ExtractionService();


    protected class GlobalTimerTask extends TimerTask {

        @Override
        public void run() {
            if(extractionIsRunning.compareAndSet(false, true)) {
                try {
                    runExtraction();
                } finally {
                    extractionIsRunning.set(false);
                }
            }
        }
    }


    protected void runExtraction() {
        RealTimeParserYahoo.refreshCache();
    }


    public void start() {
        updateBase();
        RealTimeParserYahoo.loaderCache();
        this.extractionCycleInMs =  30000;
        this.timer = new Timer("ExtractionProcess", true);
        this.timerTask = new GlobalTimerTask();
        this.timer.schedule(this.timerTask, 0, this.extractionCycleInMs);
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

        String l = StockHistory.getLastDateHistory("FR0000045072");
        DateTime timeInsert = new DateTime(l);
        DateTime timeNow = new DateTime(System.currentTimeMillis());

        int diff =
            timeNow.getDayOfMonth() - timeInsert.getDayOfMonth();

        l = StockHistory.getLastDateHistory("FRIN");
        timeInsert = new DateTime(l);
        diff = Math.max(diff, timeNow.getDayOfMonth() - timeInsert.getDayOfMonth());

        l = StockHistory.getLastDateHistory("EFCHI");
        timeInsert = new DateTime(l);
        diff = Math.max(diff, timeNow.getDayOfMonth() - timeInsert.getDayOfMonth());

        diff -=1;


        log.info("Have to extract " +  diff + " days in influxdb base");

        if (diff > 0)
            service.extractionCurrent(diff);

    }

}


