package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class LocationVector implements Location<Vector2> {
    private Vector2 position;
    private float orientation;

    public LocationVector(Vector2 position, float orientation) {
        this.position = new Vector2(position);
        this.orientation = orientation;
    }

    public LocationVector() {
        this.position = new Vector2();
        this.orientation = 0;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new LocationVector(position, orientation);
    }

    @Override
    public String toString() {
        return "LocationVector{" +
                "position=" + position +
                ", orientation=" + orientation +
                '}';
    }
}
