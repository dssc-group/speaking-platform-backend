package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "SubmissionCollection")
public class StudentSubmission {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TaskInfo {
        private String submissionType;
        private String fileName;
        private LocalDateTime submittedTime;
        private Boolean finished;
    }
    @Id
    private String id;
    private String taskId; //任务Id
    private Integer studentId; //学生Id
    private Boolean finished; //是否完成
    private List<TaskInfo> taskInfos;

}
