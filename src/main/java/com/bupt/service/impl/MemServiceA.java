package com.bupt.service.impl;

import com.bupt.mapper.MemberMapper;
import com.bupt.pojo.Member;
import com.bupt.pojo.PageBean;
import com.bupt.service.MemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class MemServiceA implements MemService {
    @Autowired
    private MemberMapper memberMapper;
    //返回数据库所有成员
    public PageBean<Member> listMem(Integer page, Integer pageSize, String stuIndex, String name, String mail){
        PageHelper.startPage(page,pageSize);
        List<Member> memberList = memberMapper.list(stuIndex,name,mail);
        Page<Member> p = (Page<Member>) memberList;
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }
    //删除指定id的成员
    @CacheEvict(value = "users",key = "#id")
    public int deleteMem(Integer id){
        return memberMapper.delete(id);
    }
    //根据Id查询学生信息
    @Cacheable(value="users",key = "#id")
    public Member searchInfoById(Integer id) {
        return memberMapper.searchById(id);
    }
    @Caching(evict = {
        @CacheEvict(value = "users",key = "#member.userId"),
    })
    @Override
    public int updateStuInfo(Member member) {
        if(member.getUserName()!=null & memberMapper.searchMailExist(member.getUserName())!=0){
            return -1;
        }
        System.out.println(member);
        member.setModifyTime(LocalDateTime.now()); //设置更新时间
        return memberMapper.updateStuInfo(member);
    }
    @Override
//    @Cacheable(value = "users",key = "#userName")
    public Member getInfoByName(String userName) {

        return memberMapper.getInfoByName(userName);
    } //只取得userId，否则会缓存不一致

    @Override
    public PageBean<Member> getStuByIds(Integer page, Integer pageSize, List<Integer> stuIds, String stuIndex, String name, String mail) {
        PageHelper.startPage(page,pageSize);
        List<Member> memberList = memberMapper.getStuByIds(stuIds,stuIndex,name,mail);
        Page<Member> p = (Page<Member>) memberList;
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }
}
