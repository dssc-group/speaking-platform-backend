package com.bupt.filter;

import com.alibaba.fastjson.JSONObject;
import com.bupt.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.bupt.utils.JwtUtils;

@Slf4j
public class JwtTokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 处理 OPTIONS 请求，直接返回 200 OK
        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }

        String jwt = httpServletRequest.getHeader("token");
        if (jwt != null) {
            try {
                if (JwtUtils.parseJwt(jwt) != null) {  // 假设 JwtUtils 有一个 validateToken 方法返回 boolean
                    Map<String, Object> claims = JwtUtils.parseJwt(jwt);
                    String username = (String) claims.get("username");
                    Integer roleId = (Integer) claims.get("roleId");
                    var auth = getUsernamePasswordAuthenticationToken(roleId, username);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                log.error("JWT Token validation error", e);
                Result error = Result.error(401,"JWT expired");
                String notLogin = JSONObject.toJSONString(error);
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getWriter().write(notLogin);
                return;
            }
        }

        // 如果没有 JWT，或者 JWT 验证成功，继续过滤链
        chain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Integer roleId, String username) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        switch (roleId) {
            case 2:
                authority = new SimpleGrantedAuthority("ROLE_STUDENT");
                break;
            case 1:
                authority = new SimpleGrantedAuthority("ROLE_TEACHER");
                break;
            case 0:
                authority = new SimpleGrantedAuthority("ROLE_ADMIN");
                break;
        }
        return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));
    }
}
