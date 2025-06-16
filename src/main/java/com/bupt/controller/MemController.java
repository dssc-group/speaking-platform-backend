package com.bupt.controller;

import com.bupt.pojo.Member;
import com.bupt.pojo.MemberDTO;
import com.bupt.pojo.PageBean;
import com.bupt.pojo.Result;
import com.bupt.service.impl.MemServiceA;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.hibernate.type.StringNVarcharType;
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
        PageBean<Member> pageBean = memService.listMem(page,pageSize,stuIndex,name,mail);
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

    @RequestMapping("/member/getInfoById")
    public Result getInfoById(int userId){
        return Result.success(memService.searchInfoById(userId));
    }

    @PostMapping("/member/updateInfo")
    public Result updateInfo(@ModelAttribute Member member){
        return Result.success(memService.updateStuInfo(member));
    }

    @PostMapping("/members/getStuByIds")
    public Result getStuByIds(@RequestBody MemberDTO memberDTO){
        return Result.success(memService.getStuByIds(memberDTO.getPage(),
                memberDTO.getPageSize(), memberDTO.getStuIds(), memberDTO.getStuIndex(),
                memberDTO.getName(), memberDTO.getMail()));
    }

}
