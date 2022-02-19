package com.joeldavidmason.forgettingmap.model;

public class StringCacheBody extends CacheBody {

    private String data;

    public StringCacheBody(int id, String data) {
        super(id);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
