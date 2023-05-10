package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.katafrakt.towerdefence.ashley.components.ai.AiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.MinionAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.EnemyAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;
import com.katafrakt.towerdefence.ashley.components.entities.NameComponent;

import java.util.Objects;

public class AiSystem extends IteratingSystem {
    private static final String TAG = AiSystem.class.getSimpleName();
    public AiSystem() {
        super(Family.one(
                TowerAiComponent.class,
                EnemyAiComponent.class,
                PlayerAiComponent.class,
                MinionAiComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AiComponent.getComponent(entity).stateMachine.update();
        if (TowerAiComponent.MAPPER.has(entity)){
            //Gdx.app.log(TAG,TowerAiComponent.MAPPER.get(entity).stateMachine.getCurrentState().toString());
        }
    }
}
