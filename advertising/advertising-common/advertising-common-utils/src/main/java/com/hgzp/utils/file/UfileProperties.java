package com.hgzp.utils.file;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.utils.file.UfileProperties
 * 创建人：wangwk
 * 类描述：统一文件配置文件
 * 创建日期：2022/10/11 15:52
 */
@Configuration
@ConfigurationProperties(prefix = "ufile")
@RefreshScope
@Data
public class UfileProperties {

    private String uFileURL;
    private String uFileURLOut;
    private String uFileExists;
    private String uWebURL;
    private String uFileDown;
    private String uExtURL;
    private String uPermanent;
    private String videoURL;
    private String videoScreenShot;
    private String videoTransWaterMark;
    private String videoTranprogress;
    private boolean defaultmode0;
    private String videoFileDown;
    private String saveFileUrl;
    private String mediaInfoUrl;
    private String fileInfoUrl;
    private String getscreenshotsha1;



    @Value("${spring.rabbitmq.exchange:ufile.yxt1}")
    private String exchange;


}
