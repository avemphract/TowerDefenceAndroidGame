package com.katafrakt.towerdefence.core.weapons.circle;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.AreaExpanderComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;

public class AreaExpanderWeapon extends Weapon {
    protected float radiusSpeed;

    public AreaExpanderWeapon(Builder<?, ?> builder) {
        super(builder);
        this.radiusSpeed = builder.radiusSpeed;
    }

    @Override
    public void attack(Entity target) {
        float remainingTime = range / radiusSpeed;
        spawn(ownerTransform, radiusSpeed, remainingTime, attackAmount);
        lastAttackTime = Main.getMain().getTotalTime();
    }

    public static void spawn(Vector2 pos, float radiusSpeed, float remainingTime, float damage) {
        PooledEngine engine = GameManager.getInstance().getEngine();

        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(TransformComponent.class).init(pos.x, pos.y));
        entity.add(engine.createComponent(AreaExpanderComponent.class).init(damage,remainingTime, radiusSpeed));
        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledCircle(Color.FOREST,1)));

        engine.addEntity(entity);

    }

    @SuppressWarnings("unchecked")
    public static class Builder<B extends AreaExpanderWeapon.Builder<B, T>, T extends AreaExpanderWeapon> extends Weapon.Builder<B, T> {
        protected float radiusSpeed;

        public B setRadiusSpeed(float radiusSpeed) {
            this.radiusSpeed = radiusSpeed;
            return (B) this;
        }

        @Override
        public Weapon build() {
            return new AreaExpanderWeapon(this);
        }
    }
}
