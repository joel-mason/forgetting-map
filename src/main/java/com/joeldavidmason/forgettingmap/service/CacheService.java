package com.joeldavidmason.forgettingmap.service;

import com.joeldavidmason.forgettingmap.model.CacheBody;

import java.net.URI;

public interface CacheService<T extends CacheBody> {

    public T retrieve(int id);

    public void add(T data);
}
