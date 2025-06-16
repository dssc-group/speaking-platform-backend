package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "TaskCollection")
public class Task {
    @Id
    private String id;
    private String ownerName;
    private Integer ownerId;
    private String taskName;
    private List<String> submissionTypes; //提交内容
    private List<String> evaluationMethods; //评价方法
    private List<Integer> assignedStudents; //分配任务
    private Map<String, Integer> mulAccessIds; //互评学生ID
//    private boolean autoMulSet; //是否设置一键互评
    private List<String> reviewSettings;
    private String fileName;
    private String taskRequirements;
    @CreatedDate
    private LocalDateTime createTime;
    private LocalDateTime deadline;
    @LastModifiedDate
    private LocalDateTime updateTime;
    private List<Integer> weights;
    private List<Integer> reviewTeachers;
    private List<EvaluationDimension> evaluationDimensions;
    //生命周期回调

}
