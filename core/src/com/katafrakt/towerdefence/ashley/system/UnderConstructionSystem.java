package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.katafrakt.towerdefence.ashley.components.buildings.UnderConstructionComponent;

public class UnderConstructionSystem extends IteratingSystem {
    public UnderConstructionSystem() {
        super(Family.all(UnderConstructionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        UnderConstructionComponent underConstructionComponent=UnderConstructionComponent.MAPPER.get(entity);
        if (underConstructionComponent.remainMaterial<=0){
            entity.remove(UnderConstructionComponent.class);
        }
    }
}
