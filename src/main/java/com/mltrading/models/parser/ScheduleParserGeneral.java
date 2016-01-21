package com.mltrading.models.parser;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mltrading.models.parser.impl.RealTimeParserYahoo;
import com.mltrading.models.util.ThreadFactory;

/**
 * Created by gmo on 19/06/2015.
 */
public class ScheduleParserGeneral  {

    protected static final long START_DELAY = 30 * 1000;

    protected AtomicBoolean extractionIsRunning = new AtomicBoolean(false);
    protected long extractionCycleInMs;

    protected Timer timer;
    protected TimerTask timerTask;


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

    /**
     * Runs an extraction IF none is currently running
     * @throws Exception
     */
    public void triggerExtraction() throws Exception{
        if(extractionIsRunning.compareAndSet(false, true)) {
            ThreadFactory.makeThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        runExtraction();
                    } finally {
                        extractionIsRunning.set(false);
                    }
                }
            }, "ExtractionOnDemandProcess").start();
        } else {
            throw new Exception("The extraction process request was ignored because an extraction is already running");
        }
    }


    protected void runExtraction() {
        RealTimeParserYahoo.refreshCache();
    }


    public void start() {
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


    public AtomicBoolean getExtractionIsRunning() {
        return extractionIsRunning;
    }

}


