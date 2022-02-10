package com.joeldavidmason.forgettingmap;

public interface Cache<K, V> {

    public void add(K key, V value);

    public V retrieve(K key);

}
