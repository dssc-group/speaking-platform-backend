package com.bupt.service.impl;
import com.bupt.pojo.EvaluationDimension;
import com.bupt.pojo.JsonRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//大模型评分
@Service
@Slf4j
public class HttpBigModel {
    @Value("${LLM.apiPassword}")
    private String apiPassword;
    @Value("${LLM.contentType}")
    private String contentType;
    @Value("${LLM.requestUrl}")
    private String requestUrl;
//    @Value("${LLM.prompt}")
    private static String prompt = "现在你是一个专业的英语演讲评稿人，请深度理解user给你的英文演讲稿，在充分阅读演讲稿的情况下给出演讲稿的评分$，评分的范围是[0-100]，以及给出这个评分的原因$$" +
        "回答格式：{\"grade\":$,\"reason\":\"$$\"}";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JsonRequest jsonRequest;
    public  JsonNode evaluateSpeech(String speechContent, EvaluationDimension.EvaluationTitles evaluationTitles) {
        try {
            String authorization = "Bearer" + " " + apiPassword;
            // 创建 HttpClient 实例
            HttpClient client = HttpClient.newHttpClient();
            List<JsonRequest.RoleAndContent> requestContents = new ArrayList<>();
            // 创建请求
            // 获得评价要求
            String evaluationTitle = evaluationTitles.getEvaluationTitle();
            List<String> evaluationInfos = evaluationTitles.getEvaluationInfos();
            //编写Prompt
//            String prompt = "现在你是一个专业的英语演讲评稿人，请深度理解user给你的英文演讲稿，在充分阅读演讲稿的情况下分别给出演讲稿的总评分$0,评价总维度是:<" + evaluationTitle + ">.";
            int i = 1;
            List<Integer> numbers = new ArrayList<>();
            numbers.add(0);
            for(String evaluationInfo:evaluationInfos){
                prompt += "演讲的分维度<" + evaluationInfo + ">的评分" + "$" +  String.valueOf(i) + ".";
                numbers.add(i);
                i++;
            }
//            prompt += "以及给出各个评分的原因$$。";
            String formattedString = numbers.stream().map(number->"$"+number).collect(Collectors.joining(","));
//            prompt += "回答格式:{\"grade\":[" +formattedString + "]," + "\"reason\":\"$$\"}";
            ////
            JsonRequest.RoleAndContent sysAndPrompt = new JsonRequest.RoleAndContent("system",prompt);
            JsonRequest.RoleAndContent userAndContent = new JsonRequest.RoleAndContent("user",speechContent);
            requestContents.add(sysAndPrompt);
            requestContents.add(userAndContent);
            jsonRequest.setMessages(requestContents);
            String jsonString = objectMapper.writeValueAsString(jsonRequest);
            System.out.println(jsonString);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(requestUrl))
                    .header("Content-Type", contentType)
                    .header("Authorization", authorization)
                    .POST(BodyPublishers.ofString(jsonString, StandardCharsets.UTF_8))
                    .build();

            // 发送请求并接收响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            JsonNode jsonNode = objectMapper.readTree(response.body());
//            String content = jsonNode.path("choices").get(0)
//                                     .path("message")
//                                     .path("content")
//                                     .asText();
            // 打印响应状态码和响应内容
            String responseBody = response.body();
            System.out.println(responseBody);
            JsonNode root = objectMapper.readTree(responseBody);
            String contentString = root.get("choices").get(0).get("message").get("content").asText();
            JsonNode contentJson = objectMapper.readTree(contentString);

            return contentJson;

        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    }
}
