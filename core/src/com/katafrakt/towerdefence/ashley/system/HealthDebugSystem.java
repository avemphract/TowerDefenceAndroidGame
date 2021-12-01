package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;

import java.util.Comparator;

public class HealthDebugSystem extends IteratingSystem {
    private final ShapeRenderer shapeRenderer;
    private final Array<Entity> entities = new Array<>();
    private final Comparator<Entity> comparator = new EntityComparator();

    public HealthDebugSystem(ShapeRenderer shapeRenderer) {
        super(Family.all(TransformComponent.class, HealthComponent.class).get());
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        entities.sort(comparator);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity entity : entities) {
            HealthComponent healthComponent = HealthComponent.MAPPER.get(entity);
            TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
            BoundComponent boundComponent = BoundComponent.MAPPER.get(entity);
            float maxWidth = boundComponent.x;
            float yOffset = boundComponent.y / 1.5f;
            float currentWidth = maxWidth * healthComponent.getCurrentHealth() / healthComponent.maxHealth;
            float xBegin = transformComponent.x - maxWidth / 2;
            shapeRenderer.setColor(Color.SCARLET);
            shapeRenderer.rect(xBegin - 0.25f, transformComponent.y - 0.25f + yOffset, maxWidth + 0.5f, 1.5f);
            shapeRenderer.setColor(Color.LIME);
            shapeRenderer.rect(xBegin, transformComponent.y + yOffset, currentWidth, 1);
        }
        shapeRenderer.end();
        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }

    static class EntityComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity o1, Entity o2) {
            return Float.compare(TransformComponent.MAPPER.get(o1).y, TransformComponent.MAPPER.get(o2).y);
        }
    }
}
