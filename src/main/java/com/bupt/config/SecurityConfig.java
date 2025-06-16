package com.bupt.config;

import com.bupt.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors() // 确保 CORS 配置被应用
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 允许所有的 OPTIONS 请求
                .antMatchers("/login").permitAll()
                .antMatchers("/create/**").permitAll()
                .antMatchers("/deleteMember/**").hasAnyRole("TEACHER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://10.112.132.247:80/"));
        configuration.setAllowedOriginPatterns(Collections.singletonList("*")); // 更安全的方式，允许所有来源模式匹配
        configuration.setAllowedMethods(Arrays.asList("GET", "POST")); // 设置允许的方法
        configuration.setAllowedHeaders(Collections.singletonList("*")); // 设置允许的请求头
        configuration.setAllowCredentials(true); // 允许发送凭据

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
