package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;

public class LocationAdapter implements Location<Vector2> {
    private final TransformComponent transformComponent;

    public LocationAdapter(TransformComponent transformComponent) {
        this.transformComponent = transformComponent;
    }

    @Override
    public Vector2 getPosition() {
        return transformComponent;
    }

    @Override
    public float getOrientation() {
        return transformComponent.orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        transformComponent.orientation=orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }
}
