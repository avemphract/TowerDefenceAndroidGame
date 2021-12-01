package com.katafrakt.towerdefence.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.katafrakt.towerdefence.screens.GameManager;

import java.util.function.BiConsumer;

public class SpawnTabType extends TabType{
    private final String name;
    private final BiConsumer<Float,Float> biConsumer;
    private final OrthographicCamera camera;
    public SpawnTabType(String name , BiConsumer<Float, Float> biConsumer) {
        this.name=name;
        this.biConsumer = biConsumer;
        camera = GameManager.getInstance().getCamera();
    }

    @Override
    public String getName() {
        return "Spawn "+name;
    }

    private Vector3 temp=new Vector3();
    @Override
    public void tab(int screenX, int screenY) {
        camera.unproject(temp.set(screenX,screenY,0));
        biConsumer.accept(temp.x,temp.y);
    }

    @Override
    public void drag(int screenX, int screenY) {

    }
}
