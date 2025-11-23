package com.iparvez.fileapi.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 1. Marks this class as a source of bean definitions
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        
        // Apply CORS configuration to ALL API endpoints (/**)
        registry.addMapping("/**")
                
                // 2. Specifies the allowed origin (Your frontend URL)
                // This is the CRITICAL fix for 'Access-Control-Allow-Origin'
                .allowedOrigins("http://localhost:5173") 
                
                // 3. Specifies which HTTP methods are allowed
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                
                // 4. Allows credentials (like cookies or Authorization headers) to be sent
                .allowCredentials(true)
                
                // 5. Specifies headers that can be used in the actual request
                .allowedHeaders("*"); 
    }
}