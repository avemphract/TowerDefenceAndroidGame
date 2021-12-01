package com.katafrakt.towerdefence.desktop.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.input.InputProcessorModified;
import com.katafrakt.towerdefence.input.TabType;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.screens.GameManager;

public class DesktopInput extends InputProcessorModified {
    private static final String TAG = DesktopInput.class.getSimpleName();

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE)
            InputProcessorModified.RESUME=!InputProcessorModified.RESUME;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    Vector3 gamePos = new Vector3();
    float lastClickedTime;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        gamePos.set(screenX, screenY, 0);
        Gdx.app.debug(TAG, "Touched Down: gameX: " + camera.unproject(gamePos));
        lastClickedTime = Main.getMain().getTotalTime();
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(gamePos);
        lastMousePosition = null;
        if (Main.getMain().getTotalTime() - lastClickedTime < 0.5f) {
            Main.getMain().getInputProcessor().getTabType().tab(screenX, screenY);
        }
        return false;
    }

    float[] lastMousePosition;
    float[] signalPosition = new float[4];

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float posX = (screenX * (camera.viewportWidth / screenWidth) * camera.zoom);
        float posY = (screenY * (camera.viewportHeight / screenHeight) * camera.zoom);

        if (lastMousePosition == null) {
            lastMousePosition = new float[]{posX, posY};
            signalPosition[0] = screenX;
            signalPosition[1] = screenY;
        } else {
            camera.position.add(0, (posY - lastMousePosition[1]), 0);
            camera.position.add((-posX + lastMousePosition[0]), 0, 0);
            cameraReceiver.dispatch(camera);

            lastMousePosition[0] = posX;
            lastMousePosition[1] = posY;

            signalPosition[2] = signalPosition[0];
            signalPosition[3] = signalPosition[1];
            signalPosition[0] = screenX;
            signalPosition[1] = screenY;
            dragReceiver.dispatch(signalPosition);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        //if (((camera.zoom+2*amount)*camera.viewportWidth<MapOrganizer.getInstance().pixelWidth)&&((camera.zoom+2*amount)*camera.viewportHeight<MapOrganizer.getInstance().pixelHeight))

        camera.zoom += amountY / 2;
        if (camera.zoom < 0.5f)
            camera.zoom = 0.5f;
        if (camera.zoom > 8)
            camera.zoom = 8;
        //camera.position.x= MathUtils.clamp(camera.position.x,camera.viewportWidth/2*camera.zoom, MapOrganizer.getInstance().pixelWidth-camera.viewportWidth/2*camera.zoom);
        //camera.position.y=MathUtils.clamp(camera.position.y,camera.viewportHeight/2*camera.zoom, MapOrganizer.getInstance().pixelWidth-camera.viewportHeight/2*camera.zoom);
        camera.update();
        cameraReceiver.dispatch(camera);
        return true;
    }
}
