package com.mltrading.models.parser;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MlForecast;
import com.mltrading.models.parser.impl.RealTimeParserBoursorama;
import com.mltrading.models.parser.impl.RealTimeSectorGoogle;
import com.mltrading.repository.ArticleRepository;
import com.mltrading.service.ExtractionService;
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

    private RealTimeParserBoursorama realTimeParserBoursorama;
    private RealTimeSectorGoogle realTimeSectorGoogle;

    @javax.inject.Inject
    private ArticleRepository articleRepository;

    public ScheduleParserGeneral() {
        realTimeParserBoursorama = new RealTimeParserBoursorama();
        realTimeSectorGoogle = new RealTimeSectorGoogle();
    }

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

    private void updatePredictor() {


        CacheMLStock.load();

        MlForecast.updatePredictor();
    }


    protected void runExtraction() {
        realTimeParserBoursorama.refreshCache();
        realTimeSectorGoogle.refreshCache();

    }


    public void start() {
        //updateBase(); //not use here but in upddate scheduler*/
        realTimeParserBoursorama.loaderCache();
        updatePredictor();
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

        try {

            int diff = 0; //service.getLastUpdateRef();

            log.info("Have to extract " +  diff + " days in influxdb base");

            if (diff > 0)
                service.extractionCurrent(articleRepository,diff);
        } catch (Exception e) {
            log.error("cannot get history last date, base perhaps empty: " +e);
            return;
        }




    }

}


