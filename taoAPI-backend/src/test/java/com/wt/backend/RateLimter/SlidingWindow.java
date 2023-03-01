package com.wt.backend.RateLimter;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.google.common.util.concurrent.RateLimiter;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xcx
 * @date
 */
public class SlidingWindow {
    private int QPS = 2;
    private ArrayDeque<Long> history = new ArrayDeque<>(2);

    private long TIME_WINDOWS = 1000;

    public String method(){
        if(history.size() < 2){
            history.addFirst(System.currentTimeMillis());
        }else{
            if(System.currentTimeMillis() - history.pollLast() > 1000){
                history.removeLast();
                history.addFirst(System.currentTimeMillis());
            }else {
                return "File";
            }
        }
        return "Success";
    }

    public static void main(String[] args) throws InterruptedException {
        SlidingWindow rateLimiter1 = new SlidingWindow();
        RateLimiter rateLimiter2 = RateLimiter.create(2);
        Thread.sleep(1000);
        for(int i = 0; i < 20; i++){
            System.out.println(LocalDateTime.now() +" " + rateLimiter1.method());
//            System.out.println(LocalDateTime.now() + " " +rateLimiter2.tryAcquire());
            Thread.sleep(250);
        }

    }
}
