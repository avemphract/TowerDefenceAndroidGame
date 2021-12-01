package com.katafrakt.towerdefence.core.weapons.bullet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.BulletComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;


public class BulletWeapon extends Weapon {
    private static final String TAG = BulletWeapon.class.getSimpleName();
    public static final float MIN_ACCURACY_RAD = MathUtils.HALF_PI;
    protected float accuracy;       //between 0 and 1
    protected float bulletSpeed;
    protected float bulletRadius;

    public BulletWeapon(Builder<?, ? extends BulletWeapon> builder) {
        super(builder);
        this.bulletSpeed = builder.bulletSpeed;
        this.accuracy = builder.accuracy;
        this.bulletRadius = builder.bulletRadius;
    }

    @Override
    public void attack(Entity target) {
        float rAngle = MathUtils.randomTriangular(-MIN_ACCURACY_RAD * (1 - accuracy), +MIN_ACCURACY_RAD * (1 - accuracy));
        Vector2 targetTransform = TransformComponent.MAPPER.get(target).cpy().rotateAroundRad(ownerTransform, rAngle);

        Vector2 vel = targetTransform.cpy().mulAdd(ownerTransform, -1).setLength(bulletSpeed);
        float time = range / bulletSpeed;


        spawn(ownerTransform, vel, attackAmount, time, bulletRadius);
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public float getAccuracy() {
        return accuracy;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public float getBulletRadius() {
        return bulletRadius;
    }

    @Override
    public String toString() {
        return "BulletWeapon{" +
                "accuracy=" + accuracy +
                ", bulletSpeed=" + bulletSpeed +
                ", bulletRadius=" + bulletRadius +
                ", attackType=" + attackType +
                ", attackAmount=" + attackAmount +
                ", attackRate=" + attackRate +
                ", range=" + range +
                '}';
    }

    public static Entity spawn(Vector2 pos, Vector2 velocity,
                               float bulletDamage, float bulletRemainTime, float bulletRadius) {
        PooledEngine engine = GameManager.getInstance().getEngine();

        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(TransformComponent.class).init(pos.x, pos.y));
        entity.add(engine.createComponent(BoundComponent.class).init(bulletRadius * 2, bulletRadius * 2, MathUtils.atan2(velocity.y, velocity.x)));
        entity.add(engine.createComponent(VelocityComponent.class).init(velocity.x, velocity.y));
        entity.add(engine.createComponent(BulletComponent.class).init(bulletDamage, bulletRemainTime));

        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledCircle(Color.BLACK, bulletRadius)));

        engine.addEntity(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<B extends Builder<B, T>, T extends BulletWeapon> extends Weapon.Builder<B, T> {
        protected float bulletSpeed;
        protected float accuracy;
        protected float bulletRadius;

        public B setBulletSpeed(float bulletSpeed) {
            this.bulletSpeed = bulletSpeed;
            return (B) this;
        }

        public B setAccuracy(float accuracy) {
            this.accuracy = accuracy;
            return (B) this;
        }

        public B setBulletRadius(float bulletRadius) {
            this.bulletRadius = bulletRadius;
            return (B) this;
        }

        @Override
        public Weapon build() {
            return new BulletWeapon(this);
        }
    }
}
