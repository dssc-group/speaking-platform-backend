package com.bupt.service.impl;

import com.bupt.mapper.ClassMapper;
import com.bupt.mapper.MemberMapper;
import com.bupt.pojo.*;
import com.bupt.service.ClassService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassServiceA implements ClassService {
    @Autowired
    private ClassMapper classMapper;

    public int createClass(Classes myClass){
        myClass.setEnable(true);
        int code = classMapper.createClass(myClass);
        return code;
    }

    @Override
    public PageBean<Classes> searchClasses(int page, int pageSize, int userId) {
        PageHelper.startPage(page,pageSize);
        List<Classes> myClasses = classMapper.searchClasses(userId); //薛定谔的🐱
        for(int i=0;i<myClasses.size();i++) {
            Classes myClass = myClasses.get(i);
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime deadline = myClass.getDeadline();
            if (myClass.isEnable() && currentDateTime.isAfter(deadline)) { //是否有效
                myClass.setEnable(false);
                classMapper.updateEnable(myClass.getClassId());
            }
        }
//        List<Member> memberList = memberMapper.list(stuIndex,name,mail);
        Page<Classes> p = (Page<Classes>) myClasses;
        PageBean<Classes> pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    @Override
    public int deleteClass(int classId) {
        int msg = classMapper.deleteClass(classId);
        return msg;
    }
    public  int addApplication(StudentApplication studentApplication){
        studentApplication.setApplicationDate(LocalDateTime.now());
        studentApplication.setApplicationStatus(StudentApplication.ApplicationStatus.pending);
        int msg = classMapper.studentApplication(studentApplication);
        return msg;
    }
    //是否需要保证原子性
    @Override
    public int addStudent(List<ApplicationApproval> applicationIds) {
        for(ApplicationApproval applicationApproval:applicationIds){
            applicationApproval.setResponseDate(LocalDateTime.now());
        }

        classMapper.updateApplicationStatus(applicationIds);
        List<Integer> addStudents = new ArrayList<>();
        for(ApplicationApproval applicationApproval:applicationIds){
            if(applicationApproval.getApprovalStatus().equals("approved")){
                addStudents.add(applicationApproval.getApplicationId());
            }
        }
        int msg = -1;
        if(!addStudents.isEmpty()){
            List<StudentApplication> addList = classMapper.searchByIdBatch(addStudents);
            msg = classMapper.batchInsertStudent(addList);
        }
        else{
            msg = -1;
        }
        return msg;
    }



    @Override
    public PageBean<StudentApplication> listApplication(int page, int pageSize, int teacherId) {
        PageHelper.startPage(page,pageSize);
        List<StudentApplication> applicationList = classMapper.listApplication(teacherId);
        Page<StudentApplication> p = (Page<StudentApplication>) applicationList;
        PageBean<StudentApplication> pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }
    //查询一个班级的学生信息
    @Override
    public PageBean<Member> listMem(Integer page, Integer pageSize, Integer classId, String stuIndex, String name, String mail) {
        PageHelper.startPage(page,pageSize);
        List<Integer> studentIds = classMapper.listStudentIds(classId);
        if(studentIds.isEmpty()){
            PageInfo<Member> p = new PageInfo<>(new ArrayList<>());
            return new PageBean<>(p.getTotal(),p.getList());
        }

        List<Member> studentInfos = classMapper.listStudentsInfos(studentIds,stuIndex,name,mail);
        PageInfo<Member> p = new PageInfo<>(studentInfos);
        PageBean<Member> pageBean = new PageBean<>(p.getTotal(),p.getList());
        return pageBean;
    }

    @Override
    public PageBean<Classes> classInfoByStuId(int page, int pageSize,int studentId) {
        List<Integer> classIds = classMapper.classBelongs(studentId);
        if(classIds.isEmpty()){
            return null;
        }
        PageHelper.startPage(page,pageSize);
        List<Classes> classInfos = classMapper.listClassInfos(classIds);
        //PageInfo<Member> p = new PageInfo<>(studentInfos);
        PageInfo<Classes> p = new PageInfo<>(classInfos);
        PageBean<Classes> pageBean = new PageBean<>(p.getTotal(),p.getList());
        return pageBean;
    }

    @Override
    public int deleteStuByIds(Integer classId, List<Integer> deleteStuIds) {

        return classMapper.deleteStuByIds(classId,deleteStuIds);
    }

    @Override
    public PageBean<Classes> getAllClasses(Integer page, Integer pageSize, String className, String teacherName, String term) {
        PageHelper.startPage(page,pageSize);
        List<Classes> classInfos = classMapper.getAllClassInfos(className,teacherName,term);
        PageInfo<Classes> p = new PageInfo<>(classInfos);
        PageBean<Classes> pageBean = new PageBean<>(p.getTotal(),p.getList());
        return pageBean;
    }

    @Override
    public List<StudentApplication> searchApplicationStatus(Integer stuId) {

        return  classMapper.getApplicationStatus(stuId);
    }

    @Override
    public List<StudentApplication> getApplicationsByClassId(Integer classId) {
        return classMapper.getApplicationsByClassId(classId);
    }

}
