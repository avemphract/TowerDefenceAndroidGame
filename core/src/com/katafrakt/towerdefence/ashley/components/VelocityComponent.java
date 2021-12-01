package com.katafrakt.towerdefence.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.utility.DebugRender;

public class VelocityComponent extends Vector2 implements Component, Pool.Poolable, DebugRender {
    public static ComponentMapper<VelocityComponent> MAPPER = ComponentMapper.getFor(VelocityComponent.class);
    public float angular;

    public VelocityComponent init(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public VelocityComponent init(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    @Override
    public String toString() {
        return "VelocityComponent{" +
                "angular=" + angular +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        angular = 0;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.line(transformComponent.x, transformComponent.y, transformComponent.x + x, transformComponent.y + y);
    }
}
