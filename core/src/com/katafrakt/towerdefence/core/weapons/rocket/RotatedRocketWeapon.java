package com.katafrakt.towerdefence.core.weapons.rocket;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;

public class RotatedRocketWeapon extends RocketWeapon {
    public static final float MIN_ACCURACY_RAD = MathUtils.HALF_PI;
    protected float accuracy;

    public RotatedRocketWeapon(Builder<?, ?> builder) {
        super(builder);
        this.accuracy = builder.accuracy;
    }

    @Override
    public void attack(Entity target) {
        float rAngle = MathUtils.random(-MIN_ACCURACY_RAD * (1 - accuracy), +MIN_ACCURACY_RAD * (1 - accuracy));
        Vector2 targetTransform = TransformComponent.MAPPER.get(target).cpy().rotateAroundRad(ownerTransform, rAngle);

        Vector2 vel = targetTransform.cpy().mulAdd(ownerTransform, -1);
        spawn(this, target, vel);
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public static class Builder<B extends RotatedRocketWeapon.Builder<B, T>, T extends RotatedRocketWeapon> extends RocketWeapon.Builder<B, T> {
        protected float accuracy;

        public B setAccuracy(float accuracy) {
            this.accuracy = accuracy;
            return (B) this;
        }

        @Override
        public Weapon build() {
            return new RotatedRocketWeapon(this);
        }
    }
}
