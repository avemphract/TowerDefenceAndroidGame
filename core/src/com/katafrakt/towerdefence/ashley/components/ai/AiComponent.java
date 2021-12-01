package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ai.WalkableMachine;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.pfa.Node;

public abstract class AiComponent<T extends State<? extends AiComponent<T>>> implements Component, Telegraph, Pool.Poolable {
    private static final String TAG=AiComponent.class.getSimpleName();
    private static final Family FAMILY = Family.one(PlayerAiComponent.class, EnemyAiComponent.class, TowerAiComponent.class).get();
    public static AiComponent<?> getComponent(Entity entity){
        if (!FAMILY.matches(entity))
            return null;
        PlayerAiComponent playerAiComponent=PlayerAiComponent.MAPPER.get(entity);
        if (playerAiComponent!=null)
            return playerAiComponent;
        EnemyAiComponent enemyAiComponent=EnemyAiComponent.MAPPER.get(entity);
        if (enemyAiComponent!=null)
            return enemyAiComponent;
        TowerAiComponent towerAiComponent=TowerAiComponent.MAPPER.get(entity);
        if (towerAiComponent!=null)
            return towerAiComponent;
        throw new RuntimeException("Not found AiComponent");
    }

    public Entity entity;
    public TransformComponent transformComponent;


    public StateMachine<? extends AiComponent<T>,T> stateMachine;

    protected Node currentNode;

    protected AiComponent<T> init(Entity entity){
        this.entity=entity;
        return this;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public abstract void setCurrentNode(Node currentNode);
}
