package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.katafrakt.towerdefence.ashley.components.ai.AiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.EnemyAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;

import java.util.Objects;

public class AiSystem extends IteratingSystem {
    public AiSystem() {
        super(Family.one(
                TowerAiComponent.class,
                EnemyAiComponent.class,
                PlayerAiComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Objects.requireNonNull(AiComponent.getComponent(entity)).stateMachine.update();
    }
}
