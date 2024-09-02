package com.bupt.mapper;

import com.bupt.pojo.TeacherTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherTaskMapper {
    public int insertTask(TeacherTask teacherTask);
//    @Select("select * from test_db.taskInfo")
    public List<TeacherTask> getTeacherTasks();
    public int deleteTeacherTask(int id);
    public String deleteFilePath(int id);
}
