package com.katafrakt.towerdefence.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.ai.AiComponent;
import com.katafrakt.towerdefence.screens.GameManager;

public class WalkableMachine<E extends AiComponent<S>, S extends State<E>> extends DefaultStateMachine<E, S> {
    private static final String TAG=WalkableMachine.class.getSimpleName();
    public float time;

    public WalkableMachine(E owner, S initialState) {
        super(owner, initialState);
        initialState.enter(owner);
    }

    @Override
    public void changeState(S newState) {
        // Keep a record of the previous state
        if (!StaticStates.isStaticState(currentState))
            previousState = currentState;

        // Call the exit method of the existing state
        if (currentState != null) currentState.exit(owner);

        // Change state to the new state
        currentState = newState;

        // Call the entry method of the new state
        if (currentState != null) currentState.enter(owner);
    }

    @Override
    public void update() {
        time += Gdx.graphics.getDeltaTime();
        if (owner.getCurrentNode() == null || !owner.getCurrentNode().isInside(owner.transformComponent)){
            owner.setCurrentNode(GameManager.getInstance().getMap().findNode(owner.transformComponent));
        }
        super.update();
    }


}
