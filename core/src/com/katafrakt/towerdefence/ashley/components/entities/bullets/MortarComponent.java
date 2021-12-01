package com.katafrakt.towerdefence.ashley.components.entities.bullets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class MortarComponent implements Component, Pool.Poolable {
    public static final ComponentMapper<MortarComponent> MAPPER = ComponentMapper.getFor(MortarComponent.class);
    public float attackAmount;
    public float mortarTime;
    public float mortarRemainTime;

    public Component init(float attackAmount, float mortarTime) {
        this.attackAmount=attackAmount;
        this.mortarTime=mortarTime;
        mortarRemainTime=mortarTime;
        return this;
    }

    @Override
    public void reset() {
        attackAmount=0;
        mortarTime=0;
        mortarRemainTime=0;
    }
}
