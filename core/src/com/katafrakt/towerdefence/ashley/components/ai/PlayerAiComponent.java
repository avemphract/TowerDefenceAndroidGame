package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.katafrakt.towerdefence.ai.WalkableMachine;
import com.katafrakt.towerdefence.ai.PlayerState;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.entities.EntityType;
import com.katafrakt.towerdefence.pfa.Node;

public class PlayerAiComponent extends AiComponent<PlayerState> {
    public static ComponentMapper<PlayerAiComponent> MAPPER =ComponentMapper.getFor(PlayerAiComponent.class);
    private static final String TAG=PlayerAiComponent.class.getSimpleName();

    @Override
    public PlayerAiComponent init(Entity entity) {
        super.init(entity);
        this.entity=entity;
        transformComponent= TransformComponent.MAPPER.get(entity);
        stateMachine =new WalkableMachine<>(this,PlayerState.IDLE);
        MessageManager.getInstance().addListener(this,EntityType.PLAYER.ordinal());
        return this;
    }

    @Override
    public void setCurrentNode(Node currentNode) {

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        Gdx.app.log(TAG,"Player input event");
        return stateMachine.handleMessage(msg);
    }

    @Override
    public void reset() {

    }
}
