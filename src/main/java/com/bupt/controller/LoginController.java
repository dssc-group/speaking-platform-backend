package com.bupt.controller;

import com.bupt.pojo.Login;
import com.bupt.pojo.Result;
import com.bupt.pojo.UserInfo;
import com.bupt.service.impl.LoginCheckServiceA;
import com.bupt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "https://localhost:1024")
public class LoginController {
    @Autowired
    LoginCheckServiceA loginCheckServiceA;
    @PostMapping("/login")
    //返回令牌
    public Result login(@RequestBody Login user){
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",user.getUserName());
        claims.put("password",user.getPassword());
        Integer userInfo = loginCheckServiceA.loginCheck(user);
        if(userInfo==0){
            return Result.error("用户不存在");
        }
        //登录成功
        String jwp = JwtUtils.genJwt(claims);
        return Result.success((Object) jwp);
    }
    //确认令牌，返回用户信息
    @CrossOrigin(origins = "https://localhost:1024")
    @RequestMapping("/login/verifyToken")
    public Result verifyToken(@RequestHeader("token") String token){
        Map<String,Object> claims = JwtUtils.parseJwt(token);
        UserInfo userInfo = loginCheckServiceA.tokenCheck(claims);
        if(userInfo == null){
            return Result.error("用户不存在！");
        }
        return Result.success(userInfo);
    }

}
