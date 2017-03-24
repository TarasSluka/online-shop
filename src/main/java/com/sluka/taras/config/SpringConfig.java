package com.sluka.taras.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringConfig extends WebMvcConfigurerAdapter {
    @Autowired
    Environment env;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photo/product/**")
                .addResourceLocations
                        ("file:///" + env.getProperty("localPathToProductImage"))
                .setCachePeriod(0);
        registry.addResourceHandler("/photo/user/**")
                .addResourceLocations
                        ("file:///" + env.getProperty("localPathToUserImage"))
                .setCachePeriod(0);
    }
}
