package com.hgzp.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.hgzp.advertisingsys.interceptor.CookieInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * 不需要拦截地址
     */
    public static final String[] EXCLUDE_URLS = {"/", "/error", "/swagger*/**", "/webjars/**", "/csrf", "/user", "/main/logOut"};


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        converters.removeIf(c -> c instanceof MappingJackson2SmileHttpMessageConverter);
        converters.removeIf(c -> c instanceof MappingJackson2CborHttpMessageConverter);

        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                // 防止循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                // 空集合返回[],不返回null
                SerializerFeature.WriteNullListAsEmpty,
                // 空字符串返回"",不返回null
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.BrowserCompatible
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);

        converters.add(fastJsonHttpMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getCookieInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_URLS)
                .order(-11);
//        registry.addInterceptor(getHeaderInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_URLS)
//                .order(-10);
//        registry.addInterceptor(getHgLicenseInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_URLS)
//                .order(-10);
//        registry.addInterceptor(getOutApiInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_URLS)
//                .order(-10);
//        registry.addInterceptor(getInnerApiInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_URLS)
//                .order(-10);
//        registry.addInterceptor(getAuthorityAnnotationInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_URLS)
//                .order(-9);
    }


//    @Bean
//    public ApiInterceptor getHeaderInterceptor() {
//        return new ApiInterceptor();
//    }
//    @Bean
//    public AuthorityAnnotationInterceptor getAuthorityAnnotationInterceptor() {
//        return new AuthorityAnnotationInterceptor();
//    }
    @Bean
    public CookieInterceptor getCookieInterceptor() {
        return new CookieInterceptor();
    }
//    @Bean
//    public HgLicenseInterceptor getHgLicenseInterceptor() {
//        return new HgLicenseInterceptor();
//    }
//    @Bean
//    public OutApiInterceptor getOutApiInterceptor() {
//        return new OutApiInterceptor();
//    }
//    @Bean
//    public InnerApiInterceptor getInnerApiInterceptor() {
//        return new InnerApiInterceptor();
//    }
}
