package com.katafrakt.towerdefence.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.FocusableComponent;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.MinionAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.NameComponent;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;

public enum MinionType {
    ZOMBIE() {
        @Override
        public Entity createEntity(Entity player, float x, float y) {
            Entity entity = template(player, x, y);
            GameManager.getInstance().getEngine().addEntity(entity);
            return entity;
        }
    };

    public abstract Entity createEntity(Entity entity, float x, float y);

    public Entity template(Entity player, float x, float y) {
        PooledEngine engine = GameManager.getInstance().getEngine();
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(TransformComponent.class).init(x, y));
        entity.add(engine.createComponent(VelocityComponent.class));
        entity.add(engine.createComponent(BoundComponent.class).init(3, 3));

        HealthComponent healthComponent = engine.createComponent(HealthComponent.class).init(entity, 100);
        entity.add(healthComponent);
        entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.FilledCircle(Color.NAVY, 1)));

        entity.add(engine.createComponent(NameComponent.class).init("Guarder Minion"));
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

        entity.add(engine.createComponent(MinionAiComponent.class).init(entity, player));

        return entity;
    }
}
