package com.katafrakt.towerdefence.core.resources;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.MathUtils;

public class Resource extends Signal<Float> {
    private float currentValue;

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public void addCurrentValue(float addition) {
        this.currentValue += addition;
        dispatch(addition);
    }
}
