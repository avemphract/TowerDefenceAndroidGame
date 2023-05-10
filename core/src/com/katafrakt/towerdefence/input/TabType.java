package com.katafrakt.towerdefence.input;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector3;
import com.katafrakt.towerdefence.ashley.components.FocusableComponent;

import java.util.function.BiConsumer;

public abstract class TabType {
    protected Family focusableFamily = Family.all(FocusableComponent.class).get();

    public abstract String getName();

    public abstract void tab(int screenX, int screenY);

    public abstract void drag(int screenX, int screenY);
}
