package com.katafrakt.towerdefence.core.weapons.mortar;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ai.steer.NodeFollowPath;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.MortarComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;

public class MortarWeapon extends Weapon {
    protected float accuracyRadius;       //between 0 and 1
    protected float time;
    protected float bulletRadius;

    public MortarWeapon(MortarWeapon.Builder<?, ?> builder) {
        super(builder);
        this.accuracyRadius = builder.accuracyRadius;
        this.time = builder.time;
        this.bulletRadius = builder.bulletRadius;
    }

    @Override
    public void attack(Entity target) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(target);

        Vector2 targetTransform = TransformComponent.MAPPER.get(target).cpy().mulAdd(velocityComponent,time).mulAdd(Vector2.X.cpy().rotateRad(MathUtils.random(0, MathUtils.PI2)), MathUtils.random(accuracyRadius));
        float time = MathUtils.random(0.85f, 1.15f) * this.time;
        Vector2 vel = targetTransform.cpy().mulAdd(ownerTransform, -1).setLength(MathUtils.clamp(targetTransform.dst(ownerTransform),0,range) / time);

        spawn(ownerTransform, vel, attackAmount, time, bulletRadius);
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public float getAccuracyRadius() {
        return accuracyRadius;
    }

    public float getTime() {
        return time;
    }

    public float getBulletRadius() {
        return bulletRadius;
    }

    public static Entity spawn(Vector2 pos, Vector2 velocity,
                                     float attackAmount, float mortarTime, float mortarRadius) {
        PooledEngine engine = GameManager.getInstance().getEngine();

        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(TransformComponent.class).init(pos.x, pos.y));
        entity.add(engine.createComponent(BoundComponent.class).init(mortarRadius * 2, mortarRadius * 2, MathUtils.atan2(velocity.y, velocity.x)));
        entity.add(engine.createComponent(VelocityComponent.class).init(velocity.x, velocity.y));
        entity.add(engine.createComponent(MortarComponent.class).init(attackAmount, mortarTime));
        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledCircle(Color.BROWN, mortarRadius)));

        engine.addEntity(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<B extends MortarWeapon.Builder<B, T>, T extends MortarWeapon> extends Weapon.Builder<B, T> {
        protected float accuracyRadius;
        protected float time;
        protected float bulletRadius;

        public B setAccuracyRadius(float accuracyRadius) {
            this.accuracyRadius = accuracyRadius;
            return (B) this;
        }

        public B setTime(float time) {
            this.time = time;
            return (B) this;
        }

        public B setBulletRadius(float bulletRadius) {
            this.bulletRadius = bulletRadius;
            return (B) this;
        }

        @Override
        public Weapon build() {
            return new MortarWeapon(this);
        }
    }
}
