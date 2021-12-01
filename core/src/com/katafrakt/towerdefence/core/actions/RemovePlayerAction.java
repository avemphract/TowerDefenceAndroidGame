package com.katafrakt.towerdefence.core.actions;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.input.MoveTabType;
import com.katafrakt.towerdefence.screens.GameManager;

public class RemovePlayerAction extends PlayerAction {
    private static final String TAG = RemovePlayerAction.class.getSimpleName();
    public RemovePlayerAction(Entity entity) {
        super(entity);
    }

    @Override
    public void act() {
        GameManager.getInstance().getEngine().removeEntity(entity);
        Main.getMain().getInputProcessor().setTabType(MoveTabType.MOVE_TAB_TYPE);
        Gdx.app.log(TAG,"Remved");
    }
}
