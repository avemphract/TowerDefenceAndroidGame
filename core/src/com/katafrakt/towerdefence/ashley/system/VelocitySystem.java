package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;

public class VelocitySystem extends IteratingSystem {
    public VelocitySystem() {
        super(Family.all(VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);

        if (velocityComponent.len2() < 0.1f) {
            SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity);
            if (steeringComponent!=null && steeringComponent.getLinearVelocity().len2() < 0.1f) {
                velocityComponent.x = 0;
                velocityComponent.y = 0;
            }
        } else {

            transformComponent.x += velocityComponent.x * deltaTime;
            transformComponent.y += velocityComponent.y * deltaTime;

            transformComponent.orientation = velocityComponent.angleRad();
        }
    }
}
