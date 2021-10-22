package com.deo.activitipro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@RestController
public class ActivitiProApplication{

    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(ActivitiProApplication.class, args);
    }
}
