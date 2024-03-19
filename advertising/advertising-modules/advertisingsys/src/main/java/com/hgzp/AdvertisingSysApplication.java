package com.hgzp;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 启动类
 * @author: wangwk
 * @create：2023/8/2 16:10
 */

@Import(cn.hutool.extra.spring.SpringUtil.class)
@EnableDiscoveryClient
@SpringBootApplication
@EnableEncryptableProperties
@MapperScan("com.hgzp.mapper")
@EnableAsync
//@ComponentScan(basePackages = {"com.hgzp"})
public class AdvertisingSysApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(AdvertisingSysApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AdvertisingSysApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}