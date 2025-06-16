package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ReviewCollection")
public class ReviewTask {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewClass{
        private String reviewContent; //评价内容：视频、音频、PPT、Word
        private Boolean finished; //评价状态
    }
    @Id
    private String id; //评价任务Id
    private Integer reviewerId; //评价人Id
    private Integer studentId; //被评价人Id
    private String taskId; //作业任务Id
    private String evaluationId; //评价结果Id
    private String reviewType; //评价类型
    private List<ReviewClass> reviews; //评价
}
