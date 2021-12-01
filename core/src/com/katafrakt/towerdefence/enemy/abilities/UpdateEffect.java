package com.katafrakt.towerdefence.enemy.abilities;

import com.badlogic.ashley.core.Entity;

public interface UpdateEffect extends Effect {
    void update(Entity entity, float delta);

    void leave(Entity entity);
}
