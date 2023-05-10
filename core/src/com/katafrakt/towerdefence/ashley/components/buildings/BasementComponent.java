package com.katafrakt.towerdefence.ashley.components.buildings;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.katafrakt.towerdefence.utility.DebugRender;

public class BasementComponent extends BuildingComponent implements DebugRender {
    public static final ComponentMapper<BasementComponent> MAPPER = ComponentMapper.getFor(BasementComponent.class);

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {

    }

    @Override
    public void reset() {

    }
}
