package com.hgzp;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 启动类
 * @author: wangwk
 * @create：2023/8/2 16:10
 */

@Import(cn.hutool.extra.spring.SpringUtil.class)
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
@MapperScan("com.hgzp.mapper")
@EnableAsync
@ComponentScan(basePackages = {"com.hgzp"})
public class AdvertisingApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(AdvertisingApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AdvertisingApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}