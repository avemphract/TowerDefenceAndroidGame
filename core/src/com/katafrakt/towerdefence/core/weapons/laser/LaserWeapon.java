package com.katafrakt.towerdefence.core.weapons.laser;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.LaserComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.MortarComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.core.weapons.bullet.BulletWeapon;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;

public class LaserWeapon extends Weapon {
    private static final String TAG = LaserWeapon.class.getSimpleName();
    private int penetration;
    private int time;

    public LaserWeapon(Builder<?, ?> builder) {
        super(builder);
        penetration = builder.penetration;
        time = builder.time;
    }

    @Override
    public void attack(Entity target) {
        Gdx.app.log(TAG,"attacked");
        TransformComponent enemyTransform = TransformComponent.MAPPER.get(target);
        Vector2 end=ownerTransform.cpy().add(enemyTransform.cpy().mulAdd(ownerTransform,-1).setLength(range));

        spawn(ownerTransform, end, time, attackAmount, penetration);
        lastAttackTime = Main.getMain().getTotalTime();
    }

    protected static void spawn(Vector2 pos, Vector2 end,
                                float time, float totalDamage, float penetration) {
        PooledEngine engine = GameManager.getInstance().getEngine();

        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(TransformComponent.class).init(pos.x, pos.y));
        entity.add(engine.createComponent(LaserComponent.class).init(end, time, totalDamage, penetration));
        entity.add(engine.createComponent(DebugGraphicComponent.class));

        engine.addEntity(entity);

    }

    public static class Builder<B extends LaserWeapon.Builder<B, T>, T extends LaserWeapon> extends Weapon.Builder<B, T> {
        private int penetration;
        private int time;

        public B setPenetration(int penetration) {
            this.penetration = penetration;
            return (B) this;
        }

        public B setTime(int time) {
            this.time = time;
            return (B) this;
        }

        @Override
        public Weapon build() {
            return new LaserWeapon(this);
        }
    }
}
