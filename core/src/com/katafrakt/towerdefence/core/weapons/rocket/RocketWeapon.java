package com.katafrakt.towerdefence.core.weapons.rocket;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ai.steer.FaceThrust;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.NameComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.RocketComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;
import com.katafrakt.towerdefence.utility.LocationAdapter;

public class RocketWeapon extends Weapon {
    private static final String TAG = RocketWeapon.class.getSimpleName();
    protected float maxLinearSpeed;
    protected float maxLinearAcceleration;
    protected float maxAngularSpeed;
    protected float maxAngularAcceleration;

    protected float width;
    protected float height;

    protected boolean autoAiming;

    public RocketWeapon(Builder<?, ?> builder) {
        super(builder);
        this.maxLinearSpeed = builder.maxLinearSpeed;
        this.maxLinearAcceleration = builder.maxLinearAcceleration;
        this.maxAngularSpeed = builder.maxAngularSpeed;
        this.maxAngularAcceleration = builder.maxAngularAcceleration;
        this.width = builder.width;
        this.height = builder.height;
        this.autoAiming = builder.autoAiming;
        lastAttackTime = -1000;
    }

    @Override
    public void attack(Entity target) {
        spawn(this, target,Vector2.X.cpy().setToRandomDirection());
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public static Entity spawn(RocketWeapon rocketWeapon, Entity target, Vector2 rocketDirection) {
        PooledEngine engine = GameManager.getInstance().getEngine();
        TransformComponent pos = rocketWeapon.getOwnerTransform();
        TransformComponent targetTransform = TransformComponent.MAPPER.get(target);
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(BoundComponent.class).init(rocketWeapon.getHeight(), rocketWeapon.getWidth()));

        entity.add(engine.createComponent(NameComponent.class).init("Rocket"));

        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class).init(rocketDirection.setLength(rocketWeapon.getMaxLinearSpeed()));
        entity.add(velocityComponent);

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class).init(pos.x, pos.y, velocityComponent.angleRad() + MathUtils.HALF_PI);
        entity.add(transformComponent);

        RocketComponent rocketComponent = engine.createComponent(RocketComponent.class).init(entity, 50, rocketWeapon.getAttackAmount());
        if (rocketWeapon.isAutoAiming())
            rocketComponent.aliveListener=((signal, object)->RocketComponent.scanNewTarget(entity));
        else
            rocketComponent.aliveListener=((signal, object)->RocketComponent.targetless(entity));
        rocketComponent.setTarget(target);
        entity.add(rocketComponent);

        SteeringComponent.Builder builder = new SteeringComponent.Builder()
                .transformComponent(transformComponent)
                .velocityComponent(velocityComponent)
                .boundRadius(1)
                .tag(false)
                .zeroLinearSpeedThreshold(0.1f)
                .maxLinearSpeed(rocketWeapon.getMaxLinearSpeed())
                .maxLinearAcceleration(rocketWeapon.getMaxLinearAcceleration())
                .maxAngularSpeed(rocketWeapon.getMaxAngularSpeed())
                .maxAngularAcceleration(rocketWeapon.getMaxAngularAcceleration());
        SteeringComponent steeringComponent = engine.createComponent(SteeringComponent.class).init(builder);

        steeringComponent.behavior = new FaceThrust(steeringComponent, new LocationAdapter(targetTransform))
                .setTimeToTarget(0.01f)
                .setAlignTolerance(0.1f)
                .setDecelerationRadius(MathUtils.degreesToRadians * 1);
        entity.add(steeringComponent);

        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledRect(Color.FIREBRICK, rocketWeapon.getWidth(), rocketWeapon.getHeight())));
        engine.addEntity(entity);
        return entity;
    }

    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public float getAttackAmount() {
        return attackAmount;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isAutoAiming() {
        return autoAiming;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<B extends RocketWeapon.Builder<B, T>, T extends RocketWeapon> extends Weapon.Builder<B, T> {
        protected float maxLinearSpeed;
        protected float maxLinearAcceleration;
        protected float maxAngularSpeed;
        protected float maxAngularAcceleration;
        protected float width;
        protected float height;
        protected boolean autoAiming;

        public B setMaxLinearSpeed(float maxLinearSpeed) {
            this.maxLinearSpeed = maxLinearSpeed;
            return (B) this;
        }

        public B setMaxLinearAcceleration(float maxLinearAcceleration) {
            this.maxLinearAcceleration = maxLinearAcceleration;
            return (B) this;
        }

        public B setMaxAngularSpeed(float maxAngularSpeed) {
            this.maxAngularSpeed = maxAngularSpeed;
            return (B) this;
        }

        public B setMaxAngularAcceleration(float maxAngularAcceleration) {
            this.maxAngularAcceleration = maxAngularAcceleration;
            return (B) this;
        }

        public B setWidth(float width) {
            this.width = width;
            return (B) this;
        }

        public B setHeight(float height) {
            this.height = height;
            return (B) this;
        }

        public B setAutoAiming(boolean autoAiming) {
            this.autoAiming = autoAiming;
            return (B) this;
        }

        @Override
        public Weapon build() {
            return new RocketWeapon(this);
        }
    }

}
