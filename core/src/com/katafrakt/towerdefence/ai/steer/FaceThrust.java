package com.katafrakt.towerdefence.ai.steer;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class FaceThrust extends Face<Vector2> {
    private boolean faceActive = true;

    public FaceThrust(Steerable<Vector2> owner) {
        super(owner);
    }

    public FaceThrust(Steerable<Vector2> owner, Location<Vector2> target) {
        super(owner, target);
    }

    @Override
    protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
        if (faceActive)
            super.calculateRealSteering(steering);
        steering.linear.set(Vector2.X.cpy().rotateRad(owner.getOrientation() - MathUtils.HALF_PI).setLength(owner.getMaxLinearAcceleration()));
        return steering;
    }

    public void isFaceActive(boolean faceActive) {
        this.faceActive = faceActive;
    }
}
