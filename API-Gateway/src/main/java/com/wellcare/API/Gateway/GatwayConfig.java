package com.wellcare.API.Gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatwayConfig {
    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }
}
