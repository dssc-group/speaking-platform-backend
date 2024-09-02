package com.bupt.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.bupt.pojo.Result;
import com.bupt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override//目标资源运行前运行、返回true则放行，
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
//        System.out.println(url);
        //登录操作
        if(url.contains("login")){
            return true;
        }
        String jwt = request.getHeader("token");
        //提示未登录的信息
        if(!StringUtils.hasLength(jwt)){
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }
        try {
            JwtUtils.parseJwt(jwt);
        }
        catch (Exception e){
            e.printStackTrace();
            Result error = Result.error("Session expired, please log in again.");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }
        //放行
        return true;
    }

    @Override//目标资源后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override//试图渲染后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
