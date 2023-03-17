package com.wt.project.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xcx
 * @date
 */
public class ThreadPool {
    public static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(2,4,500L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(65536));
}
