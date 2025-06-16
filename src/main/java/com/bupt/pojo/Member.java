package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {
    private Integer userId;
    private String userName;
    private Integer roleId;
    private String nickName;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private LocalDateTime activeTime;
    private String mobile;
    private String studentId;
    private String school;
    private String major;
    private String dept;
    private String sex;
    public Member(Account account){
        this.userId = account.getUserId();
        this.userName = account.getUserName();
        this.roleId = account.getRoleId();
        this.nickName = account.getNickName();
        this.createTime = account.getCreateTime();
        this.modifyTime = account.getModifyTime();
        this.activeTime = account.getActiveTime();
        this.mobile = account.getMobile();
        this.studentId = account.getStudentId();
        this.school = account.getSchool();
        this.major = account.getMajor();
        this.dept = account.getDept();
        this.sex = account.getSex();
    }
}
