package com.bupt.controller;

import com.bupt.pojo.Result;
import com.bupt.pojo.TeacherTask;
import com.bupt.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "https://localhost:1024")
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    @PostMapping ("/uploadTask")
    public Result uploadTask(MultipartFile file) throws IOException {
        int state = taskService.uploadTask(file);
        return Result.success(0);
    }
    @PostMapping ("/task/uploadTask")
    public Result uploadTeacherTask(@RequestParam(value = "file",required = false) MultipartFile file,
                                    @RequestParam("jsonData") String jsonData) throws IOException {

        TeacherTask teacherTask = new ObjectMapper().readValue(jsonData,TeacherTask.class);
        System.out.println(teacherTask);
        taskService.uploadTeacherTask(teacherTask,file);
        return Result.success(0);
    }
    @RequestMapping("/getTeacherTasks")
    public Result getTeacherTasks() throws IOException{
        return Result.success(taskService.getTeacherTasks());
    }
    @PostMapping("/deleteTeacherTask")
    public Result deleteTeacherTask(@RequestBody List<Integer> deleteList) throws IOException {
        taskService.deleteTeacherTask(deleteList);
        return Result.success(0);
    }
}
