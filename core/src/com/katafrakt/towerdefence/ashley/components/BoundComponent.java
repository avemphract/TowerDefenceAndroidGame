package com.katafrakt.towerdefence.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.utility.DebugRender;

public class BoundComponent extends Vector2 implements Component, Pool.Poolable, DebugRender {
    public static final ComponentMapper<BoundComponent> MAPPER = ComponentMapper.getFor(BoundComponent.class);
    public float angle;

    public BoundComponent init(float width, float height) {
        return init(width, height, 0);
    }

    public BoundComponent init(float width, float height, float angle) {
        this.x = width;
        this.y = height;
        this.angle = angle;
        return this;
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        angle = 0;
    }


    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(transformComponent.x - x / 2, transformComponent.y - y / 2, x, y);
    }
}
