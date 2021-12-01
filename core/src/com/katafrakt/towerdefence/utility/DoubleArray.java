package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


public class DoubleArray<V1,V2> extends Array<Pair<V1,V2>> {
    Pool<Pair<V1,V2>> pairPool=new Pool<Pair<V1, V2>>() {
        @Override
        protected Pair<V1, V2> newObject() {
            return new Pair<>();
        }
    };

    public void addPair(V1 v1, V2 v2){
        add(pairPool.obtain().init(v1,v2));
    }

    @Override
    public void clear() {
        for (Pair<V1,V2> pair:this) {
            pairPool.free(pair);
        }
        super.clear();
    }
}
