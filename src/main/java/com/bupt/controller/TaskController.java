package com.bupt.controller;

import com.bupt.pojo.*;
import com.bupt.service.TaskService;
import com.bupt.service.impl.HttpBigModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HttpBigModel httpBigModel;
    @PostMapping ("/uploadTask")
    public Result uploadTask(@RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        int state = taskService.uploadTask(file);
        return Result.success(0);
    }
    @PostMapping ("/task/uploadTask")
    public Result uploadTeacherTask(@RequestParam(value = "file",required = false) MultipartFile file,
                                    @RequestParam(value = "taskJson") String taskJson,
                                    HttpServletRequest request) throws IOException {
        Task task = objectMapper.readValue(taskJson,Task.class);
        if (file!=null) {
            String UPLOAD_URL = "http://file:8082/file/uploadTeacherTask";
            String newFileName = "/teacher"+"/"+taskService.getNewFileName(file);
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("File", file.getResource());
            body.add("fileName", newFileName);

//            file.transferTo(new File(this.teacherTaskHome + newFileName));
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("token",request.getHeader("token"));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(UPLOAD_URL, HttpMethod.POST, requestEntity, String.class);
            task.setFileName(newFileName);
        }

        return Result.success(taskService.createTask(task,file));
    }
    @RequestMapping("/getTeacherTasks")
    public Result getTeacherTasks(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  Integer teacherId, String taskName) throws IOException{
        PageBean<TeacherTask> pageBean = taskService.getTeacherTasks(page,pageSize,teacherId,taskName);
        return Result.success(pageBean);
    }
    @PostMapping("/deleteTeacherTask")
    public Result deleteTeacherTask(@RequestBody List<Integer> deleteList) throws IOException {
        taskService.deleteTeacherTask(deleteList);
        return Result.success(0);
    }

    @RequestMapping("/task/getTasksById")
    public Result getTasksByOwnerId(Integer ownerId) {
        return Result.success(taskService.getTasksByOwnerId(ownerId));
    }

    @RequestMapping("/task/getReviewTaskByStuId")
    public Result getReviewTaskByStuId(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       Integer reviewerId) {
        return Result.success(taskService.getReviewTaskByRewId(page,pageSize,reviewerId));
    }

    @RequestMapping("/task/getSubmissionsByStuId")
    public Result getSubmissionsByStuId(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        Integer studentId){
        return Result.success(taskService.getSubmissionsByStuId(page,pageSize,studentId));
    }

    @RequestMapping("/task/getEvaluationBySubId")
    public Result getEvaluationBySubId(String submissionId){
        return Result.success(taskService.getEvaluationBySubId(submissionId));
    }

    @PostMapping ("/task/uploadVideoTask") //上传视频
    public Result uploadVideoTask(@RequestParam(value = "videoFile") MultipartFile videoFile,
                                  @RequestParam(value = "submissionId") String submissionId) throws IOException {


        return Result.success(taskService.uploadVideoTask(submissionId,videoFile));
    }

    @PostMapping("/task/uploadPPtTask")
    public Result uploadPPtTask(@RequestParam(value = "PPtFile")MultipartFile PPtFile,
                                @RequestParam(value = "submissionId")String submissionId) throws IOException {
        return Result.success(taskService.uploadPPtTask(PPtFile,submissionId));
    }


    @PostMapping("/task/uploadTextTask")
    public Result uploadTextTask(@RequestParam(value = "textFile",required = true) MultipartFile textFile,
                                 @RequestParam(value = "submissionId",required = true) String submissionId) throws IOException {
        return Result.success(taskService.uploadTextTask(submissionId,textFile));
    }

    @PostMapping  ("/task/assignStudents") //将作业分配给学生
    public Result assignStudents(@RequestBody TaskAssignmentDTD taskAssignmentDTD){
        return Result.success(taskService.assignStudents(taskAssignmentDTD.getTaskId(),taskAssignmentDTD.getStuIds()));
    }

    @GetMapping("/task/getEvaluationByTaskIdAndStuId")
    public Result getEvaluationByTaskIdAndStuId(@RequestParam("taskId") String taskId,
                                               @RequestParam("stuId") Integer stuId){
        return  Result.success(taskService.getEvaluationByTaskIdAndStuId(taskId,stuId));
    }

    @PostMapping("/task/setReviewTeachers")
    public Result setReviewTeachers(@RequestBody TaskAssignmentDTD taskAssignmentDTD){
        return Result.success(taskService.setReviewTeachers(taskAssignmentDTD.getTaskId(),taskAssignmentDTD.getTeacherId()));
    }

    @PostMapping("/task/setReviewStudents")
    public Result setReviewStudents(@RequestBody TaskAssignmentDTD taskAssignmentDTD){
        return Result.success(taskService.setReviewStudents(taskAssignmentDTD.getTaskId(),
                taskAssignmentDTD.getStuIds(),
                taskAssignmentDTD.getAllocateStudents(),
                taskAssignmentDTD.isReAllocate()));
    }
    @PostMapping("/task/deleteAllocated")//互评任务
    public Result deleteAllocated(@RequestBody TaskAssignmentDTD taskAssignmentDTD){
        return Result.success(taskService.deleteAllocated(taskAssignmentDTD.getReviewTasks()));
    }

    @PostMapping("/task/deleteAssigned")
    public Result deleteAssigned(@RequestBody TaskAssignmentDTD taskAssignmentDTD){
        return Result.success(taskService.deleteAssigned(taskAssignmentDTD.getTaskId(),taskAssignmentDTD.getStuIds()));
    }

    @GetMapping("/task/deleteTask")
    public Result deleteTask(@RequestParam("taskId") String taskId){
        return Result.success(taskService.deleteTask(taskId));
    }

    @PostMapping("/task/setTaskEvaluation")
    public Result setTaskEvaluation(@RequestBody TaskAssignmentDTD taskAssignmentDTD){
        return Result.success(taskService.setTaskEvaluation(taskAssignmentDTD.getTaskId(), taskAssignmentDTD.getEvaluationDimensions()));
    }

    @GetMapping("/task/getPaperGrade")
    public Result getPaperGrade(@RequestParam("stuId") Integer stuId,@RequestParam("taskId") String taskId,@RequestParam("reviewType") String reviewType) throws IOException, ExecutionException, InterruptedException {
        return Result.success(taskService.getMachineGrade(stuId,taskId,reviewType));
    }
    @GetMapping("/task/getVideoGradeMachine")
    public Result getVideoGrade(Integer stuId,String taskId) throws JsonProcessingException, ExecutionException, InterruptedException {
        return Result.success(taskService.getMachineGradeVideo(stuId,taskId));
    }
    //评价任务

    @PostMapping("/task/setGrade")
    public Result setGrade(@RequestBody TaskAssignmentDTD taskAssignmentDTD){
        return Result.success(taskService.setGrade(taskAssignmentDTD.getTaskId(), taskAssignmentDTD.getStuId(),taskAssignmentDTD.getEvaluationType()));
    }

    @GetMapping("/task/getTaskInfoById")
    public Result getTaskInfoById(@RequestParam String taskId){
        return Result.success(taskService.getTaskInfoById(taskId));
    }

    @GetMapping("/task/getSubmissionInfoById")
    public Result getSubmissionInfoById(String submissionId){
        return Result.success(taskService.getSubmissionInfoById(submissionId));
    }

    @GetMapping("/task/getAllGradesByTaskId")
    public Result getAllGradesByTaskId(String taskId,int page,int pageSize){
        return Result.success(taskService.getAllGradesByTaskId(taskId,page,pageSize));
    }

    @GetMapping("/task/getAllSubmitsByTaskId")
    public Result getAllSubmitsByTaskId(String taskId,int page,int pageSize){
        return Result.success(taskService.getAllSubmitsByTaskId(taskId,page,pageSize));
    }
}
