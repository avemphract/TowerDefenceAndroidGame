package com.katafrakt.towerdefence.ashley.components.buildings;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.core.weapons.bullet.BulletWeapon;
import com.katafrakt.towerdefence.core.weapons.laser.LaserWeapon;
import com.katafrakt.towerdefence.core.weapons.mortar.MortarWeapon;
import com.katafrakt.towerdefence.core.weapons.rocket.RocketWeapon;
import com.katafrakt.towerdefence.utility.BeforeEngine;
import com.katafrakt.towerdefence.utility.DebugRender;

public class TowerComponent implements Component, Pool.Poolable, BeforeEngine, DebugRender {
    private static final String TAG = TowerComponent.class.getSimpleName();
    public static ComponentMapper<TowerComponent> MAPPER = ComponentMapper.getFor(TowerComponent.class);

    public Weapon weapon;

    public TowerComponent init(Entity entity, Weapon weapon) {
        this.weapon = weapon;
        weapon.setOwner(entity);
        return this;
    }

    @Override
    public void reset() {
        weapon = null;
    }

    @Override
    public void beforeEngine(PooledEngine engine, Entity own) {

    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        TowerAiComponent towerAiComponent = TowerAiComponent.MAPPER.get(entity);
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        shapeRenderer.setColor(Color.BLACK);
        if (towerAiComponent.getTarget() != null) {
            TransformComponent targetTransform = TransformComponent.MAPPER.get(towerAiComponent.getTarget());
            if (weapon instanceof BulletWeapon) {
                BulletWeapon bulletWeapon = (BulletWeapon) weapon;
                shapeRenderer.line(transformComponent, targetTransform.cpy().rotateAroundRad(transformComponent, (1 - bulletWeapon.getAccuracy()) * BulletWeapon.MIN_ACCURACY_RAD));
                shapeRenderer.line(transformComponent, targetTransform.cpy().rotateAroundRad(transformComponent, -(1 - bulletWeapon.getAccuracy()) * BulletWeapon.MIN_ACCURACY_RAD));
                shapeRenderer.line(transformComponent, targetTransform);
            } else if (weapon instanceof MortarWeapon) {
                MortarWeapon mortarWeapon = (MortarWeapon) weapon;
                VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(towerAiComponent.getTarget());
                shapeRenderer.circle(targetTransform.x + velocityComponent.x * ((MortarWeapon) weapon).getTime(), targetTransform.y + velocityComponent.y * ((MortarWeapon) weapon).getTime(), mortarWeapon.getAccuracyRadius());
            } else if (weapon instanceof RocketWeapon) {
                shapeRenderer.line(targetTransform.x - 5, targetTransform.y, targetTransform.x + 5, targetTransform.y);
                shapeRenderer.line(targetTransform.x, targetTransform.y - 5, targetTransform.x, targetTransform.y + 5);
            }
            else if (weapon instanceof LaserWeapon){
                shapeRenderer.line(transformComponent, targetTransform);
            }
        }

        if (weapon != null) {
            shapeRenderer.circle(transformComponent.x, transformComponent.y, weapon.getRange());
        }
    }
}
