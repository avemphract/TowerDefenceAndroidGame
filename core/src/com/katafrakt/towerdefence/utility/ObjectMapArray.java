package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ObjectMapArray<K,A extends Array<?>> extends ObjectMap<K, A> {

    public void clearArrays(){
        for (Array<?> array:values()) {
            array.clear();
        }
    }
}
