package com.bupt.pojo;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class test implements Callable<Integer>,Runnable{
    private final ReentrantLock lock = new ReentrantLock();
    @Override
    public Integer call()throws Exception{
        return 1;
    }
    @Override
    public void run(){
        return;
    }

    public synchronized static void main(String[] args) {
        Callable<Integer> myTask = new test();
        FutureTask<Integer> futureTask = new FutureTask<>(myTask);
        Thread t = new Thread(futureTask);
        t. start();
        ExecutorService executors = Executors.newFixedThreadPool(10);
        AtomicInteger ans = new AtomicInteger(1);
        ans.incrementAndGet();
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        threadLocal.set(1);
        ans.decrementAndGet();
    }

}

