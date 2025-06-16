package com.bupt.controller;

import com.bupt.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class FileDownloadController {
    @Value("${data.ppt.path}")
    private String pptPath;

    @Value("${data.teacherTask.path}")
    private String teacherTaskPath;

    @Value("${data.video.path}")
    private String videoPath;

    @Value("${data.audio.path}")
    private String audioPath;

    @Value("${data.word.path}")
    private String wordPath;

    @RequestMapping("/download")
    public ResponseEntity<Resource> downloadFile(String fileType,String fileName) {
        Path filePath = Paths.get(getFilePath(fileType) + fileName); //返回对应文件的路径
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri()); //转化为可以下载的Resource对象
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String contentType = getContentType(fileType); //确定文件的 Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
        //创建一个 HttpHeaders 对象，并设置 Content-Disposition 标头。Content-Disposition 用来告诉浏览器以附件形式下载文件，而不是在浏览器中直接显示

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(resource);
    }

    private String getFilePath(String fileType) {
        // 根据文件类型返回路径，这里使用本地文件路径
        switch (fileType.toLowerCase()) {
            case "ppt":
                return pptPath;
            case "word":
                return wordPath;
            case "video":
                return videoPath;
            case "audio":
                return audioPath;
            default:
                return teacherTaskPath;
        }
    }

    private String getContentType(String fileType) {
        // 根据文件类型设置 Content-Type
        switch (fileType.toLowerCase()) {
            case "ppt":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "word":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "video":
                return "video/mp4";
            case "audio":
                return "audio/mpeg";
            default:
                return "application/octet-stream";
        }
    }

    @GetMapping("/video/info/{fileName}")
    //获取视频信息
    public Result getVideoInfo(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(videoPath,fileName); //获取视频地址
        Resource resource = new UrlResource(filePath.toUri());
        if(!resource.exists()){
            return Result.error("video Not Found!");
        }
        long fileSize = resource.contentLength();
        Map<String, Object> videoInfo = new HashMap<>();
        videoInfo.put("fileName",fileName);
        videoInfo.put("fileSize",fileSize);
        videoInfo.put("contentType","video/mp4");
        return Result.success(videoInfo);

    }
    //返回视频 的断点续传方法
    @GetMapping("/video/{fileName}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String fileName, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            // 确定文件路径
            Path filePath = Paths.get(videoPath, fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 确定文件长度
            long fileSize = resource.contentLength();

            // 设置默认的开始和结束位置
            long rangeStart = 0;
            long rangeEnd = fileSize - 1;

            // 如果请求头中包含 Range
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] ranges = rangeHeader.substring(6).split("-");
                try {
                    // 获取开始和结束位置
                    rangeStart = Long.parseLong(ranges[0]);
                    if (ranges.length > 1) {
                        rangeEnd = Long.parseLong(ranges[1]);
                    }
                } catch (NumberFormatException e) {
                    return new ResponseEntity<>(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
                }
            }

            // 处理超过文件范围的请求
            if (rangeStart > rangeEnd || rangeEnd >= fileSize) {
                return new ResponseEntity<>(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
            }

            long contentLength = rangeEnd - rangeStart + 1;
            InputStream inputStream = resource.getInputStream();
            inputStream.skip(rangeStart);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");

            // 返回部分内容
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentLength(contentLength)
                    .contentType(org.springframework.http.MediaType.parseMediaType("video/mp4"))
                    .body(new InputStreamResource(inputStream));

        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/showFile")
    public ResponseEntity<Resource> showFile(String fileType, String fileName) {
        Path filePath = Paths.get(getFilePath(fileType) + fileName); // 返回对应文件的路径
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri()); // 转化为可以下载的Resource对象
            if (!resource.exists() || !resource.isReadable()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String contentType = getContentType(fileType); // 确定文件的 Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"");
        // 设置为 inline，以便浏览器尝试直接显示文件，而不是下载

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(resource);
    }


}
