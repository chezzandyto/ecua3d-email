package com.ecua3d.email.config;

import com.ecua3d.email.service.KeycloakService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(KeycloakService keycloakService) {
        return requestTemplate -> {
            String accessToken = keycloakService.getAccessToken();
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        };
    }
}
