package com.joeldavidmason.forgettingmap.configuration;

import com.joeldavidmason.forgettingmap.cache.impl.UUIDCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cache.string-cache")
public class UUIDCacheConfiguration {

    private int maxCapacity;

    @Bean
    public UUIDCache uuidCache() {
        return new UUIDCache(maxCapacity);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
