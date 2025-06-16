package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class JsonRequest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleAndContent{
        private String role;
        private String content;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class response_format {
        private String type;
    }

    @Value("${LLM.model}")
    private String model;
    @Value("${LLM.stream}")
    private boolean stream;
    @Value("${LLM.top_p}")
    private float top_p;
    private List<RoleAndContent> messages;
    private response_format response_format = new response_format("text"); //json_object text
}
