package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int userId;
    private String userName;
    private String password;
    private int roleId;
    private String nickName;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private String mobile;
    private LocalDateTime activeTime;
    private String studentId;
    private String school;
    private String classId;
    private String major;
    private String dept;
    private String sex;
    private Integer attendClassId;
}
