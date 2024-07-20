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
        
        // Allow specific origins
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://4.230.17.119:3000")); 
        // Specify allowed methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Specify allowed headers
        config.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        // Specify exposed headers
        config.setExposedHeaders(Arrays.asList("x-auth-token"));
        // Allow credentials if needed
        config.setAllowCredentials(true); 
        
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
