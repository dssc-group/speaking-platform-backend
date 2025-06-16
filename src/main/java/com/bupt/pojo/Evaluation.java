package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="EvaluationCollection")
public class Evaluation {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvaluationContent {
        private String evaluationContent; //视频
        private Boolean finished;
        private String evaluationTitle;
        private Integer grade;
        private List<Map<String,String>> evaluationDimensions;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvaluationType {
        private String evaluationMethod; //评价方法：自评、机评、师评、互评
        private Integer reviewerId;
        private List<EvaluationContent> evaluationContents; //评价维度及分数
    }
    @Id
    private String id;
    private String taskId;
    private Integer studentId;
    private String submissionId; //任务提交Id
    private List<EvaluationType> evaluationTypes;
}
