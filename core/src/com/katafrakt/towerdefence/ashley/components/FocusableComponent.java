package com.katafrakt.towerdefence.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.core.actions.PlayerAction;

public class FocusableComponent implements Component {
    public final static ComponentMapper<FocusableComponent> MAPPER = ComponentMapper.getFor(FocusableComponent.class);
    public enum Type{TOWER,PLAYER,UNIT}
    public Type type;
    public Array<PlayerAction> actions;
    public FocusableComponent init(Type type, PlayerAction... playerActions){
        this.type=type;
        this.actions=Array.with(playerActions);
        return this;
    }
}
