package com.joeldavidmason.forgettingmap.model;

import java.util.UUID;

public class UUIDCacheBody extends CacheBody {

    private UUID data;

    public UUIDCacheBody(int id, UUID data) {
        super(id);
        this.data = data;
    }

    public UUID getData() {
        return data;
    }

    public void setData(UUID data) {
        this.data = data;
    }
}
