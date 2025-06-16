package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
//String taskId,List<Integer> stuIds, List<Integer> allocateStudents, boolean reAllocate
public class TaskAssignmentDTD {
    private String taskId;
    private Integer stuId;
    private List<Integer> stuIds;
    private List<Integer> teacherId;
    private List<Integer> allocateStudents;
    private List<String> reviewTasks;
    private List<EvaluationDimension> evaluationDimensions;
    private boolean reAllocate;
    private Evaluation.EvaluationType evaluationType;

}
