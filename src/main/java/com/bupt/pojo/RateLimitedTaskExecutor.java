package com.bupt.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class RateLimitedTaskExecutor {
    private final BlockingDeque<Runnable> taskQueue = new LinkedBlockingDeque<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public RateLimitedTaskExecutor(){
        executorService.submit(() -> {
            while(true) {
                try {
                    Runnable task = taskQueue.take();
                    scheduler.schedule(()->{
                        task.run();
                    },2,TimeUnit.SECONDS);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
    public void submitTask(Runnable task){
        try {
            taskQueue.put(task);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }


}
