package com.joeldavidmason.forgettingmap;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ForgettingMap<K, V> extends LinkedHashMap<K, CacheWrapper<V>> {

    private final int maxCapacity;

    /**
     * Creates ForgettingMap with the specified max capacity
     * @param maxCapacity - the maximum number of items that can be stored in the map
     */
    public ForgettingMap(int maxCapacity) {
        super(0, .75f, false);
        this.maxCapacity = maxCapacity;
    }

    /**
     * Retrieves an item in the map, and then sorts the map based on how many times an item has been retrieved, and the
     * date it was added.
     * @param key - key with which the specified value is to be associated
     * @return
     */
    @Override
    public synchronized CacheWrapper<V> get(Object key) {
        var cacheWrapper = super.get(key);
        if(cacheWrapper == null) {
            return null;
        }
        super.replace((K) key, cacheWrapper, cacheWrapper.setCount(cacheWrapper.getCount() + 1).setTimeLastRetrieved());
        sort();
        return cacheWrapper;
    }

    /**
     * Adds a new item into the map, while also making sure the size is less than or equal to the maxCapacity.
     * If the size is greater than the maxCapacity, then the first item in the map will be removed. This is because on retrieval,
     * the map is sorted based on amount of times the item is retrieved, and the time is was added.
     * @param key - key with which the specified value is to be associated
     * @param value - value to be associated with the specified key
     * @return
     */
    @Override
    public synchronized CacheWrapper<V> put(K key, CacheWrapper<V> value) {
        var cacheWrapper = super.put(key, value);
        if(size() > maxCapacity) {
            this.remove(entrySet().iterator().next().getKey());
        }
        sort();
        return cacheWrapper;
    }

    /**
     * equals will return true if the max capacity and entry set are equal
     * @param o - the object to compare
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var that = (ForgettingMap<?, ?>) o;
        return maxCapacity == that.maxCapacity && this.entrySet().equals(that.entrySet());
    }

    /**
     * sorts the map based on the count and createdTime inside CacheWrapper
     */
    private void sort() {
        var map = entrySet().stream()
                .sorted(Map.Entry.comparingByValue(new CountComparator()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x,y)-> {throw new AssertionError();},
                        LinkedHashMap::new));
        this.clear();
        this.putAll(map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.entrySet(), maxCapacity);
    }

    /**
     * Comparator class to compare the count and timeCreated
     */
    private class CountComparator implements Comparator<CacheWrapper<V>> {

        @Override
        public int compare(CacheWrapper<V> cw1, CacheWrapper<V> cw2) {
            var compareCount = Integer.compare(cw1.getCount(), cw2.getCount());
            if(compareCount == 0) {
                return cw1.getTimeLastRetrieved().compareTo(cw2.getTimeLastRetrieved());
            } else {
                return compareCount;
            }
        }
    }
}
