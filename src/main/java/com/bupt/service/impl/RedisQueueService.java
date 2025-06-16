package com.bupt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
//redis 任务队列收发
@Slf4j
@Service
public class RedisQueueService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String QUEUE_NAME = "UploadQueue";

    public void publishUploadStatus(String message) {
        redisTemplate.convertAndSend("UploadChannel",message);
        log.info(message + "uploaded!");
    }

    public String receiveMessage() {
        return (String) redisTemplate.opsForList().rightPop(QUEUE_NAME);
    }
}