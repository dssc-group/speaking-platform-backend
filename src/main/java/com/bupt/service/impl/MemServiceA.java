package com.bupt.service.impl;

import com.bupt.mapper.MemberMapper;
import com.bupt.pojo.Member;
import com.bupt.pojo.PageBean;
import com.bupt.service.MemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MemServiceA implements MemService {
    @Autowired
    private MemberMapper memberMapper;
    //返回数据库所有成员
    public PageBean listMem(Integer page, Integer pageSize, String stuIndex, String name, String mail){
        PageHelper.startPage(page,pageSize);
        List<Member> memberList = memberMapper.list(stuIndex,name,mail);
        Page<Member> p = (Page<Member>) memberList;
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }
    //删除指定id的成员
    public int deleteMem(Integer id){
        return memberMapper.delete(id);
    }

}
