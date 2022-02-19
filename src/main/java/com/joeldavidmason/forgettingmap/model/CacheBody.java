package com.joeldavidmason.forgettingmap.model;

public abstract class CacheBody {
    private int id;

    protected CacheBody(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
