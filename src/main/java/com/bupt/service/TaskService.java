package com.bupt.service;

import com.bupt.pojo.StudentTask;
import com.bupt.pojo.TeacherTask;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//返回任务数据、上传数据
public interface TaskService {
    public StudentTask getTask();
    public int uploadTask(MultipartFile file) throws IOException;
    public int uploadTeacherTask(TeacherTask teacherTask,MultipartFile file) throws IOException;
    public List<TeacherTask> getTeacherTasks() throws IOException;
    public int deleteTeacherTask(List<Integer> idList) throws IOException;
}
