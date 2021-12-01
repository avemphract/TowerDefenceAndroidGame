package com.katafrakt.towerdefence.ashley.system.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.AreaExpanderComponent;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.Overlapping;

public class AreaExpanderSystem extends IteratingSystem {
    private static final String TAG = AreaExpanderSystem.class.getSimpleName();
    private PooledEngine engine;

    public AreaExpanderSystem() {
        super(Family.all(AreaExpanderComponent.class).get());
        this.engine = GameManager.getInstance().getEngine();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AreaExpanderComponent areaExpanderComponent = AreaExpanderComponent.MAPPER.get(entity);
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);

        areaExpanderComponent.radius += areaExpanderComponent.radiusSpeed * deltaTime;
        areaExpanderComponent.remainTime -= deltaTime;

        for (Entity enemy : engine.getEntitiesFor(EnemyComponent.FAMILY)) {
            EnemyComponent enemyComponent = EnemyComponent.MAPPER.get(enemy);
            HealthComponent healthComponent = HealthComponent.MAPPER.get(enemy);
            TransformComponent enemyTransform = TransformComponent.MAPPER.get(enemy);
            if (healthComponent.isAlive() && !areaExpanderComponent.alreadyDamaged.contains(enemy) && Overlapping.overlapCirclePoint(transformComponent, areaExpanderComponent.radius, enemyTransform)) {
                healthComponent.addCurrentHealth(-areaExpanderComponent.damage);
                areaExpanderComponent.alreadyDamaged.add(enemy);
                healthComponent.aliveSignal.add(areaExpanderComponent.targetAliveListener);
            }
        }

        if (areaExpanderComponent.remainTime < 0)
            engine.removeEntity(entity);
    }
}
