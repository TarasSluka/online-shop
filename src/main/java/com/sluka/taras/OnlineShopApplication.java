package com.sluka.taras;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ComponentScan("com.sluka.taras")
@EnableJpaAuditing
@EnableTransactionManagement
@SpringBootApplication
public class OnlineShopApplication extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(OnlineShopApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OnlineShopApplication.class);
    }
}
