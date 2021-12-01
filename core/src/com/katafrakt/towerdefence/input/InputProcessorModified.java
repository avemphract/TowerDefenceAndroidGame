package com.katafrakt.towerdefence.input;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class InputProcessorModified implements InputProcessor {
    public static boolean RESUME = true;
    protected OrthographicCamera camera;
    protected int screenWidth, screenHeight;

    public TabType defaultTabType;
    protected TabType tabType;
    public final Signal<TabType> tabTypeReceiver = new Signal<>();
    public final Signal<float[]> dragReceiver = new Signal<>();
    public final Signal<OrthographicCamera> cameraReceiver = new Signal<>();

    public InputProcessor init(OrthographicCamera camera) {
        this.camera = camera;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        defaultTabType = new MoveTabType();
        tabType = defaultTabType;
        return this;
    }

    public TabType getTabType() {
        return tabType;
    }

    public void setTabType(TabType tabType) {
        this.tabType = tabType;
        tabTypeReceiver.dispatch(tabType);
    }
}
