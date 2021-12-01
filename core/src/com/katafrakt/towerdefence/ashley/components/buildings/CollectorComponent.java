package com.katafrakt.towerdefence.ashley.components.buildings;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugRender;

public class CollectorComponent implements Component, DebugRender {
    public static final ComponentMapper<CollectorComponent> MAPPER = ComponentMapper.getFor(CollectorComponent.class);
    public int range;
    public float efficiency;

    public CollectorComponent init(int range, float efficiency) {
        this.range = range;
        this.efficiency = efficiency;
        return this;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        TransformComponent transform = TransformComponent.MAPPER.get(entity);
        for (Node node : GameManager.getInstance().getMap().viewedConnectedNode(transform, range, Node.Type.OBSTACLE_TILE, Node.Type.ENEMY_PATH)) {
            shapeRenderer.circle(node.x, node.y, Node.LENGTH / 2);
        }
    }
}
