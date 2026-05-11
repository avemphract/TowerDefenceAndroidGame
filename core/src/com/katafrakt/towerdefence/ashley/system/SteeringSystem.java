package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.katafrakt.towerdefence.ai.steer.FaceThrust;
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
        if(steeringComponent.behavior instanceof FaceThrust){
            Gdx.app.log(TAG, String.valueOf(steeringComponent.steeringOutput.angular));
        }
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        velocityComponent.mulAdd(steeringComponent.steeringOutput.linear, deltaTime).limit(steeringComponent.getMaxLinearSpeed());

        velocityComponent.angular += steeringComponent.steeringOutput.angular * deltaTime;
        velocityComponent.angular = MathUtils.clamp(velocityComponent.angular, -steeringComponent.getMaxAngularSpeed(), steeringComponent.getMaxAngularSpeed());

    }
}
