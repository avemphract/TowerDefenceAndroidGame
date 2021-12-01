package com.katafrakt.towerdefence.entities.spawner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.BulletComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.MortarComponent;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;


public class BulletSpawner {

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

    public static Entity mortarSpawn(Vector2 pos, Vector2 velocity,
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

/*
    public static Entity rapidRocketSpawn(RocketWeapon rocketWeapon, Entity target) {
        PooledEngine engine = GameManager.getInstance().getEngine();
        TransformComponent pos = rocketWeapon.getOwnerTransform();
        TransformComponent targetTransform = TransformComponent.MAPPER.get(target);
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(BoundComponent.class).init(rocketWeapon.getHeight(), rocketWeapon.getWidth()));


        VelocityComponent velocityComponent;
        if (rocketWeapon.isDirectedFromStart())
            velocityComponent = engine.createComponent(VelocityComponent.class).init(targetTransform.cpy().mulAdd(pos,-1).setLength(rocketWeapon.getMaxLinearSpeed()));
        else
            velocityComponent = null;//engine.createComponent(VelocityComponent.class).init(Vector2.X.cpy().setToRandomDirection().setLength(rocketWeapon.getMaxLinearSpeed()));
        entity.add(velocityComponent);

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class).init(pos.x, pos.y, velocityComponent.angleRad() + MathUtils.HALF_PI);
        entity.add(transformComponent);

        RocketComponent rocketComponent = engine.createComponent(RocketComponent.class).init(target, 50, rocketWeapon.getAttackAmount());
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
                .setTimeToTarget(1f) //
                .setAlignTolerance(0.5f) //
                .setDecelerationRadius(MathUtils.degreesToRadians * 5);
        entity.add(steeringComponent);

        rocketComponent.listener = ((signal, object) -> {
            if (rocketWeapon.isAutoAiming()) {
                RocketComponent.scanNewTarget(entity);
            } else {
                RocketComponent.targetless(entity);
            }
        });

        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledRect(Color.FIREBRICK, rocketWeapon.getWidth(), rocketWeapon.getHeight())));
        engine.addEntity(entity);
        return entity;
    }*/

}
