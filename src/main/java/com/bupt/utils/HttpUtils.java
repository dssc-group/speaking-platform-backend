package com.bupt.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpUtils {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static String get(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
