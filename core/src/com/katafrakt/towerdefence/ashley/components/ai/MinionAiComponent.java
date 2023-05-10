package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ai.MinionState;
import com.katafrakt.towerdefence.ai.WalkableMachine;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.utility.LocationVector;

public class MinionAiComponent extends AiComponent<MinionState> implements HaveTarget, FormationMember<Vector2> {
    public static ComponentMapper<MinionAiComponent> MAPPER = ComponentMapper.getFor(MinionAiComponent.class);
    private static final String TAG = MinionAiComponent.class.getSimpleName();

    public Array<Vector2> path;
    public Entity leader;
    public Location<Vector2> targetPosition = new LocationVector();

    private Entity target;
    private final Listener<Entity> entityListener = (signal, object) -> target = null;

    public MinionAiComponent init(Entity own, Entity leader) {
        super.init(own);
        this.entity = own;
        this.leader = leader;
        PlayerAiComponent.MAPPER.get(leader).formation.addMember(this);
        transformComponent = TransformComponent.MAPPER.get(entity);
        stateMachine = new WalkableMachine<>(this, MinionState.WALK);
        MessageManager.getInstance().addListener(this, MinionState.Message.SET_WALK_TARGET.index);
        return this;
    }

    @Override
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }

    @Override
    public void reset() {
        MessageManager.getInstance().removeListener(this, MinionState.Message.SET_WALK_TARGET.index);
    }

    @Override
    public Entity getTarget() {
        return target;
    }

    @Override
    public void setTarget(Entity target) {
        if (this.target != null)
            this.target.componentRemoved.remove(entityListener);
        this.target = target;
        if (this.target != null)
            this.target.componentRemoved.add(entityListener);
    }

    @Override
    public Location<Vector2> getTargetLocation() {
        return targetPosition;
    }
}
