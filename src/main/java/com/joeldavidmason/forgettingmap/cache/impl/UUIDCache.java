package com.joeldavidmason.forgettingmap.cache.impl;

import com.joeldavidmason.forgettingmap.cache.Cache;
import com.joeldavidmason.forgettingmap.map.ForgettingMap;
import com.joeldavidmason.forgettingmap.cache.wrapper.CacheWrapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This is how the ForgettingMap would be implemented. The reason this should be implemented like this and
 * not used directly is because it hides the public methods available in LinkedHashMap.
 */
public class UUIDCache implements Cache<Integer, UUID> {

    private final ForgettingMap<Integer, UUID> forgettingMap;

    public UUIDCache(int maxCapacity) {
        forgettingMap = new ForgettingMap<>(maxCapacity);
    }

    @Override
    public void add(Integer key, UUID value) {
        forgettingMap.put(key, new CacheWrapper<>(value));
    }

    @Override
    public UUID retrieve(Integer key) {
        var cacheWrapper = forgettingMap.get(key);
        return cacheWrapper != null ? cacheWrapper.getContent() : null;
    }
}
