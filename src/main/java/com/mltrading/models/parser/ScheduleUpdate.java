package com.mltrading.models.parser;


import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.ml.CacheMLActivities;
import com.mltrading.ml.CacheMLStock;
import com.mltrading.ml.MatrixValidator;
import com.mltrading.ml.MlForecast;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.model.ModelTypeList;
import com.mltrading.repository.ArticleRepository;
import com.mltrading.service.ExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gmo on 19/06/2015.
 */
public class ScheduleUpdate {

    private static final Logger log = LoggerFactory.getLogger(ScheduleUpdate.class);
    protected AtomicBoolean extractionPassed = new AtomicBoolean(true);

    protected Timer timer;
    protected TimerTask timerTask;
    private static ExtractionService service = new ExtractionService();

    @javax.inject.Inject
    private ArticleRepository articleRepository;


    protected class GlobalTimerTask extends TimerTask {

        @Override
        public void run() {
            if(extractionPassed.compareAndSet(true, true)) {
                runUpdate();
            }
            extractionPassed.set(true);
            System.out.print(extractionPassed);
        }
    }


    protected void runUpdate() {

        if (CacheMLActivities.setIsRunning() == false) {
            log.error("optimizing still running, cannot launch runUpdate");
            return;
        }
        //System.out.print("now ?");
        updateBase();

        //CacheMLStock.load(); not need !!
        MlForecast ml = new MlForecast();

        //clean model perf database
        InfluxDaoConnector.deleteDB(MatrixValidator.dbNameModelPerf);


        //process result with model and save them
        ModelTypeList.modelTypes.forEach( t -> {
            ml.processList(t);
            CacheMLStock.savePerf(t);

        });

        //process aggragation model and save it
        ml.updateEnsemble();
        CacheMLStock.savePerf(ModelType.ENSEMBLE);

        //update result
        ml.updatePredictor();

        // close mutex
        CacheMLActivities.endRunning();

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
        if (t.before(Date.from(Instant.now()))) extractionPassed.set(false);
        this.timer.scheduleAtFixedRate(this.timerTask, t, u); // 60*60*24*100 = 8640000ms

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
            int diff = service.getLastUpdateRef() -1; //remove -1
            log.info("Have to extract " +  diff + " days in influxdb base");

            if (diff > 0)
                service.extractionCurrent(articleRepository,diff);
        } catch (Exception e) {
            log.error("cannot get history last date, base perhaps empty: " +e);
            return;
        }

    }

}


