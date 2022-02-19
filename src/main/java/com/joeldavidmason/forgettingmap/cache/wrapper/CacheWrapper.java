package com.joeldavidmason.forgettingmap.cache.wrapper;

import java.time.Instant;

/**
 * Wrapper for the content that is to be stored inside a ForgettingMap
 * @param <T> - Type of Object to be stored.
 */
public class CacheWrapper<T> {
    private int count;
    private Instant timeLastRetrieved;
    private final T content;

    /**
     * CacheWrapper constructor
     * @param content - the content that needs to be stored
     */
    public CacheWrapper(T content) {
        this.count = 0;
        this.timeLastRetrieved = Instant.now();
        this.content = content;
    }

    /**
     * gets the number of times this object has been read
     * @return - count
     */
    public int getCount() {
        return count;
    }

    /**
     * Update the number of times this object has been read
     * @param count - number of times read
     * @return this
     */
    public CacheWrapper<T> setCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * Get the Instant this object was created
     * @return Instant
     */
    public Instant getTimeLastRetrieved() {
        return timeLastRetrieved;
    }

    /**
     * Set the time this object was last retrieved
     * @return Instant
     */
    public CacheWrapper<T> setTimeLastRetrieved() {
        this.timeLastRetrieved = Instant.now();
        return this;
    }

    /**
     * Get the content inside this Wrapper
     * @return content
     */
    public T getContent() {
        return content;
    }
}
