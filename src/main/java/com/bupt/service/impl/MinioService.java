package com.bupt.service.impl;

import io.minio.MinioClient;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.errors.MinioException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioService {
    private final MinioClient minioClient;
    // 构造函数注入 MinioClient
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    // 创建存储桶
    public void createBucket(String bucketName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("Bucket created successfully: " + bucketName);
        } else {
            System.out.println("Bucket already exists: " + bucketName);
        }
    }

    // 上传 MultipartFile 文件
    public void uploadFile(String bucketName, String objectName, MultipartFile file) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        createBucket(bucketName); // 确保存储桶存在
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1) // 文件流、大小
                            .contentType(file.getContentType())       // 设置内容类型
                            .build()
            );
            System.out.println("File uploaded successfully: " + objectName);
        }
    }

    // 获取文件流
    public InputStream getFile(String bucketName, String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

}
