package com.katafrakt.towerdefence.ashley.components.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.enemy.abilities.AbilityController;
import com.katafrakt.towerdefence.utility.BeforeEngine;

public class EnemyComponent implements Component, Pool.Poolable, BeforeEngine {
    public static final ComponentMapper<EnemyComponent> MAPPER = ComponentMapper.getFor(EnemyComponent.class);
    public static final Family FAMILY = Family.one(EnemyComponent.class).get();
    public float progress;
    public float deathTime;

    private Entity entity;

    public EnemyComponent init(Entity entity) {
        this.entity = entity;
        return this;
    }


    @Override
    public void reset() {
        this.entity=null;
    }


    @Override
    public void beforeEngine(PooledEngine engine, Entity own) {
        progress = 0;
        deathTime = 0;
    }
}
