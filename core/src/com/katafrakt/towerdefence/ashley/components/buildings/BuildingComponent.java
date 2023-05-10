package com.katafrakt.towerdefence.ashley.components.buildings;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.BeforeEngine;

public abstract class BuildingComponent implements Component, Pool.Poolable, BeforeEngine {
    public static BuildingComponent getComponent(Entity entity) {
        BasementComponent basementComponent = BasementComponent.MAPPER.get(entity);
        if (basementComponent != null)
            return basementComponent;
        CollectorComponent collectorComponent = CollectorComponent.MAPPER.get(entity);
        if (collectorComponent != null)
            return collectorComponent;
        MinerComponent minerComponent = MinerComponent.MAPPER.get(entity);
        if (minerComponent != null)
            return minerComponent;
        TowerComponent towerComponent = TowerComponent.MAPPER.get(entity);
        if (towerComponent != null)
            return towerComponent;
        throw new RuntimeException("Not found BuildingComponent");
    }

    public boolean walkable = false;

    @Override
    public void beforeEngine(PooledEngine engine, Entity own) {
        GameManager.getInstance().getMap().findNode(TransformComponent.MAPPER.get(own)).setBuilding(own);
    }
}
