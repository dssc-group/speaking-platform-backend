package com.bupt.service;

import com.bupt.pojo.Member;
import com.bupt.pojo.PageBean;

import java.util.List;
//返回成员数据
public interface MemService {
    public PageBean listMem(Integer start, Integer pageSize,String stuIndex, String name, String mail);
    public  int deleteMem(Integer id);
}
