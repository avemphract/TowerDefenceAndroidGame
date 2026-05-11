package com.katafrakt.towerdefence.ai.steer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class FaceThrust extends Face<Vector2> {
    private static final String TAG = FaceThrust.class.getSimpleName();
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
        //Gdx.app.log(TAG, this.target.getPosition().toString());
        steering.angular = MathUtils.clamp(steering.angular, -owner.getMaxAngularAcceleration(), owner.getMaxAngularAcceleration());
        steering.linear.set(Vector2.Y.cpy().rotateRad(owner.getOrientation()).setLength(owner.getMaxLinearAcceleration()));
        return steering;
    }

    public void isFaceActive(boolean faceActive) {
        this.faceActive = faceActive;
    }
}
