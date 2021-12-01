package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.katafrakt.towerdefence.ashley.components.EffectComponent;
import com.katafrakt.towerdefence.enemy.abilities.Effect;
import com.katafrakt.towerdefence.enemy.abilities.UpdateEffect;

public class EffectSystem extends IteratingSystem {
    public EffectSystem() {
        super(Family.all(EffectComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        for (Effect effect : EffectComponent.MAPPER.get(entity).effects) {
            if (effect instanceof UpdateEffect) {
                ((UpdateEffect) effect).update(entity, deltaTime);
            }
        }
    }
}
