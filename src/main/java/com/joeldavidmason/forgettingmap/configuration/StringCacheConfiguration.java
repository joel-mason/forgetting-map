package com.joeldavidmason.forgettingmap.configuration;

import com.joeldavidmason.forgettingmap.cache.impl.StringCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cache.uuid-cache")
public class StringCacheConfiguration {

    private int maxCapacity;

    @Bean
    public StringCache stringCache() {
        return new StringCache(maxCapacity);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
