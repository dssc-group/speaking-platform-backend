package com.bupt.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskProducer {
    @Value("${redis.redisHome}")
    String redisHost;
    @Value("${redis.redisPort}")
    Integer redisPort;
    ObjectMapper objectMapper = new ObjectMapper();
    JedisPool jedisPool;



    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class CeleryTaskMessage {
        String task;
        String [] args;
        String id;
        Map<String, Object> properties;
    }
    @PostConstruct
    public void init() {
        this.jedisPool = new JedisPool(redisHost, redisPort);
    }
    //音频转录
    public void extraAudioFromVide(String videoName, String audioName) throws JsonProcessingException {
        String taskName = "extraAudioFromVideo";
        String taskId = UUID.randomUUID().toString();
        Map<String,Object> properties = new HashMap<>();
        properties.put("content_type", "application/json");
        properties.put("delivery_mode", 2); // 持久化消息
        properties.put("priority", 0); // 任务优先级
        CeleryTaskMessage taskMessage = new CeleryTaskMessage(taskName,new String[]{videoName,audioName},taskId,properties);
        String taskJson = objectMapper.writeValueAsString(taskMessage);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush("messageQueue",taskJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //机评任务
    //发送任务请求以及等待任务完成
    public void getMachineGradeWord(String taskId,String wordPath) throws JsonProcessingException {
        String taskName = "getMachineGradeWord";
        Map<String,Object> properties = new HashMap<>();
        properties.put("content_type", "application/json");
        properties.put("delivery_mode", 2); // 持久化消息
        properties.put("priority", 0); // 任务优先级
        CeleryTaskMessage taskMessage = new CeleryTaskMessage(taskName,new String[]{wordPath},taskId,properties);
        String taskJson = objectMapper.writeValueAsString(taskMessage);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush("messageQueue",taskJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMachineGradeAudio(String taskId,String wordPath,String audioPath) throws JsonProcessingException {
        String taskName = "getMachineGradeAudio";
        Map<String,Object> properties = new HashMap<>();
        properties.put("content_type", "application/json");
        properties.put("delivery_mode", 2); // 持久化消息
        properties.put("priority", 0); // 任务优先级
        CeleryTaskMessage taskMessage = new CeleryTaskMessage(taskName,new String[]{wordPath,audioPath},taskId,properties);
        String taskJson = objectMapper.writeValueAsString(taskMessage);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush("messageQueue",taskJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getMachineGradeVideo(String reviewTaskId, String fileNameVideo) throws JsonProcessingException {
        String taskName = "getMachineGradeVideo";
        Map<String,Object> properties = new HashMap<>();
        properties.put("content_type", "application/json");
        properties.put("delivery_mode", 2); // 持久化消息
        properties.put("priority", 0); // 任务优先级
        CeleryTaskMessage taskMessage = new CeleryTaskMessage(taskName,new String[]{fileNameVideo},reviewTaskId,properties);
        String taskJson = objectMapper.writeValueAsString(taskMessage);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush("messageQueue",taskJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getMachineGradePPT(String reviewTaskId,String fileNamePPtx) throws JsonProcessingException {
        String taskName = "getMachineGradePPtx";
        Map<String,Object> properties = new HashMap<>();
        properties.put("content_type", "application/json");
        properties.put("delivery_mode", 2); // 持久化消息
        properties.put("priority", 0); // 任务优先级
        CeleryTaskMessage taskMessage = new CeleryTaskMessage(taskName,new String[]{fileNamePPtx},reviewTaskId,properties);
        String taskJson = objectMapper.writeValueAsString(taskMessage);
        try (Jedis jedis = jedisPool.getResource()){
            jedis.lpush("messageQueue",taskJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
