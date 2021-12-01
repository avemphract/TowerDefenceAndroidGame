package com.katafrakt.towerdefence.core.weapons.bullet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.entities.spawner.BulletSpawner;

public class MultiParallelBulletWeapon extends BulletWeapon {
    private static final String TAG = MultiParallelBulletWeapon.class.getSimpleName();
    protected float bulletDistance;
    protected int bulletAmount;

    public MultiParallelBulletWeapon(MultiParallelBulletWeapon.Builder<?, ? extends MultiParallelBulletWeapon> builder) {
        super(builder);
        this.bulletDistance = builder.bulletDistance;
        this.bulletAmount = builder.bulletAmount;
    }

    @Override
    public void attack(Entity target) {
        Vector2 ownerVector = ownerTransform;
        Vector2 targetVector = TransformComponent.MAPPER.get(target);
        float rAngle = MathUtils.randomTriangular(-MathUtils.HALF_PI * (1 - accuracy), +MIN_ACCURACY_RAD * (1 - accuracy));
        float iAngle = targetVector.cpy().rotateAroundRad(ownerVector, rAngle).mulAdd(ownerVector, -1).angleRad() + MathUtils.HALF_PI;
        Vector2 iVector = Vector2.X.cpy().rotateRad(iAngle);
        for (int i = 0; i < bulletAmount; i++) {
            float f = -bulletDistance / 2 + bulletDistance * i / (bulletAmount - 1);
            Gdx.app.log(TAG, "iAngle: " + iAngle);
            Vector2 begin = ownerVector.cpy().mulAdd(iVector, f);
            Vector2 end = targetVector.cpy().mulAdd(iVector, f).rotateAroundRad(begin, rAngle);

            Vector2 vel = end.cpy().mulAdd(begin, -1).setLength(bulletSpeed);
            float time = range / bulletSpeed;

            spawn(begin, vel, attackAmount, time, bulletRadius);
        }
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public static class Builder<B extends MultiParallelBulletWeapon.Builder<B, T>, T extends MultiParallelBulletWeapon> extends BulletWeapon.Builder<B, T> {
        protected float bulletDistance;
        protected int bulletAmount;

        public Builder<B, T> setBulletDistance(float bulletDistance) {
            this.bulletDistance = bulletDistance;
            return this;
        }

        public Builder<B, T> setBulletAmount(int bulletAmount) {
            this.bulletAmount = bulletAmount;
            return this;
        }

        @Override
        public Weapon build() {
            return new MultiParallelBulletWeapon(this);
        }
    }
}
