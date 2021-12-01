package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.EnemyAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.enemy.abilities.AbilityController;
import com.katafrakt.towerdefence.enemy.abilities.UpdateAbilityController;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.screens.GameManager;

public class EnemySystem extends IteratingSystem {
    private static final String TAG = EnemySystem.class.getSimpleName();
    private final Map map;
    private PooledEngine engine;

    public EnemySystem() {
        super(Family.all(EnemyComponent.class).get());
        map = GameManager.getInstance().getMap();
        engine = GameManager.getInstance().getEngine();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyComponent = EnemyComponent.MAPPER.get(entity);
        EnemyAiComponent enemyAiComponent = EnemyAiComponent.MAPPER.get(entity);
        HealthComponent healthComponent = HealthComponent.MAPPER.get(entity);

        enemyComponent.progress = map.endRemain.get(map.startNode) - map.endRemain.get(enemyAiComponent.getCurrentNode());
        if (healthComponent.getCurrentHealth() <= 0) {
            healthComponent.setAlive(false);
            SteeringComponent.MAPPER.get(entity).behavior = null;
            VelocityComponent.MAPPER.get(entity).init(0, 0);
            enemyComponent.deathTime += deltaTime;
            if (enemyComponent.deathTime > 1) {
                engine.removeEntity(entity);
            }
        }

    }
}
