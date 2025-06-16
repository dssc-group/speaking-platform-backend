package com.bupt.service.impl;

import com.bupt.mapper.LoginMapper;
import com.bupt.pojo.Login;
import com.bupt.pojo.UserInfo;
import com.bupt.service.LoginService;
import com.bupt.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginCheckServiceA implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public Integer loginCheck(Login user) {
        UserInfo myInfo = loginMapper.loginCheck(user);
        if (myInfo == null) {
            return -2; //用户名不存在
        }
        if (PasswordUtils.matchPassword(user.getPassword(),myInfo.getPassword()) == false) {
            return -1; //密码不正确
        }

        return myInfo.getRoleId(); //用户名&密码正确
    } //登陆检查
    public UserInfo tokenCheck(Map<String,Object> user) {
        return loginMapper.tokenCheck(user);
    } //令牌检查
}
