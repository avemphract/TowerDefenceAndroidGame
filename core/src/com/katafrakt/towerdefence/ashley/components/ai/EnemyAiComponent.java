package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.katafrakt.towerdefence.ai.WalkableMachine;
import com.katafrakt.towerdefence.ai.EnemyState;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.entities.EntityType;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.AfterEngine;

public class EnemyAiComponent extends AiComponent<EnemyState> implements AfterEngine {
    private static final String TAG = EnemyAiComponent.class.getSimpleName();
    public static ComponentMapper<EnemyAiComponent> MAPPER = ComponentMapper.getFor(EnemyAiComponent.class);

    public DefaultGraphPath<Node> path;

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    @Override
    public EnemyAiComponent init(Entity entity) {
        super.init(entity);
        this.entity = entity;
        transformComponent = TransformComponent.MAPPER.get(entity);
        stateMachine = new WalkableMachine<>(this, EnemyState.FLOW_PATH);
        MessageManager.getInstance().addListener(this, EntityType.ENEMY.ordinal());
        currentNode = GameManager.getInstance().getMap().findNode(transformComponent);
        this.currentNode.enemyEntities.add(entity);
        return this;
    }

    @Override
    public void setCurrentNode(Node currentNode) {
        this.currentNode.enemyEntities.remove(entity);
        this.currentNode = currentNode;
        this.currentNode.enemyEntities.add(entity);
    }

    @Override
    public void reset() {
        transformComponent = null;
        stateMachine = null;
    }

    @Override
    public void afterEngine(PooledEngine engine, Entity own) {
        this.currentNode.enemyEntities.remove(entity);
        MessageManager.getInstance().removeListener(this,EntityType.ENEMY.ordinal());
    }
}
