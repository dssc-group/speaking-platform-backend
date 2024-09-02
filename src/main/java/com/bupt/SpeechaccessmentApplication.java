package com.bupt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan //开对JavaWeb组件的支持
@SpringBootApplication
public class SpeechaccessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeechaccessmentApplication.class, args);
    }

}
