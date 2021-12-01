package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.utils.Pool;

public class Pair<K, V> implements Pool.Poolable {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Pair() {
    }

    public Pair<K,V> init(K key, V value) {
        this.key = key;
        this.value = value;
        return this;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }


    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String toString() {
        return key + "=" + value;
    }

    @Override
    public void reset() {
        key=null;
        value=null;
    }
}