package com.katafrakt.towerdefence.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public interface BeforeEngine {
    void beforeEngine(PooledEngine engine, Entity own);
}
