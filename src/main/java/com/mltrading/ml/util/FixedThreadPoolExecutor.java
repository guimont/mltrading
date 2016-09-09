package com.mltrading.ml.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 09/09/2016.
 */
public class FixedThreadPoolExecutor extends ThreadPoolExecutor {
    public FixedThreadPoolExecutor(int size, String name) {
        this(size, 2147483647, name);
    }

    public FixedThreadPoolExecutor(int size, int queueSize, final String name) {
        super(size, size, 0L, TimeUnit.MILLISECONDS, (BlockingQueue)(queueSize == 0?new SynchronousQueue():new LinkedBlockingQueue(queueSize)), new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return com.mltrading.ml.util.ThreadFactory.makeThread(r, name);
            }
        });
    }
}
