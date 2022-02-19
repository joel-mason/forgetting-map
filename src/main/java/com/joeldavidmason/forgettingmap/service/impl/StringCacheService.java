package com.joeldavidmason.forgettingmap.service.impl;

import com.joeldavidmason.forgettingmap.cache.impl.StringCache;
import com.joeldavidmason.forgettingmap.cache.impl.UUIDCache;
import com.joeldavidmason.forgettingmap.exception.NotFoundException;
import com.joeldavidmason.forgettingmap.model.StringCacheBody;
import com.joeldavidmason.forgettingmap.model.UUIDCacheBody;
import com.joeldavidmason.forgettingmap.service.CacheService;
import org.springframework.stereotype.Service;

@Service
public class StringCacheService implements CacheService<StringCacheBody> {

    private final StringCache stringCache;

    public StringCacheService(StringCache stringCache) {
        this.stringCache = stringCache;
    }

    @Override
    public StringCacheBody retrieve(int id) {
        var data = stringCache.retrieve(id);
        if(data == null) {
            throw new NotFoundException("Cannot find data with id: " + id);
        }
        return new StringCacheBody(id, data);
    }

    @Override
    public void add(StringCacheBody data) {
        stringCache.add(data.getId(), data.getData());
    }
}
