package com.katafrakt.towerdefence.input;

import com.badlogic.gdx.math.Vector3;

import java.util.function.BiConsumer;

public abstract class TabType {

    public abstract String getName();

    public abstract void tab(int screenX, int screenY);

    public abstract void drag(int screenX, int screenY);
}
