package com.bupt.mapper;

import com.bupt.pojo.ApplicationApproval;
import com.bupt.pojo.Classes;
import com.bupt.pojo.Member;
import com.bupt.pojo.StudentApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassMapper {
    public Integer createClass(Classes myClass);
    public List<Classes> searchClasses(int userId);
    public int deleteClass(int classId);
    public int updateEnable(int classId);
    public int studentApplication(StudentApplication studentApplication);
    public List<StudentApplication> listApplication(int teacherId);
    //批量修改待同意待同意的学生申请
    public int updateApplicationStatus(@Param("approvals") List<ApplicationApproval> approvals);
    //批量搜索ApplicationId
    public List<StudentApplication> searchByIdBatch(@Param("approvalList") List<Integer> approvalList);
    //批量加入班级
    public int batchInsertStudent(@Param("applications") List<StudentApplication> applications);
    public int addStudent(@Param("stuId") Integer stuId,@Param("classId") Integer classId);
    public List<Integer> listStudentIds(int classId);
    public List<Member> listStudentsInfos(@Param("studentIds") List<Integer> studentIds,@Param("stuIndex") String stuIndex, @Param("name") String name,
                                          @Param("mail") String mail);
    public List<Classes> listClassInfos(@Param("classIds") List<Integer> classIds);
    public List<Integer> classBelongs(Integer studentId);

    int deleteStuByIds(@Param("classId") Integer classId, @Param("deleteStuIds") List<Integer> deleteStuIds);

    List<Classes> getAllClassInfos(@Param("className") String className,@Param("teacherName") String teacherName,@Param("term") String term);

    List<StudentApplication> getApplicationStatus(Integer stuId);

    List<StudentApplication> getApplicationsByClassId(Integer classId);
}
