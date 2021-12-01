package com.katafrakt.towerdefence.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.map.Map;

public class GameManager implements Pool.Poolable {
    private static GameManager INSTANCE;
    private GameManager(){}
    public static GameManager getInstance(){
        if (INSTANCE ==null)
            INSTANCE =new GameManager();
        return INSTANCE;
    }

    private Map map;
    public Map getMap(){return map;}
    public GameManager setMap(Map map){this.map=map;return this;}

    private PooledEngine engine;
    public PooledEngine getEngine(){ return engine; }
    public GameManager setEngine(PooledEngine engine){ this.engine=engine;return this; }

    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    public InputMultiplexer getInputMultiplexer() { return inputMultiplexer; }

    private OrthographicCamera camera;
    public OrthographicCamera getCamera() { return camera; }
    public GameManager setCamera(OrthographicCamera camera) { this.camera = camera;return this; }

    @Override
    public void reset() {
        map=null;
        engine=null;
    }
}
