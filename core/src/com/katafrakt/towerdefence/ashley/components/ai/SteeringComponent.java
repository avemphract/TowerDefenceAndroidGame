package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ai.steer.FaceThrust;
import com.katafrakt.towerdefence.ai.steer.NodeFollowPath;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.utility.DebugRender;
import com.katafrakt.towerdefence.utility.ValueFunction;

public class SteeringComponent implements Steerable<Vector2>, Component, Pool.Poolable, DebugRender {
    public static ComponentMapper<SteeringComponent> MAPPER = ComponentMapper.getFor(SteeringComponent.class);
    TransformComponent transformComponent;
    VelocityComponent velocityComponent;
    //inputs
    float boundRadius;
    boolean tag;
    float zeroLinearSpeedThreshold;

    public ValueFunction maxLinearSpeed = new ValueFunction();
    public ValueFunction maxLinearAcceleration = new ValueFunction();

    float maxAngularSpeed;
    float maxAngularAcceleration;

    //behaviour
    public SteeringBehavior<Vector2> behavior;
    //output
    public SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2(), 0);

    public SteeringComponent init(Builder builder) {
        transformComponent = builder.transformComponent;
        velocityComponent = builder.velocityComponent;
        boundRadius = builder.boundRadius;
        tag = builder.tag;
        zeroLinearSpeedThreshold = builder.zeroLinearSpeedThreshold;

        maxLinearSpeed.setBaseStat(builder.maxLinearSpeed);
        maxLinearAcceleration.setBaseStat(builder.maxLinearAcceleration);

        maxAngularSpeed = builder.maxAngularSpeed;
        maxAngularAcceleration = builder.maxAngularAcceleration;
        return this;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return velocityComponent;
    }

    @Override
    public float getAngularVelocity() {
        return velocityComponent.angular;
    }

    @Override
    public float getBoundingRadius() {
        return boundRadius;
    }

    @Override
    public boolean isTagged() {
        return tag;
    }

    @Override
    public void setTagged(boolean tagged) {
        tag = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed.getValue();
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        throw new RuntimeException();
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration.getValue();
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        throw new RuntimeException();
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
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
        transformComponent.orientation = orientation;
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

    //Kullanılmıyor
    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    @Override
    public void reset() {
        boundRadius = 0;
        zeroLinearSpeedThreshold = 0;
        maxLinearSpeed.reset();
        maxLinearAcceleration.reset();
        maxAngularSpeed = 0;
        maxAngularAcceleration = 0;

        //behaviour
        behavior = null;
        //output
        steeringOutput.setZero();
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        //TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        if (behavior instanceof NodeFollowPath) {
            Vector2 vector2 = ((NodeFollowPath) behavior).getInternalTargetPosition();
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.circle(vector2.x, vector2.y, 0.6f);
            shapeRenderer.circle(vector2.x, vector2.y, 0.3f);

            shapeRenderer.setColor(Color.BROWN);
            for (LinePath.Segment<Vector2> segment : ((LinePath<Vector2>) ((NodeFollowPath) behavior).getPath()).getSegments()) {
                shapeRenderer.line(segment.getBegin().x, segment.getBegin().y, segment.getEnd().x, segment.getEnd().y);
            }
        }
        if (behavior instanceof FaceThrust) {

        }
    }

    public static class Builder {
        TransformComponent transformComponent;
        VelocityComponent velocityComponent;
        float boundRadius;
        boolean tag;
        float zeroLinearSpeedThreshold;
        float maxLinearSpeed;
        float maxLinearAcceleration;
        float maxAngularSpeed;
        float maxAngularAcceleration;

        public Builder transformComponent(TransformComponent transformComponent) {
            this.transformComponent = transformComponent;
            return this;
        }

        public Builder velocityComponent(VelocityComponent velocityComponent) {
            this.velocityComponent = velocityComponent;
            return this;
        }

        public Builder boundRadius(float boundRadius) {
            this.boundRadius = boundRadius;
            return this;
        }

        public Builder tag(boolean tag) {
            this.tag = tag;
            return this;
        }

        public Builder zeroLinearSpeedThreshold(float zeroLinearSpeedThreshold) {
            this.zeroLinearSpeedThreshold = zeroLinearSpeedThreshold;
            return this;
        }

        public Builder maxLinearSpeed(float maxLinearSpeed) {
            this.maxLinearSpeed = maxLinearSpeed;
            return this;
        }

        public Builder maxLinearAcceleration(float maxLinearAcceleration) {
            this.maxLinearAcceleration = maxLinearAcceleration;
            return this;
        }

        public Builder maxAngularSpeed(float maxAngularSpeed) {
            this.maxAngularSpeed = maxAngularSpeed;
            return this;
        }

        public Builder maxAngularAcceleration(float maxAngularAcceleration) {
            this.maxAngularAcceleration = maxAngularAcceleration;
            return this;
        }

    }
}
