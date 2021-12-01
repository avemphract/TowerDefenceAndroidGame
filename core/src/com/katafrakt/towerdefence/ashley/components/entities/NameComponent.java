package com.katafrakt.towerdefence.ashley.components.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class NameComponent implements Component, Pool.Poolable {
    public static final ComponentMapper<NameComponent> MAPPER = ComponentMapper.getFor(NameComponent.class);

    public String name;

    public NameComponent init(String name){
        this.name=name;
        return this;
    }

    @Override
    public void reset() {
        name=null;
    }
}
