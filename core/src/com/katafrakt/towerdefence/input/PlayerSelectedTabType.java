package com.katafrakt.towerdefence.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.katafrakt.towerdefence.ai.PlayerState;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;
import com.katafrakt.towerdefence.screens.GameManager;

public class PlayerSelectedTabType extends TabType{
    Entity player;

    public PlayerSelectedTabType(Entity player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    public void tab(int screenX, int screenY) {
        Vector3 gamePos=new Vector3();
        GameManager.getInstance().getCamera().unproject(gamePos.set(screenX,screenY,0));
        MessageManager.getInstance().dispatchMessage(0,null,PlayerAiComponent.MAPPER.get(player), PlayerState.Message.SET_WALK_TARGET.index,new Vector2(gamePos.x,gamePos.y),false);
        //PlayerAiComponent.MAPPER.get(player).setWalkTarget();

    }

    @Override
    public void drag(int screenX, int screenY) {

    }
}
