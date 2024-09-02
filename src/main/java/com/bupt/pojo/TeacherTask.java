package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherTask {
    private int id;
    private String owner;
    private String createTime;
    private String name;
    private String deadline;
    private String taskRequirements;
    private String filePath;
    private List<String> submitContent;
    private List<String> reviewProcess;
    private List<String> reviewSettings;
    private List<Integer> weights;
    private String submitContentJson;
    private String reviewProcessJson;
    private String reviewSettingsJson;
    private String weightsJson;
}
