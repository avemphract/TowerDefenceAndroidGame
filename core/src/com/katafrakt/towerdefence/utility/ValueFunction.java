package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;

public class ValueFunction implements Pool.Poolable {
    private static final String TAG = ValueFunction.class.getSimpleName();
    private float baseStat;
    private final FloatArray addition=new FloatArray();
    private float multiplier = 1;

    private float value;

    public float getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(float baseStat) {
        this.baseStat = baseStat;
        value = (baseStat + (addition.isEmpty() ? 0 : addition.get(0))) * multiplier;
    }

    public void addAddition(float f) {
        addition.add(f);
        addition.sort();
        addition.reverse();
        value = (baseStat + (addition.isEmpty() ? 0 : addition.get(0))) * multiplier;
    }

    public void removeAddition(float f) {
        addition.removeValue(f);
        addition.sort();
        addition.reverse();
        //Gdx.app.log(TAG,"Float added. "+addition.toString());
        value = (baseStat + (addition.isEmpty() ? 0 : addition.get(0))) * multiplier;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
        addition.sort();
        addition.reverse();
        value = (baseStat + (addition.isEmpty() ? 0 : addition.get(0))) * multiplier;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void reset() {
        baseStat=0;
        addition.clear();
        multiplier=1;
    }
}
