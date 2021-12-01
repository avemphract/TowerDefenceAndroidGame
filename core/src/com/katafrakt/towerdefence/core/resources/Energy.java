package com.katafrakt.towerdefence.core.resources;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.MathUtils;

public class Energy extends Signal<Float> {
    public float capacity=10;
    private float currentValue;

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
        dispatch(currentValue);
    }

    public void addCurrentValue(float addition) {
        this.currentValue = MathUtils.clamp(this.currentValue + addition, 0, capacity);
        dispatch(addition);
    }
}