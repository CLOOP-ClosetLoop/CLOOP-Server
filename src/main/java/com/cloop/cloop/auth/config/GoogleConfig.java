package com.cloop.cloop.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "google")         // google로 시작하는 설정값을 매핑
public class GoogleConfig {
    private List<String> clientIds;
}
