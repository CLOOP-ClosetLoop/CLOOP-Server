package com.cloop.cloop.auth.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");        // 모든 출처 허용
        config.addAllowedHeader("*");               // 모든 헤더 허용
        config.addAllowedMethod("*");               // 모든 http 메서드 허용
        source.registerCorsConfiguration("/**", config);        // 모든 경로에 적용
        return new CorsFilter(source);

    }
}
