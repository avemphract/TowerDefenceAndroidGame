package com.katafrakt.towerdefence.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.utility.DebugRender;

public class HealthComponent implements Component, Pool.Poolable, DebugRender {
    public static final ComponentMapper<HealthComponent> MAPPER = ComponentMapper.getFor(HealthComponent.class);
    private Entity entity;

    public Signal<Float> healthChange = new Signal<>(); //if damaged -
    public float maxHealth;
    private float currentHealth;

    public Signal<Entity> aliveSignal = new Signal<>();
    private boolean alive;

    public HealthComponent init(Entity entity, float maxHealth) {
        this.entity = entity;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        alive=true;
        return this;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth <= 0 ? 0 : currentHealth;
    }

    public void addCurrentHealth(float additionHealth) {
        this.currentHealth = MathUtils.clamp(currentHealth + additionHealth,0,maxHealth);
        healthChange.dispatch(additionHealth);
    }


    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        aliveSignal.dispatch(entity);
        aliveSignal.removeAllListeners();
    }

    @Override
    public void reset() {
        healthChange.removeAllListeners();
        maxHealth = 0;
        currentHealth = 0;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        BoundComponent boundComponent = BoundComponent.MAPPER.get(entity);
        float maxWidth = boundComponent.x;
        float yOffset = boundComponent.y / 1.5f;
        float currentWidth = maxWidth * getCurrentHealth() / maxHealth;
        float xBegin = transformComponent.x - maxWidth / 2;
        shapeRenderer.setColor(Color.SCARLET);
        shapeRenderer.rect(xBegin - 0.25f, transformComponent.y - 0.25f + yOffset, maxWidth + 0.5f, 1.5f);
        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.rect(xBegin, transformComponent.y + yOffset, currentWidth, 1);
    }
}
