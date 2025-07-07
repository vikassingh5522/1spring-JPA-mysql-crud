package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataSourceConfig.class, CacheConfig.class, WebConfig.class})
public class AppConfig {
}