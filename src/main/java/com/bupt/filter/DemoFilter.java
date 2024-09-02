package com.bupt.filter;

import com.alibaba.fastjson.JSONObject;
import com.bupt.pojo.Result;
import com.bupt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
//@WebFilter(urlPatterns = "/* ") //过滤所有界面
public class DemoFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse responce = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI().toString();
        log.info("请求的url{}",url);
        //登录操作
        if(url.contains("login")){
            log.info("放行");
            filterChain.doFilter(servletRequest,servletResponse);
            return ;
        }
        String jwt = request.getHeader("token");
        //提示未登录的信息
        if(!StringUtils.hasLength(jwt)){
            log.info("未登录！");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            servletResponse.getWriter().write(notLogin);
            return ;
        }
        try {
            JwtUtils.parseJwt(jwt);
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("解析令牌失败");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            servletResponse.getWriter().write(notLogin);
            return ;
        }
        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
