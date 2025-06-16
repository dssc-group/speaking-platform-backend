package com.bupt.service.impl;

import com.bupt.mapper.AccountMapper;
import com.bupt.pojo.Account;
import com.bupt.pojo.Member;
import com.bupt.service.AccountService;
import com.bupt.service.MemService;
import com.bupt.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountServiceA implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private MemServiceA memServiceA;

    public int crateStudentAccount(Account myAccount) {
        if(memServiceA.getInfoByName(myAccount.getUserName())!=null){
            return -1; //用户名已存在
        }
        Integer userId = createAccount(myAccount,2).getUserId() ;
        return userId;
    }

    public int createTeacherAccount(Account myAccount) {
        if(memServiceA.getInfoByName(myAccount.getUserName())!=null){
            return -1; //用户名已存在
        }
        Integer userId = createAccount(myAccount,1).getUserId() ;
        return userId;
    };

    public int createAdminAccount(Account myAccount) {
        if(memServiceA.getInfoByName(myAccount.getUserName())!=null){
            return -1; //用户名已存在
        }
        Integer userId = createAccount(myAccount,0).getUserId() ;
        return userId;
    }



    @Override
    public Member createAccount(Account myAccount, Integer roleId) {
        myAccount.setPassword(PasswordUtils.encodePassword(myAccount.getPassword()));
        myAccount.setCreateTime(LocalDateTime.now());
        myAccount.setActiveTime(LocalDateTime.now());
        myAccount.setRoleId(roleId);
        accountMapper.createAccount(myAccount);
        return new Member(myAccount);
    }

    @Override
    public int batchAccount() {
        return 0;
    }

    @Override
    public int changePassword(int userId, String oldPassword, String newPassword) {
        String tureEncPassword = accountMapper.searchPassWordById(userId);
        if(PasswordUtils.matchPassword(oldPassword,tureEncPassword)==false){
            return -1; //密码错误
        }
        String newEncPassword = PasswordUtils.encodePassword(newPassword);
        accountMapper.changePassword(newEncPassword,userId);
        return accountMapper.changePassword(newPassword,userId);
    }
}
