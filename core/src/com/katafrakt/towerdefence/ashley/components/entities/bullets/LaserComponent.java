package com.katafrakt.towerdefence.ashley.components.entities.bullets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.utility.DebugRender;
import com.katafrakt.towerdefence.utility.Renderable;

public class LaserComponent implements Pool.Poolable, Component, DebugRender {
    public static final ComponentMapper<LaserComponent> MAPPER = ComponentMapper.getFor(LaserComponent.class);
    public Vector2 endPoint;
    public float time;
    public float totalDamage;
    public float penetration;

    public float remainTime;
    public Vector2 realEnd;

    public LaserComponent init(Vector2 endPoint, float time, float totalDamage, float penetration) {
        this.endPoint = endPoint;
        this.time = time;
        this.totalDamage = totalDamage;
        this.penetration = penetration;
        remainTime = time;
        return this;
    }

    @Override
    public void reset() {

    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rectLine(transformComponent.x, transformComponent.y, realEnd.x, realEnd.y,0.5f);
    }
}
