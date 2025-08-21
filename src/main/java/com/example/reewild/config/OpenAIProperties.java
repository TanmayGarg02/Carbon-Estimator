package com.example.reewild.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openai")
@Data
public class OpenAIProperties {
    private boolean enabled;
    private String apiKey;
    private String baseUrl;
    private String chatModel;
    private String visionModel;
}
