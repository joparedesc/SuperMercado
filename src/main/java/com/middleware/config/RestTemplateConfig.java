package com.middleware.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig config.
 * @author jesu_
 */
@Configuration
public class RestTemplateConfig {
    @Bean("clientRest")
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
