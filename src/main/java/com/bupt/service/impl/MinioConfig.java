package com.bupt.service.impl;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.rootUser}")
    private String accessKey;

    @Value("${minio.rootPassword}")
    private String secretKey;

    @Bean
    public MinioService minioService() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return new MinioService(minioClient);
    }
}
