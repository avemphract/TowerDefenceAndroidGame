package com.katafrakt.towerdefence.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.EffectComponent;
import com.katafrakt.towerdefence.ashley.components.FocusableComponent;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.SpawnComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.EnemyAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.ashley.components.entities.NameComponent;
import com.katafrakt.towerdefence.ashley.components.entities.PlayerComponent;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.core.weapons.mortar.MortarWeapon;
import com.katafrakt.towerdefence.enemy.abilities.controller.GetDamagedController;
import com.katafrakt.towerdefence.enemy.abilities.controller.TimeController;
import com.katafrakt.towerdefence.enemy.abilities.effects.SpeedUpdateEffect;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;

public enum PlayerType {
    GUARDER() {
        @Override
        public Entity createEntity(float x, float y) {
            PooledEngine engine = GameManager.getInstance().getEngine();
            Entity entity = createTemplate(x, y);
            engine.addEntity(entity);
            return entity;
        }
    };

    final MortarWeapon.Builder<?, MortarWeapon> builder6 = new MortarWeapon.Builder<>()
            .setAttackAmount(10)
            .setAttackRate(5)
            .setRange(35)
            .setAccuracyRadius(10)
            .setTime(1)
            .setBulletRadius(1.5f);

    public abstract Entity createEntity(float x, float y);

    public Entity createEntity(Node node) {
        return createEntity(node.x, node.y);
    }

    protected Entity createTemplate(float x, float y) {
        PooledEngine engine = GameManager.getInstance().getEngine();
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(TransformComponent.class).init(x, y));
        entity.add(engine.createComponent(VelocityComponent.class));
        entity.add(engine.createComponent(BoundComponent.class).init(5, 5));
        entity.add(engine.createComponent(FocusableComponent.class));

        HealthComponent healthComponent = engine.createComponent(HealthComponent.class).init(entity, 100);
        entity.add(healthComponent);
        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledCircle(Color.ROYAL, 3)));

        entity.add(engine.createComponent(NameComponent.class).init("Guarder"));
        SteeringComponent.Builder builder = new SteeringComponent.Builder()
                .transformComponent(TransformComponent.MAPPER.get(entity))
                .velocityComponent(VelocityComponent.MAPPER.get(entity))
                .maxLinearAcceleration(45f)
                .maxLinearSpeed(9f)
                .maxAngularAcceleration(0.5f)
                .maxAngularSpeed(2f)
                .zeroLinearSpeedThreshold(0.001f)
                .boundRadius(2.5f);
        SteeringComponent steeringComponent = engine.createComponent(SteeringComponent.class).init(builder);
        entity.add(steeringComponent);

        PlayerAiComponent playerAiComponent = engine.createComponent(PlayerAiComponent.class).init(entity);
        entity.add(playerAiComponent);

        Weapon weapon = builder6.build();
        weapon.setOwner(entity);
        entity.add(engine.createComponent(PlayerComponent.class).init(weapon));

        entity.add(engine.createComponent(SpawnComponent.class).init(MinionType.ZOMBIE, 16f, 50));

        healthComponent.aliveSignal.add((signal, bool) -> playerAiComponent.getCurrentNode().enemyEntities.remove(entity));
        return entity;
    }
}
