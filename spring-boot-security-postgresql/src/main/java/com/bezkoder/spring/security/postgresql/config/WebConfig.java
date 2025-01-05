package com.bezkoder.spring.security.postgresql.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configura o caminho da URL para acessar os arquivos na pasta 'uploads'
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/path/to/uploads/"); // Caminho absoluto da pasta uploads
    }

}