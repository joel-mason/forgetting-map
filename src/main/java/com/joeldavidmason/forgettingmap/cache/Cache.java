package com.joeldavidmason.forgettingmap.cache;

public interface Cache<K, V> {

    public void add(K key, V value);

    public V retrieve(K key);

}
