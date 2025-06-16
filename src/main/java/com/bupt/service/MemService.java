package com.bupt.service;

import com.bupt.pojo.Member;
import com.bupt.pojo.PageBean;

import java.util.List;
//返回成员数据
public interface MemService {
    public PageBean<Member> listMem(Integer start, Integer pageSize,String stuIndex, String name, String mail);
    public  int deleteMem(Integer id);
    public Member searchInfoById(Integer id); //搜索信息
    public int updateStuInfo(Member member); //更新信息
    public Member getInfoByName(String userName);
    public  PageBean<Member> getStuByIds(Integer page, Integer pageSize, List<Integer> stuIds, String stuIndex, String name, String mail);
}
