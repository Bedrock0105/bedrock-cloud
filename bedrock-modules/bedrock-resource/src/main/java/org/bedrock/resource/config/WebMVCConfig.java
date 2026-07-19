package org.bedrock.resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Component
public class WebMVCConfig implements WebMvcConfigurer {

    @Value("${file.location.path-pattern:/files/**}")
    private String pathPattern;

    @Value("${file.location.static-path:D:}")
    private String staticPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(pathPattern)
                .addResourceLocations("file:" + staticPath + File.separator);
    }
}
