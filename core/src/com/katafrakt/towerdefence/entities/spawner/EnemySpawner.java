package com.katafrakt.towerdefence.entities.spawner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.EffectComponent;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.EnemyAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.ashley.components.entities.NameComponent;
import com.katafrakt.towerdefence.enemy.abilities.Amplifier;
import com.katafrakt.towerdefence.enemy.abilities.controller.DeathOnlyController;
import com.katafrakt.towerdefence.enemy.abilities.controller.GetDamagedController;
import com.katafrakt.towerdefence.enemy.abilities.controller.TimeController;
import com.katafrakt.towerdefence.enemy.abilities.effects.HealInstantEffect;
import com.katafrakt.towerdefence.enemy.abilities.effects.HealUpdateEffect;
import com.katafrakt.towerdefence.enemy.abilities.effects.SpeedUpdateEffect;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;

public class EnemySpawner {

    public Entity spawn(float x, float y) {
        PooledEngine engine = GameManager.getInstance().getEngine();
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(TransformComponent.class).init(x, y));
        entity.add(engine.createComponent(VelocityComponent.class));
        entity.add(engine.createComponent(BoundComponent.class).init(5, 5));

        HealthComponent healthComponent = engine.createComponent(HealthComponent.class).init(entity, 100);
        entity.add(healthComponent);
        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledCircle(Color.MAROON, 3)));

        entity.add(engine.createComponent(NameComponent.class).init("Enemy"));
        SteeringComponent.Builder builder = new SteeringComponent.Builder()
                .transformComponent(TransformComponent.MAPPER.get(entity))
                .velocityComponent(VelocityComponent.MAPPER.get(entity))
                .maxLinearAcceleration(18f)
                .maxLinearSpeed(9f)
                .maxAngularAcceleration(0.5f)
                .maxAngularSpeed(2f)
                .zeroLinearSpeedThreshold(0.001f);
        SteeringComponent steeringComponent = engine.createComponent(SteeringComponent.class).init(builder);
        entity.add(steeringComponent);

        EnemyAiComponent enemyAiComponent = engine.createComponent(EnemyAiComponent.class).init(entity);
        entity.add(enemyAiComponent);

        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class).init(entity);
        entity.add(enemyComponent);


        healthComponent.aliveSignal.add((signal, bool) -> enemyAiComponent.getCurrentNode().enemyEntities.remove(entity));

        //enemyComponent.abilityControllerArray.add(
        //        new DeathOnlyController(entity,new HealInstantEffect(Amplifier.AreaAmplifier.get(false,30)
        //                ,10)));

        //enemyComponent.abilityControllerArray.add(
        //        new GetDamagedController(entity,new TimeController(entity,new HealUpdateEffect(20,Amplifier.AreaAmplifier.get(false,30)),2),true));
        EffectComponent effectComponent = engine.createComponent(EffectComponent.class);

        effectComponent.effects.add(
                new GetDamagedController(new TimeController(new SpeedUpdateEffect(10,10), 0.5f), entity, false));
        entity.add(effectComponent);

        engine.addEntity(entity);
        return entity;
    }
}
