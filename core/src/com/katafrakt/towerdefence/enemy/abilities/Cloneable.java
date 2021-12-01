package com.katafrakt.towerdefence.enemy.abilities;

import com.badlogic.ashley.core.Entity;

public interface Cloneable<T extends AbilityController> {
    T clone(Entity entity);
}
