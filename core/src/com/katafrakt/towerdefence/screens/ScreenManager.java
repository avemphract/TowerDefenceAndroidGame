package com.katafrakt.towerdefence.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.katafrakt.towerdefence.Main;

import java.util.Stack;

public class ScreenManager extends Stack<Screen> {
    private static final String TAG = ScreenManager.class.getSimpleName();
    private static ScreenManager INSTANCE;

    private ScreenManager(){super();}
    public static ScreenManager getInstance(){if (INSTANCE ==null) INSTANCE =new ScreenManager(); return INSTANCE;}

    @Override
    public Screen push(Screen screen) {
        super.push(screen);
        Main.getMain().setScreen(screen);
        Gdx.app.log(TAG,"new Screen: "+screen.getClass().getSimpleName());
        return screen;
    }

    @Override
    public synchronized Screen pop() {
        Screen screen= super.pop();
        Main.getMain().setScreen(peek());
        return screen;
    }
}
