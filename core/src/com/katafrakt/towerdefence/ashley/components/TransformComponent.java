package com.katafrakt.towerdefence.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.utility.DebugRender;

public class TransformComponent extends Vector2 implements Component, Pool.Poolable, DebugRender {
    public static final ComponentMapper<TransformComponent> MAPPER = ComponentMapper.getFor(TransformComponent.class);
    public float orientation;

    public TransformComponent init(float x, float y) {
        return init(x, y, 0);
    }

    public TransformComponent init(float x, float y, float rad) {
        this.x = x;
        this.y = y;
        orientation = rad;
        return this;
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        orientation = 0;
    }

    @Override
    public String toString() {
        return "TransformComponent{" +
                "orientation=" + orientation +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(x, y, 0.5f);
    }
}
