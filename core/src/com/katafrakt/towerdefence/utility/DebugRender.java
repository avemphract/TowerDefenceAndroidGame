package com.katafrakt.towerdefence.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface DebugRender {
    void render(ShapeRenderer shapeRenderer, Entity entity);

    @interface type{
        ShapeRenderer.ShapeType value();
    }
}
