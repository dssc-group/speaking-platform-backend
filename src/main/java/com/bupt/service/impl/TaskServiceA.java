package com.bupt.service.impl;

import com.bupt.mapper.TeacherTaskMapper;
import com.bupt.pojo.StudentTask;
import com.bupt.pojo.TeacherTask;
import com.bupt.service.TaskService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
@Service
public class TaskServiceA implements TaskService {
    @Autowired
    private TeacherTaskMapper teacherTaskMapper;
    @Value("${data.video.path}")
    private String videoHome;
    @Value("${data.teacherTask.path}")
    private String teacherTaskHome;
    private ObjectMapper mapper = new ObjectMapper();
    private String textHome = "/Users/xiongzihua/text/";
    private String pptHome = "/Users/xiongzihua/ppt/";
    public StudentTask getTask(){
        return null;
    }
    public String getNewFileName(MultipartFile file){
        String[] temFileList = file.getOriginalFilename().split("\\.");
        String fileSuffix = '.' + temFileList[temFileList.length-1];
        String newFileName = UUID.randomUUID() + fileSuffix;
        return newFileName;
    }
    public int uploadTask(MultipartFile file) throws IOException {
        String newFileName = this.getNewFileName(file);
        file.transferTo(new File(this.videoHome + newFileName));
        return 0;
    }
    public int uploadTeacherTask(TeacherTask teacherTask, MultipartFile file) throws IOException {
        String newFileName = "";
        if (file!=null) {
            newFileName = this.getNewFileName(file);
            file.transferTo(new File(this.teacherTaskHome + newFileName));
        }
        //创建时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String CreateTime = now.format(formatter);
        teacherTask.setCreateTime(CreateTime);
        //序列化JSON数据
        teacherTask.setSubmitContentJson(mapper.writeValueAsString(teacherTask.getSubmitContent()));
        teacherTask.setWeightsJson(mapper.writeValueAsString(teacherTask.getWeights()));
        teacherTask.setReviewSettingsJson(mapper.writeValueAsString(teacherTask.getReviewSettings()));
        teacherTask.setReviewProcessJson(mapper.writeValueAsString(teacherTask.getReviewProcess()));
        teacherTask.setFilePath(newFileName);
        int result = teacherTaskMapper.insertTask(teacherTask);
        System.out.println(result);
        return 0;
    }
    public List<TeacherTask> getTeacherTasks() throws IOException{
        List<TeacherTask> teacherTaskList = teacherTaskMapper.getTeacherTasks();
        for(TeacherTask teacherTask:teacherTaskList) {
            //反序列化字符串，还原JSON数据
            teacherTask.setWeights(mapper.readValue(teacherTask.getWeightsJson(), new TypeReference<List<Integer>>() {}));
            teacherTask.setSubmitContent(mapper.readValue(teacherTask.getSubmitContentJson(), new TypeReference<List<String>>(){}));
            teacherTask.setReviewProcess(mapper.readValue(teacherTask.getReviewProcessJson(), new TypeReference<List<String>>(){}));
            teacherTask.setReviewSettings(mapper.readValue(teacherTask.getReviewSettingsJson(), new TypeReference<List<String>>(){}));
            //解析时间数据
            if(teacherTask.getCreateTime()!=null && teacherTask.getDeadline()!=null){
//                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
//                DateTimeFormatter formattedCustomDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//                teacherTask.setCreateTime(LocalDateTime.parse(teacherTask.getCreateTime(), formatter).format(formattedCustomDateTime));
//                teacherTask.setDeadline(LocalDateTime.parse(teacherTask.getDeadline(), formatter).format(formattedCustomDateTime));
                DateTimeFormatter originalFormatter = DateTimeFormatter.ISO_DATE_TIME;
                DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                teacherTask.setCreateTime(LocalDateTime.parse(teacherTask.getCreateTime(), originalFormatter).format(customFormatter));

// 同样的步骤应用于deadline
                String deadlineStr = teacherTask.getDeadline();
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr, originalFormatter);
                ZonedDateTime deadlineBeijing = deadline.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
                String formattedDeadline = deadlineBeijing.format(customFormatter);
                teacherTask.setDeadline(formattedDeadline);
            }
        }
        return teacherTaskList;
    }
    public int deleteTeacherTask(List<Integer> deleteList) throws IOException{
        for(int id:deleteList){
            String filePath = teacherTaskMapper.deleteFilePath(id);
            File file = new File(teacherTaskHome+filePath);
            if(file.exists()) {
                boolean deleted = file.delete();
            }
            teacherTaskMapper.deleteTeacherTask(id);
        }
        return 0;
    }
}
