package com.bupt.service.impl;

import com.bupt.mapper.LoginMapper;
import com.bupt.pojo.Login;
import com.bupt.pojo.UserInfo;
import com.bupt.service.LoginService;
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
        return loginMapper.loginCheck(user);
    }
    public UserInfo tokenCheck(Map<String,Object> user) {
        return loginMapper.tokenCheck(user);
    }
}
