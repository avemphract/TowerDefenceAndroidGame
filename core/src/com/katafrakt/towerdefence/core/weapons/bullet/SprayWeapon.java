package com.katafrakt.towerdefence.core.weapons.bullet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.entities.spawner.BulletSpawner;

public class SprayWeapon extends BulletWeapon {
    protected int bulletAmount;
    protected float totalAngle;


    public SprayWeapon(Builder<?, ? extends BulletWeapon> builder) {
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
            Vector2 end = targetVector.cpy().rotateAroundRad(begin, rAngle + MathUtils.randomTriangular(-totalAngle / 2, +totalAngle / 2));
            Vector2 vel = end.cpy().mulAdd(begin, -1).setLength(MathUtils.random(bulletSpeed / 2, bulletSpeed));
            float time = MathUtils.randomTriangular(range / bulletSpeed * 0.5f * bulletSpeed/vel.len(), range / bulletSpeed * 1.2f, range / bulletSpeed);

            spawn(begin, vel, attackAmount, time, bulletRadius);
        }
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public static class Builder<B extends SprayWeapon.Builder<B, T>, T extends SprayWeapon> extends BulletWeapon.Builder<B, T> {
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
            return new SprayWeapon(this);
        }
    }
}
