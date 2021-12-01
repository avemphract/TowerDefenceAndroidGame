package com.katafrakt.towerdefence.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.utility.AssetOrganizer;

public class LoadingScreen implements Screen {
    private static final String TAG = LoadingScreen.class.getSimpleName();
    private final Main main;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;

    private float progress;

    public LoadingScreen(Main main,ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        this.main = main;
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void show() {
        loadAssets();

    }

    @Override
    public void render(float delta) {
        if(!AssetOrganizer.getInstance().update()){
            progress=AssetOrganizer.getInstance().getProgress();
            Gdx.input.setInputProcessor(GameManager.getInstance().getInputMultiplexer());
        }
        else {
            ScreenManager.getInstance().push(new GameScreen(shapeRenderer,spriteBatch));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void loadAssets(){
        AssetOrganizer.getInstance().load("skins/neutralizer/skin.atlas", TextureAtlas.class);
        AssetOrganizer.getInstance().load("skins/neutralizer/skin.json", Skin.class, new SkinLoader.SkinParameter("skins/neutralizer/skin.atlas"));
    }
}
