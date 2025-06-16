package com.bupt.service;

import com.bupt.pojo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

//返回任务数据、上传数据
public interface TaskService {
    public StudentTask getTask();
    public int uploadTask(MultipartFile file) throws IOException; //学生作业上传
    public int uploadTeacherTask(TeacherTask teacherTask,MultipartFile file) throws IOException;
//    public List<TeacherTask> getTeacherTasks() throws IOException;
    public int deleteTeacherTask(List<Integer> idList) throws IOException;
    public PageBean<TeacherTask> getTeacherTasks(int page,int pageSize,Integer teacherId,String taskName);
//    public int assignTask(Task uploadTask);

    //MongoDB
    public Task createTask(Task task, MultipartFile file) throws IOException;
    public List<Task> getTasksByOwnerId(Integer ownerId);
    public PageBean<ReviewTask> getReviewTaskByRewId(int page,int pageSize,Integer reviewerId);
    public PageBean<StudentSubmission> getSubmissionsByStuId(int page,int pageSize, Integer studentId);
    public Evaluation getEvaluationBySubId(String submissionId);
    public Evaluation evaluateTask(String taskId);
    public StudentSubmission uploadVideoTask(String submissionId, MultipartFile videoFile) throws IOException;//上传视频任务
    public StudentSubmission uploadTextTask(String submissionId, MultipartFile textFile) throws IOException;
    public Task assignStudents(String taskId, List<Integer> studentIds);
    public List<ReviewTask> setReviewTeachers(String taskId,List<Integer> teacherIds);
    public List<ReviewTask> setReviewStudents(String taskId, List<Integer> stuIds, List<Integer> allocateStudents, boolean reAllocate);
    public Evaluation getEvaluationByTaskIdAndStuId(String taskId,Integer stuId);
    public String deleteAllocated(List<String> reviewTaskIds);
    public Task deleteAssigned(String taskId, List<Integer> studentIds);
    public StudentSubmission uploadPPtTask(MultipartFile PPtFile,String submissionId) throws IOException;
    public String deleteTask(String taskId);
    public Evaluation getMachineGrade(Integer stuId,String taskId,String reviewType) throws IOException, ExecutionException, InterruptedException;
    public Evaluation getMachineGradeVideo(Integer stuId, String taskId) throws JsonProcessingException, ExecutionException, InterruptedException;
    public Task setTaskEvaluation(String taskId,List<EvaluationDimension> evaluationDimensions);

    public Evaluation setGrade(String taskId, Integer stuId, Evaluation.EvaluationType evaluationType);
    public String getNewFileName(MultipartFile file);
    Task getTaskInfoById(String taskId);

    StudentSubmission getSubmissionInfoById(String submissionId);


    public PageBean getAllGradesByTaskId(String taskId,int page,int pageSize);

    public PageBean getAllSubmitsByTaskId(String taskId, int page, int pageSize);
}
