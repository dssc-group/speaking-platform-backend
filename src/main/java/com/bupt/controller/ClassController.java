package com.bupt.controller;

import com.bupt.pojo.*;
import com.bupt.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ClassController {
    @Autowired
    ClassService classService;
    @PostMapping("/class/createClass")
    public Result createClass(@ModelAttribute Classes myClass){ //处理表单
        int msg = classService.createClass(myClass);
        if(msg==0){
            return Result.error("创建失败");
        }
        return Result.success();
    }

    @RequestMapping("/class/getClasses")
    public Result getClasses(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             int teacherId){
        //memService.listMem()
        PageBean<Classes> pageBean = classService.searchClasses(page,pageSize,teacherId);
        return Result.success(pageBean);
    }

    @RequestMapping("/class/deleteClass")
    public Result deleteClass(int classId){
        int msg = classService.deleteClass(classId);
        if (msg==0){
            return Result.error("删除失败");
        }
        return Result.success();
    }

    @PostMapping("/class/applyClass")
    public Result addApplication(@ModelAttribute StudentApplication studentApplication){
        int msg = classService.addApplication(studentApplication);
        if (msg==0){
            return Result.error("申请失败");
        }
        return Result.success();
    };
    @RequestMapping("/class/listApplication") //列出当前班级申请
    public Result listApplication(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  Integer teacherId){
        PageBean<StudentApplication> pageBean = classService.listApplication(page,pageSize,teacherId);
        return Result.success(pageBean);
    }

    @PostMapping("/class/approveApplications")
    public Result approveApplications(@RequestBody List<ApplicationApproval> approvals){
        int msg = classService.addStudent(approvals);
        return Result.success(msg);
    }

    @RequestMapping("/class/getMembers")
    public Result getMembers(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Integer classId, String stuIndex, String name, String mail){
        //memService.listMem()
        PageBean<Member> pageBean = classService.listMem(page,pageSize,classId,stuIndex,name,mail);
        return Result.success(pageBean);
    }
    @RequestMapping("/class/stuBelongs")
    public Result stuBelongs(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Integer studentId){
        PageBean<Classes> pageBean = classService.classInfoByStuId(page,pageSize,studentId);
        return Result.success(pageBean);
    }

    @PostMapping ("/class/deleteStuByIds")
    public Result deleteStuByIds(@RequestBody ClassDTD classDTD){
        return Result.success("成功删除:" + classService.deleteStuByIds(classDTD.getClassId(),classDTD.getDeleteStuIds()));
    }

    @RequestMapping("/class/getAllClasses")
    public Result getAllClasses(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                String className, String teacherName, String term){
        return Result.success(classService.getAllClasses(page,pageSize,className,teacherName,term));
    }
    @RequestMapping("/class/searchApplicationStatus")
    public Result searchApplicationStatus(Integer stuId){
        return Result.success(classService.searchApplicationStatus(stuId));
    }

    @RequestMapping("/class/getApplicationsByClassId")
    public Result getApplicationsByClassId(Integer classId){
        return Result.success(classService.getApplicationsByClassId(classId));
    }

}
