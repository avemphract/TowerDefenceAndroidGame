package com.katafrakt.towerdefence.ashley.components.entities.bullets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.utility.DebugRender;

public class AreaExpanderComponent implements Component, Pool.Poolable, DebugRender {
    public static final ComponentMapper<AreaExpanderComponent> MAPPER = ComponentMapper.getFor(AreaExpanderComponent.class);
    public ObjectSet<Entity> alreadyDamaged = new ObjectSet<>();
    public Listener<Entity> targetAliveListener = (entitySignal, entity) -> alreadyDamaged.remove(entity);
    public float remainTime;
    public float radius;
    public float radiusSpeed;
    public float damage;

    public AreaExpanderComponent init(float damage, float remainTime, float sizeSpeed) {
        this.damage = damage;
        this.remainTime = remainTime;
        this.radiusSpeed = sizeSpeed;
        return this;
    }

    @Override
    public void reset() {
        radius = 0;
        alreadyDamaged.clear();
        for (Entity entity : alreadyDamaged) {
            HealthComponent.MAPPER.get(entity).aliveSignal.remove(targetAliveListener);
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        TransformComponent transformComponent=TransformComponent.MAPPER.get(entity);
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.circle(transformComponent.x,transformComponent.y,radius);
    }
}
