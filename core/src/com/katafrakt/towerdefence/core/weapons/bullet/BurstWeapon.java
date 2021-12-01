package com.katafrakt.towerdefence.core.weapons.bullet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.entities.spawner.BulletSpawner;

public class BurstWeapon extends BulletWeapon {
    protected float burstInterval;
    protected float burstCount;

    public BurstWeapon(Builder<?, ? extends ShotgunBulletWeapon> builder) {
        super(builder);
        this.burstInterval = builder.burstInterval;
        this.burstCount = builder.burstCount;
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

    @Override
    public float getAttackRate() {
        return super.getAttackRate();
    }

    public static class Builder<B extends ShotgunBulletWeapon.Builder<B, T>, T extends ShotgunBulletWeapon> extends BulletWeapon.Builder<B, T> {
        protected float burstInterval;
        protected float burstCount;

        public Builder<B, T> setBurstInterval(float burstInterval) {
            this.burstInterval = burstInterval;
            return this;
        }

        public Builder<B, T> setBurstCount(float burstCount) {
            this.burstCount = burstCount;
            return this;
        }

        @Override
        public Weapon build() {
            return new BurstWeapon(this);
        }
    }
}
