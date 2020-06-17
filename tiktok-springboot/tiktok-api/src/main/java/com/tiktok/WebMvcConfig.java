package com.tiktok;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    // command+N override method

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")      // "/**"访问所有资源
                .addResourceLocations("classpath:/META-INF/resources")
                .addResourceLocations("file:/Users/suen/project/tiktok-dev/");
    }
}
