package com.bupt.controller;

import com.bupt.pojo.Account;
import com.bupt.pojo.Login;
import com.bupt.pojo.Result;
import com.bupt.pojo.UserInfo;
import com.bupt.service.impl.AccountServiceA;
import com.bupt.service.impl.LoginCheckServiceA;
import com.bupt.utils.HttpUtils;
import com.bupt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.net.URLCodec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class LoginController {
    private static final URLCodec URL_CODEC = new URLCodec("UTF-8");
    @Autowired
    LoginCheckServiceA loginCheckServiceA;
    @Autowired
    AccountServiceA accountServiceA;
    @PostMapping("/login")
    //返回令牌
    public Result login(@RequestBody(required = false) Login user,@RequestParam(value = "ticket",required = false) String ticket) throws UnsupportedEncodingException {
        if(StringUtils.hasLength(ticket)) {
            return casLogin(ticket);
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",user.getUserName());
        Integer code = loginCheckServiceA.loginCheck(user);
        if(code == -2){
            return Result.error("用户不存在");
        }
        if(code == -1){
            return Result.error("密码错误");
        }
        //登录成功
        claims.put("roleId",code);
        String jwp = JwtUtils.genJwt(claims);
        return Result.success((Object) jwp);
    }

    private Result casLogin(String ticket) throws UnsupportedEncodingException {
        String casServerValidateUrl = "https://auth.bupt.edu.cn/authserver/serviceValidate";
        String serviceUrl = "http://123.56.183.160:8081/cas/callback"; // 你的服务地址
        String validateUrl = casServerValidateUrl + "?service=" + serviceUrl + "&ticket=" + ticket;
        // 1. 发送 GET 请求到 CAS 服务器，获取用户信息
        String casResponse = HttpUtils.get(validateUrl);
        String username = extractUserId(casResponse);
        String nickName = extractUserName(casResponse);
        if (username == null) {
            return Result.error("CAS 认证失败");
        }
        Login user = new Login(username,"");
        // 2. 检查本地数据库是否已有该用户
        Integer code = loginCheckServiceA.loginCheck(user);
        if (code == -2) {
            // 3. 如果用户不存在，则创建新用户
            Account account = new Account();
            account.setStudentId(username);
            account.setPassword("");
            account.setUserName(username);
            account.setNickName(nickName);
            accountServiceA.crateStudentAccount(account);
            code = 2;
        }

        // 4. 生成 JWT 并返回
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roleId", code);

        String jwt = JwtUtils.genJwt(claims);
        return Result.success((Object) jwt);
    }

    private String extractUserId(String casResponse) {
        Pattern pattern = Pattern.compile("<cas:user>(.*?)</cas:user>");
        Matcher matcher = pattern.matcher(casResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    private String extractUserName(String casResponse) {
        Pattern pattern = Pattern.compile("<cas:name>(.*?)</cas:name>");
        Matcher matcher = pattern.matcher(casResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    //确认令牌，返回用户信息
    //返回用户信息：包括用户名、用户角色、用户ID
    @RequestMapping("/login/verifyToken")
    public Result verifyToken(@RequestHeader("token") String token){
        Map<String,Object> claims = JwtUtils.parseJwt(token);
        System.out.println(claims);
        UserInfo userInfo = loginCheckServiceA.tokenCheck(claims);
        if(userInfo == null){
            return Result.error("error!");
        }
        return Result.success(userInfo);
    }


}
