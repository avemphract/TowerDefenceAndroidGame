package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.RocketComponent;

public class SteeringSystem extends IteratingSystem {
    private static final String TAG = SteeringSystem.class.getSimpleName();

    public SteeringSystem() {
        super(Family.all(SteeringComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity);
        if (steeringComponent.behavior == null)
            return;
        steeringComponent.behavior.calculateSteering(steeringComponent.steeringOutput);


        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        if ((velocityComponent.len()*1.05f) > steeringComponent.getMaxLinearSpeed()) {
            velocityComponent.mulAdd(steeringComponent.steeringOutput.linear, deltaTime).limit(MathUtils.lerp(velocityComponent.len(), steeringComponent.getMaxLinearSpeed(), 0.15f));

        } else {
            velocityComponent.mulAdd(steeringComponent.steeringOutput.linear, deltaTime).limit(steeringComponent.getMaxLinearSpeed());
        }
        velocityComponent.angular = -steeringComponent.steeringOutput.angular * deltaTime;

    }
}
