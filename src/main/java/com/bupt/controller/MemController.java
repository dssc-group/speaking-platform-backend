package com.bupt.controller;

import com.bupt.pojo.Member;
import com.bupt.pojo.PageBean;
import com.bupt.pojo.Result;
import com.bupt.service.MemService;
import com.bupt.service.impl.MemServiceA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "https://localhost:1024")
@RestController
@Slf4j
public class MemController {
    @Autowired
    private MemServiceA memService;
    @RequestMapping("/getMembers")
    public Result getMembers(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             String stuIndex, String name, String mail){
        //memService.listMem()
        System.out.println(stuIndex);
        PageBean pageBean = memService.listMem(page,pageSize,stuIndex,name,mail);
        System.out.println(pageBean);
        return Result.success(pageBean);
    }

    @RequestMapping("/deleteMember/{id}")
    public Result deleteMember(@PathVariable Integer id){
        int state = memService.deleteMem(id);
        if(state==0){
            return Result.success("delete failed!");
        }
        else{
            return Result.success();
        }
    }


}
