package com.sample.testpersistence;

public class ItemModel{
    private String key;
    private long time;
    private String name;

    public long getTime() {
        return time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
