package com.katafrakt.towerdefence.core.actions;

import com.badlogic.ashley.core.Entity;

public abstract class PlayerAction {
    protected final Entity entity;

    public PlayerAction(Entity entity) {
        this.entity = entity;
    }

    public abstract void act();
}
