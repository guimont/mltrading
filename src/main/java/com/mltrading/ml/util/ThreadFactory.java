package com.mltrading.ml.util;

/**
 * Created by gmo on 09/09/2016.
 */
public class ThreadFactory {
    public ThreadFactory() {
    }

    public static Thread makeThread(Runnable r, String name) {
        Thread t = new Thread(r, name);
        if(t.isDaemon()) {
            t.setDaemon(false);
        }

        if(t.getPriority() != 5) {
            t.setPriority(5);
        }

        return t;
    }
}
