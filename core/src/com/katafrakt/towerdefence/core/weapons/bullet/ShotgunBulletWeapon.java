package com.katafrakt.towerdefence.core.weapons.bullet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.entities.spawner.BulletSpawner;

public class ShotgunBulletWeapon extends BulletWeapon {
    protected int bulletAmount;
    protected float totalAngle;

    public ShotgunBulletWeapon(Builder<?, ?> builder) {
        super(builder);
        this.bulletAmount = builder.bulletAmount;
        this.totalAngle = builder.totalAngle;
    }

    @Override
    public void attack(Entity target) {
        Vector2 ownerVector = ownerTransform;
        Vector2 targetVector = TransformComponent.MAPPER.get(target);
        float rAngle = MathUtils.randomTriangular(-MIN_ACCURACY_RAD * (1 - accuracy), +MIN_ACCURACY_RAD * (1 - accuracy));
        for (int i = 0; i < bulletAmount; i++) {
            Vector2 begin = ownerVector.cpy();
            Vector2 end = targetVector.cpy().rotateAroundRad(begin, rAngle + totalAngle*i/(bulletAmount-1));
            Vector2 vel = end.cpy().mulAdd(begin, -1).setLength(bulletSpeed);
            float time = range / bulletSpeed;

            spawn(begin, vel, attackAmount, time, bulletRadius);
        }
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public int getBulletAmount() {
        return bulletAmount;
    }

    public float getTotalAngle() {
        return totalAngle;
    }

    public static class Builder<B extends ShotgunBulletWeapon.Builder<B, T>, T extends ShotgunBulletWeapon> extends BulletWeapon.Builder<B, T> {
        protected int bulletAmount;
        protected float totalAngle;

        public Builder<B, T> setBulletAmount(int bulletAmount) {
            this.bulletAmount = bulletAmount;
            return this;
        }

        public Builder<B, T> setTotalAngle(float totalAngle) {
            this.totalAngle = totalAngle;
            return this;
        }

        @Override
        public Weapon build() {
            return new ShotgunBulletWeapon(this);
        }
    }
}
