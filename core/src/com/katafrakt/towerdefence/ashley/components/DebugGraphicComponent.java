package com.katafrakt.towerdefence.ashley.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.utility.DebugShapes;

public class DebugGraphicComponent implements Component, Pool.Poolable {
    public static final ComponentMapper<DebugGraphicComponent> MAPPER = ComponentMapper.getFor(DebugGraphicComponent.class);
    public DebugShapes debugShapes;
    public boolean isVisible;

    public DebugGraphicComponent init(DebugShapes debugShapes) {
        this.debugShapes = debugShapes;
        isVisible=true;
        return this;
    }

    @Override
    public void reset() {
        debugShapes=null;
        isVisible=true;
    }
}
