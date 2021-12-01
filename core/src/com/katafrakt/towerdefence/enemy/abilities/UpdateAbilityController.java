package com.katafrakt.towerdefence.enemy.abilities;

import com.badlogic.ashley.core.Entity;

public abstract class UpdateAbilityController extends AbilityController implements UpdateEffect {

    public UpdateAbilityController(Effect effect, Entity owner) {
        super(effect, owner);
    }

    public UpdateAbilityController(Effect effect) {
        super(effect);
    }

    public abstract void update(Entity entity, float delta);
    public abstract void leave(Entity entity);
}
