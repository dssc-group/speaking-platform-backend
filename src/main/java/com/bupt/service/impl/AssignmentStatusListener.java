package com.bupt.service.impl;

import com.bupt.pojo.StudentSubmission;
import com.bupt.pojo.TaskProducer;
import com.bupt.repository.StudentSubmissionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AssignmentStatusListener implements org.springframework.data.redis.connection.MessageListener {

    @Autowired
    private TaskProducer taskProducer;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StudentSubmissionRepository studentSubmissionRepository;

    private Map<String, String> fileMap;
    private static final String redisChannel = "UploadChannel";

    // 初始化文件类型映射
    @PostConstruct
    public void initMap() {
        fileMap = new HashMap<>();
        fileMap.put("video", "视频");
        fileMap.put("audio", "音频");
        fileMap.put("docx", "演讲稿");
        fileMap.put("ppt", "PPT");
    }

    // 处理消息逻辑，使用 MongoDB 原子操作更新数据
    @Transactional
    public void handleFileUploadStatus(String message) throws JsonProcessingException {
        // 解析消息
        String[] parts = message.split("/");
        Integer stuId = Integer.valueOf(parts[1]);
        String taskId = parts[2];
        String fileType = fileMap.get(parts[3]);

        String videoPath = null;
        String audioPath = null;

        if ("视频".equals(fileType)) {
            videoPath = message; // 如果是视频文件，记录路径
        }

        // 查询条件：根据任务 ID 和学生 ID 定位文档
        Query query = new Query(Criteria.where("taskId").is(taskId).and("studentId").is(stuId));

        // 更新操作
        Update update = new Update();
        update.set("taskInfos.$[elem].finished", true); // 将对应任务标记为完成
        update.set("taskInfos.$[elem].submittedTime", LocalDateTime.now()); // 更新提交时间

        // 仅更新指定类型的子文档
        update.filterArray("elem.submissionType", fileType);

        // 执行原子更新
        mongoTemplate.updateFirst(query, update, StudentSubmission.class);

        // 如果文件类型为视频且音频路径存在，调用音频提取逻辑
        if (videoPath != null) {
            Query audioQuery = new Query(Criteria.where("taskId").is(taskId).and("studentId").is(stuId).and("taskInfos.submissionType").is("音频"));
            StudentSubmission audioSubmission = mongoTemplate.findOne(audioQuery, StudentSubmission.class);

            if (audioSubmission != null) {
                for (StudentSubmission.TaskInfo taskInfo : audioSubmission.getTaskInfos()) {
                    if ("音频".equals(taskInfo.getSubmissionType())) {
                        audioPath = taskInfo.getFileName(); // 获取音频文件路径
                        break;
                    }
                }
            }

            if (audioPath != null) {
                taskProducer.extraAudioFromVide(videoPath, audioPath); // 执行音频提取逻辑
            }
        }
    }

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        // 从 Redis 消息中获取内容
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            handleFileUploadStatus(msg); // 处理消息
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process message: " + msg, e);
        }
    }
}
