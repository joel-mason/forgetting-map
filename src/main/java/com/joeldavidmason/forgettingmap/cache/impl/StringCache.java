package com.joeldavidmason.forgettingmap.cache.impl;

import com.joeldavidmason.forgettingmap.cache.Cache;
import com.joeldavidmason.forgettingmap.cache.wrapper.CacheWrapper;
import com.joeldavidmason.forgettingmap.map.ForgettingMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringCache implements Cache<Integer, String> {

    private final ForgettingMap<Integer, String> forgettingMap;

    public StringCache(int maxCapacity) {
        forgettingMap = new ForgettingMap<>(maxCapacity);
    }

    @Override
    public void add(Integer key, String value) {
        log.info("Adding key {} and value {}", key, value);
        forgettingMap.put(key, new CacheWrapper<>(value));
    }

    @Override
    public String retrieve(Integer key) {
        var cacheWrapper = forgettingMap.get(key);
        return cacheWrapper != null ? cacheWrapper.getContent() : null;
    }
}
