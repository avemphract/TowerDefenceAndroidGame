package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.utils.ObjectFloatMap;
import com.katafrakt.towerdefence.ai.EnemyState;

public class ArrayFloatMap<T extends Enum<T>> extends ObjectFloatMap<T> {
    public ArrayFloatMap(T[] array) {
        for(T c :array){
            put(c,0);
        }
    }
}
