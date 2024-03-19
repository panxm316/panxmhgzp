package com.hgzp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.hgzp.core.web.BaseController;

/**
 * 创建人：wangwk
 * 类描述：代码生成
 * 创建日期：2023/8/4 14:20
 */

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.50.113:3306/hgcyaddb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
        String username = "yuqing";
        String password = "!Hg123456";
        String outputPath = System.getProperty("user.dir") + "/gencode/";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(config -> {
                    config.author("wwk")
                            .outputDir(outputPath)
                            .dateType(DateType.ONLY_DATE)
                            .build();
                })
//                .dataSourceConfig(config ->{
//                    config.typeConvert(new MySqlTypeConvert())
//                            .build()
//                })
                .strategyConfig(config -> {
                    config.entityBuilder()
                            .enableLombok()
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
                }).execute();
    }
}