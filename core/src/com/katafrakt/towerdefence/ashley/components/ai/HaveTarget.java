package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.Entity;

public interface HaveTarget {
    Entity getTarget();
    void setTarget(Entity entity);
}
