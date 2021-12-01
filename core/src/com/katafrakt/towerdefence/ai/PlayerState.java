package com.katafrakt.towerdefence.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;

public enum PlayerState implements State<PlayerAiComponent> {
    IDLE(){
        @Override
        public void enter(PlayerAiComponent entity) {

        }

        @Override
        public void update(PlayerAiComponent entity) {
        }

        @Override
        public void exit(PlayerAiComponent entity) {
        }

        @Override
        public boolean onMessage(PlayerAiComponent entity, Telegram telegram) {
            walkAPoint(entity,telegram);
            return false;
        }
    },
    WALK(){
        @Override
        public void enter(PlayerAiComponent entity) {

        }

        @Override
        public void update(PlayerAiComponent entity) {
            float delta=Gdx.graphics.getDeltaTime();

        }

        @Override
        public void exit(PlayerAiComponent entity) {

        }

        @Override
        public boolean onMessage(PlayerAiComponent entity, Telegram telegram) {
            walkAPoint(entity,telegram);
            return false;
        }
    };
    protected static final String TAG=PlayerState.class.getSimpleName();

    protected void walkAPoint(PlayerAiComponent entity, Telegram telegram){

    }
}
