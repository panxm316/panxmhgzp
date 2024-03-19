package com.hgzp.flowable.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.hgzp.flowable.core")
public class CoreApp {
    public static void main(String[] args) {
        SpringApplication.run(CoreApp.class, args);
        log.info("=====================Core APP  Start========================");
    }
}
