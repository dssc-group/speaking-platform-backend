package com.bupt.service;

import com.bupt.pojo.Account;
import com.bupt.pojo.Member;

public interface AccountService {
    public Member createAccount(Account myAccount, Integer roleId);

    public int batchAccount();//返回创建成功的用户个数
    public int changePassword(int userId,String oldPassword, String newPassword);
}
