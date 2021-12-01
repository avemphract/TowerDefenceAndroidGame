package com.katafrakt.towerdefence.enemy.abilities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.screens.GameManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public enum Amplifier implements Function<Entity, Set<Entity>> {
    OWN{
        @Override
        public Set<Entity> apply(Entity entity) {
            return Collections.singleton(entity);
        }
    };

    public static class AreaAmplifier implements Function<Entity, Set<Entity>> {
        private final boolean includeOwn;
        private final float radius;

        private AreaAmplifier(boolean includeOwn, float radius) {
            this.includeOwn = includeOwn;
            this.radius = radius;
        }

        @Override
        public Set<Entity> apply(Entity entity) {
            Set<Entity> entities = new HashSet<>();
            TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
            for (Entity potentialEntity : GameManager.getInstance().getEngine().getEntitiesFor(EnemyComponent.FAMILY)) {
                if (entity == potentialEntity && includeOwn) {
                    continue;
                }
                TransformComponent transformPotential = TransformComponent.MAPPER.get(potentialEntity);
                if (transformComponent.dst(transformPotential) < radius) {
                    entities.add(potentialEntity);
                }
            }
            return entities;
        }


        public static AreaAmplifier get(boolean includeOwn, float radius) {
            return new AreaAmplifier(includeOwn, radius);
        }
    }

    public static class FixedAreaAmplifier implements Function<Entity, Set<Entity>>{
        boolean isFirst=true;
        Entity entity;

        @Override
        public Set<Entity> apply(Entity entity) {
            if (isFirst){
                Entity pot = GameManager.getInstance().getEngine().createEntity();

            }
            return null;
        }
    }
}
