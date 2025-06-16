package com.bupt.mapper;

import com.bupt.pojo.TaskAssignmentDTD;
import com.bupt.pojo.TeacherTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherTaskMapper {
    public int insertTask(TeacherTask teacherTask);
//    @Select("select * from test_db.taskInfo")
    public List<TeacherTask> getTeacherTasks(@Param("teacherId") Integer teacherId, @Param("taskName") String taskName);
    public int deleteTeacherTask(int id);
    public String deleteFilePath(int id);
    public int assignTask(@Param("taskId") int taskId,@Param("stuIds") List<Integer> stuIds); //分配任务
}
