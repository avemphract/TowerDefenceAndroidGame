package com.katafrakt.towerdefence.ai.steer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

public class TryToStop extends SteeringBehavior<Vector2> {
    private static final String TAG = TryToStop.class.getSimpleName();
    public TryToStop(Steerable<Vector2> owner) {
        super(owner);
    }

    @Override
    protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
        if (owner.getLinearVelocity().epsilonEquals(Vector2.Zero)){
            steering.linear.setZero();
        }
        else if (owner.getLinearVelocity().len() > owner.getMaxAngularAcceleration() * Gdx.graphics.getDeltaTime()) {
            steering.linear.setZero().mulAdd(owner.getLinearVelocity(),-1).setLength(owner.getMaxLinearAcceleration());
        }
        else {
            steering.linear.setZero().mulAdd(owner.getLinearVelocity(),-1);
        }
        return steering;
    }
}
