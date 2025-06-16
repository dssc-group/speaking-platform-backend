package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherTask {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class evaluationContent{
        String evaluationType;
        String evaluationTitle;
        List<String> evaluationDimensions;
    }

    private int id;
    private int ownerId;
    private String owner;
    private LocalDateTime createTime;
    private String taskName;
    private LocalDateTime deadline;
    private String taskRequirements;
    private String filePath;
    private LocalDateTime updateTime;
    private List<String> submitContent;
    private List<String> reviewProcess;
    private List<String> reviewSettings;
    private List<Integer> weights;
    private String submitContentJson;
    private String reviewProcessJson;
    private String reviewSettingsJson;
    private String weightsJson;
}
