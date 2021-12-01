package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

public class AssetOrganizer {
    public static final String TAG = AssetOrganizer.class.getSimpleName();
    private static AssetOrganizer initiate;

    private InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
    private final AssetManager assetManager = new AssetManager();

    public static AssetOrganizer getInstance() { if (initiate == null) initiate = new AssetOrganizer();return initiate; }

    public void unloadAsset(String assetFileNamePath) {
        if (assetManager.isLoaded(assetFileNamePath))
            assetManager.unload(assetFileNamePath);
        else
            Gdx.app.error(TAG, "Asset is not loaded");
    }

    public <T> void load(String assetFileNamePath, Class<T> type) {
        if (fileHandleResolver.resolve(assetFileNamePath).exists()) {
            assetManager.load(assetFileNamePath, type);
        } else {
            Gdx.app.error(TAG, type.getSimpleName() + " + " + assetFileNamePath + " does not exist");
            throw new NullPointerException();
        }
    }

    public <T> void load(String assetFileNamePath, Class<T> type, AssetLoaderParameters<T> parameter) {
        if (fileHandleResolver.resolve(assetFileNamePath).exists()) {
            assetManager.load(assetFileNamePath, type, parameter);
        } else {
            Gdx.app.error(TAG, type.getSimpleName() + " + " + assetFileNamePath + " does not exist");
            throw new NullPointerException();
        }
    }

    public <T> T get(String assetFileNamePath, Class<T> type) {
        if (assetManager.isLoaded(assetFileNamePath, type))
            return assetManager.get(assetFileNamePath, type);
        else {
            Gdx.app.log(TAG, type.getSimpleName() + " + " + assetFileNamePath + " does not loaded");
            throw new NullPointerException();
        }
    }

    public boolean isLoaded(String assetFileNamePath){
        return assetManager.isLoaded(assetFileNamePath);
    }

    public boolean update() {
        return assetManager.update();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }
}
