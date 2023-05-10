package com.katafrakt.towerdefence.ashley.components.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ai.WalkableMachine;
import com.katafrakt.towerdefence.ai.PlayerState;
import com.katafrakt.towerdefence.ai.formations.SquareFormation;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.utility.DebugRender;
import com.katafrakt.towerdefence.utility.LocationVector;

public class PlayerAiComponent extends AiComponent<PlayerState> implements HaveTarget, DebugRender {
    public static ComponentMapper<PlayerAiComponent> MAPPER = ComponentMapper.getFor(PlayerAiComponent.class);
    private static final String TAG = PlayerAiComponent.class.getSimpleName();

    public Array<Vector2> path;
    public Formation<Vector2> formation;

    public LocationVector walkTarget;
    public Entity target;
    private final Listener<Entity> entityListener = (signal, object) -> target = null;


    @Override
    public PlayerAiComponent init(Entity entity) {
        super.init(entity);
        this.entity = entity;
        walkTarget = new LocationVector(TransformComponent.MAPPER.get(entity), TransformComponent.MAPPER.get(entity).orientation);
        formation = new Formation<>(walkTarget, new SquareFormation(5));
        transformComponent = TransformComponent.MAPPER.get(entity);
        stateMachine = new WalkableMachine<>(this, PlayerState.IDLE);
        return this;
    }

    @Override
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
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
    public boolean handleMessage(Telegram msg) {
        Gdx.app.log(TAG, "Player input event");
        return stateMachine.handleMessage(msg);
    }

    @Override
    public void reset() {

    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        shapeRenderer.setColor(Color.RED);
        for (int i = 0; i < formation.getSlotAssignmentCount(); i++) {
            Location<Vector2> location = formation.getSlotAssignmentAt(i).member.getTargetLocation();
            shapeRenderer.circle(location.getPosition().x, location.getPosition().y, 0.5f);
        }
    }
}
