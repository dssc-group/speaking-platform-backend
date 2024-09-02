package com.bupt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static String sighKey = "xiongzihua";
    private static Long expire = 43200000L;

    public static String genJwt(Map<String, Object> claims){
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,sighKey)//签名算法
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+ 3600*1000*12))
                .compact();
        return jwt;
    }

    public static Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(sighKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}