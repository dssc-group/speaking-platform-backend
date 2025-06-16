package com.bupt.service;

import com.bupt.pojo.*;

import java.util.List;
import java.util.Map;

public interface ClassService {
    public int createClass(Classes myClass); //创建班级
    public PageBean<Classes> searchClasses(int page,int pageSize,int userId);
    public int deleteClass(int classId);
    public int addApplication(StudentApplication studentApplication);
    public int addStudent(List<ApplicationApproval> applicationIds);
    PageBean<StudentApplication> listApplication(int page, int pageSize, int teacherId);

    PageBean<Member> listMem(Integer page, Integer pageSize, Integer classId, String stuIndex, String name, String mail);
    public PageBean<Classes> classInfoByStuId(int page,int pageSize,int studentId);

    int deleteStuByIds(Integer classId, List<Integer> deleteStuIds);

    PageBean<Classes> getAllClasses(Integer page, Integer pageSize, String className, String teacherName, String term);

    List<StudentApplication> searchApplicationStatus(Integer stuId);

    List<StudentApplication> getApplicationsByClassId(Integer classId);
}
