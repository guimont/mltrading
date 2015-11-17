package com.mltrading.models.util;

/**
 * Created by gmo on 20/06/2015.
 */
public class ThreadFactory {
    /**
     * Builds a Thread, ensures it uses default priority & is not daemon
     *
     * @param r
     *            a {@link Runnable} instance
     * @param name
     *            a {@link Thread} name
     * @return a {@link Thread}
     */
    public static Thread makeThread (final Runnable r, final String name) {
        final Thread t = new Thread (r, name);
        if (t.isDaemon ()) {
            t.setDaemon (false);
        }
        if (t.getPriority () != Thread.NORM_PRIORITY) {
            t.setPriority (Thread.NORM_PRIORITY);
        }
        return t;
    }


}
