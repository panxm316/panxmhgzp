package com.hgzp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.hgzp.core.web.BaseController;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建人：wangwk
 * 类描述：代码生成
 * 创建日期：2023/8/4 14:20
 */

public class Generator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.50.113:3306/hgcyaddb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
        String username = "yuqing";
        String password = "!Hg123456";
        String outputPath = System.getProperty("user.dir") + "/gencode2/";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(config -> {
                    config.author("muyn") // <- 这里可以修改作者
                            .outputDir(outputPath)
                            .dateType(DateType.ONLY_DATE)
                            .build();
                })
                .strategyConfig(config -> {
                    config.addInclude("^t.*"); // 仅限 t 开头的表
                    config.entityBuilder()
                            .enableLombok()
                            .enableChainModel()
                            .enableFileOverride()
                            .idType(IdType.ASSIGN_ID)
                            .build();
                    config.controllerBuilder()
                            .superClass(BaseController.class)
                            .enableFileOverride()
                            .enableRestStyle()
                            .formatFileName("%sController")
                            .build();
                    config.serviceBuilder()
                            .superServiceClass(IService.class)
                            .superServiceImplClass(ServiceImpl.class)
                            .enableFileOverride()
                            .formatServiceFileName("%sServiceI")
                            .formatServiceImplFileName("%sServiceImpl")
                            .build();
                    config.mapperBuilder()
                            .superClass(BaseMapper.class)
                            .enableFileOverride()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper")
                            .build();
                })
                .packageConfig(config -> {
                    config.parent("com.hgzp")
                            .entity("core.model")
                            .service("service.sys")
                            .serviceImpl("service.sys.impl")
                            .mapper("mapper.sys")
                            .xml("mapper.sys")
                            .build();
                })
                .injectionConfig(config -> {
                    // 设置自定义属性
                    Map<String, Object> map = new HashMap<>();
                    map.put("abc", 118); // 使用 ${cfg.abc} 获取
                    config.customMap(map);
                    config.customFile(file ->
                            file.fileName("DTO.java")
                                    .templatePath("/templates/dto.java.ftl")
                                    .filePath(outputPath + "/dto"));
                    config.customFile(file ->
                            file.fileName("VO.java")
                                    .templatePath("/templates/vo.java.ftl")
                                    .enableFileOverride()
                                    .filePath(outputPath + "/vo"));
                    config.customFile(file ->
                            file.fileName(".dart")
                                    .formatNameFunction((entityName) -> entityName.getEntityName().toLowerCase())
                                    .templatePath("/templates/DartModel.dart.ftl")
                                    .filePath(outputPath + "/dart"));
                    config.customFile(file ->
                            file.fileName(".d.ts")
                                    .templatePath("/templates/ts.java.ftl")
                                    .enableFileOverride()
                                    .filePath(outputPath + "/ts"));
                    config.customFile(file ->
                            file.fileName(".ts")
                                    .templatePath("/templates/api-ts.java.ftl")
                                    .enableFileOverride()
                                    .filePath(outputPath + "/api-ts"));
                    config.customFile(file ->
                            file.fileName(".vue")
                                    .templatePath("/templates/views.java.ftl")
                                    .enableFileOverride()
                                    .filePath(outputPath + "/views"));
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(config -> {
                    config.controller("templates/controller.java")
                            .service("templates/service.java")
                            .serviceImpl("templates/serviceImpl.java")
                            .entity("templates/entity.java")
                            .build();
                })
                .execute();
    }

}