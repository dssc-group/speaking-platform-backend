package com.bupt.service;

import com.bupt.pojo.Login;
import com.bupt.pojo.UserInfo;

import java.util.Map;

public interface LoginService {
    public Integer loginCheck(Login user);
    public UserInfo tokenCheck(Map<String,Object> user);
}
