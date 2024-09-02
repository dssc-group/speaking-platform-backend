package com.bupt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpeechaccessmentApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void testGenJwt(){
        Map<String ,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("name","tom");
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,"xiong")//签名算法
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 3600*1000))
                .compact();
        System.out.println(jwt);
    }
    @Test
    public void testParseJwt(){
        Claims claims = Jwts.parser()
                .setSigningKey("xiong")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidG9tIiwiaWQiOjEsImV4cCI6MTcwMjU0ODU5OH0.KyCB0XqVDQXWB7unmv3eUIogPlMPX8oQFs8eW_ToLwE")
                .getBody();
        System.out.println(claims);
    }
}
