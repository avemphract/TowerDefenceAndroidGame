package com.katafrakt.towerdefence.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public interface AfterEngine {
    void afterEngine(PooledEngine engine, Entity own);
}
