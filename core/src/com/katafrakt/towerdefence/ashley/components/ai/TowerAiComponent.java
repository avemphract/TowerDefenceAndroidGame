package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.katafrakt.towerdefence.ai.BasicTowerState;
import com.katafrakt.towerdefence.ai.WalkableMachine;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.entities.EntityType;
import com.katafrakt.towerdefence.pfa.Node;

import java.util.Comparator;

public class TowerAiComponent extends AiComponent<BasicTowerState> {
    public static final ComponentMapper<TowerAiComponent> MAPPER = ComponentMapper.getFor(TowerAiComponent.class);
    public Comparator<Entity> enemyComparator;

    private Entity target;

    private final Listener<Entity> entityListener = (signal, object) -> target = null;

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    public TowerAiComponent init(Entity entity, Comparator<Entity> enemyComparator) {
        super.init(entity);
        this.entity = entity;
        this.enemyComparator = enemyComparator;
        transformComponent = TransformComponent.MAPPER.get(entity);
        stateMachine = new DefaultStateMachine<>(this, BasicTowerState.IDLING);
        MessageManager.getInstance().addListener(this, EntityType.PLAYER.ordinal());
        return this;
    }

    @Override
    public void setCurrentNode(Node currentNode) {

    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        if (this.target != null)
            this.target.componentRemoved.remove(entityListener);
        this.target = target;
        if (this.target != null)
            this.target.componentRemoved.add(entityListener);
    }


    @Override
    public void reset() {
        enemyComparator = null;
    }

}
