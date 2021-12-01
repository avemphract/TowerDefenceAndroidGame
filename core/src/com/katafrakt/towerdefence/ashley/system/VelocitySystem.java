package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;

public class VelocitySystem extends IteratingSystem {
    public VelocitySystem() {
        super(Family.all(VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocityComponent=VelocityComponent.MAPPER.get(entity);
        TransformComponent transformComponent=TransformComponent.MAPPER.get(entity);

        transformComponent.x+=velocityComponent.x*deltaTime;
        transformComponent.y+=velocityComponent.y*deltaTime;

        transformComponent.orientation+=velocityComponent.angular*deltaTime;

    }
}
