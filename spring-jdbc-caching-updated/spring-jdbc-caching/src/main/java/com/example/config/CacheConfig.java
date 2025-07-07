package com.example.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Configure cache properties
        MutableConfiguration<Object, Object> cacheConfig = new MutableConfiguration<>()
                .setTypes(Object.class, Object.class)
                .setStoreByValue(false)
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 1)));

        // Get JCache CacheManager
        javax.cache.CacheManager jCacheManager = Caching.getCachingProvider().getCacheManager();

        // Create cache only if it doesn't exist
        if (jCacheManager.getCache("usersCache") == null) {
            jCacheManager.createCache("usersCache", cacheConfig);
        }

        return new JCacheCacheManager(jCacheManager);
    }
}