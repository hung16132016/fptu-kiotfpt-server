package com.kiotfpt.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        config.setExposedHeaders(Arrays.asList("x-auth-token"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
