package com.joeldavidmason.forgettingmap.service.impl;

import com.joeldavidmason.forgettingmap.cache.impl.UUIDCache;
import com.joeldavidmason.forgettingmap.exception.NotFoundException;
import com.joeldavidmason.forgettingmap.model.UUIDCacheBody;
import com.joeldavidmason.forgettingmap.service.CacheService;
import org.springframework.stereotype.Service;

@Service
public class UUIDCacheService implements CacheService<UUIDCacheBody> {

    private final UUIDCache uuidCache;

    public UUIDCacheService(UUIDCache uuidCache) {
        this.uuidCache = uuidCache;
    }

    @Override
    public UUIDCacheBody retrieve(int id) {
        var data = uuidCache.retrieve(id);
        if(data == null) {
            throw new NotFoundException("Cannot find data with id: " + id);
        }
        return new UUIDCacheBody(id, data);
    }

    @Override
    public void add(UUIDCacheBody data) {
        uuidCache.add(data.getId(), data.getData());
    }
}
