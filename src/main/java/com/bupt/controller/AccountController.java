package com.bupt.controller;

import com.bupt.pojo.Account;
import com.bupt.pojo.Result;
import com.bupt.service.impl.AccountServiceA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AccountController {
    @Autowired
    private AccountServiceA accountServiceA;
    @PostMapping("/create/StudentAccount")
    public Result createStudentAccount(@ModelAttribute Account myAccount){
        int msg = accountServiceA.crateStudentAccount(myAccount);
        if(msg == -1){
            return Result.error("用户名已存在");
        }
        return Result.success("创建完成");
    }

    @PostMapping("/create/TeacherAccount")
    public Result createTeacherAccount(@ModelAttribute Account myAccount){
        int msg = accountServiceA.createTeacherAccount(myAccount);
        if(msg == -1){
            return Result.error("用户名已存在");
        }
        return Result.success("创建完成");
    }

    @PostMapping("/create/AdminAccount")
    public Result createAdminAccount(@ModelAttribute Account myAccount){
//        System.out.println(myAccount);
        int msg = accountServiceA.createTeacherAccount(myAccount);
        if(msg == -1){
            return Result.error("用户名已存在");
        }
        return Result.success("创建完成");
    }
    @RequestMapping("/account/changePassword")
    public Result changePassword(@RequestParam Integer userId,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword){
        int msg = accountServiceA.changePassword(userId,oldPassword,newPassword);
        if (msg==-1){
            Result.error("密码错误");
        }
        return Result.success("修改密码成功");
    }
}
