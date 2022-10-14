package com.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    private String frontend = "http://d2bax8wjt0uva1.cloudfront.net/";
    private String backend = "http://ec2-3-232-180-106.compute-1.amazonaws.com/api/";
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedMethods("GET","POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowedOrigins(frontend,backend);
            }
        };
    }
}