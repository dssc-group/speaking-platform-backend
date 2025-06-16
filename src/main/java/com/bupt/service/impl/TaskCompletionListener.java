package com.bupt.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class TaskCompletionListener implements MessageListener {

    // 存储待完成任务的 Map，Key 为任务 ID，Value 为 CompletableFuture
    private ConcurrentMap<String, CompletableFuture<Object>> pendingTasks = new ConcurrentHashMap<>();
    @Autowired
    ObjectMapper objectMapper;
    public void addPendingTask(String taskId, CompletableFuture<Object> future) {
        pendingTasks.put(taskId, future);
    }
    //消息队列接受任务完成的消息
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageJson = new String(message.getBody(), StandardCharsets.UTF_8); //任务完成数据
        try {
            JsonNode rootNode =  objectMapper.readTree(messageJson); //解析Json数据
            JsonNode result = rootNode.get("result");
            String taskId = rootNode.get("taskId").asText();
            CompletableFuture<Object> future = pendingTasks.get(taskId);
            if (future != null) {
                // 将结果设置到 CompletableFuture 中
                future.complete(result);
                // 移除已完成的任务
                pendingTasks.remove(taskId);
            } //发送任务结果，并且从任务字典中移除已完成的任务
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 获取对应的 CompletableFuture
    }


}
