package com.katafrakt.towerdefence.ashley.components.buildings;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ai.BasicTowerState;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.ai.AiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.BeforeEngine;
import com.katafrakt.towerdefence.utility.DebugRender;

public class UnderConstructionComponent implements Component, DebugRender, Pool.Poolable {
    private static final String TAG =UnderConstructionComponent.class.getSimpleName();
    public static final ComponentMapper<UnderConstructionComponent> MAPPER = ComponentMapper.getFor(UnderConstructionComponent.class);

    public Entity entity;
    public float initialRequiredMaterial = 0;
    public float remainMaterial = 0;

    public UnderConstructionComponent init(Entity entity, float initialRequiredMaterial) {
        this.entity = entity;
        this.initialRequiredMaterial = initialRequiredMaterial;
        this.remainMaterial = initialRequiredMaterial;
        Gdx.app.log(TAG,"Component kuruldu");
        MessageManager.getInstance().dispatchMessage(0,null,TowerAiComponent.MAPPER.get(entity),BasicTowerState.MessageTypes.CONSTRUCT.ordinal(),null,false);
        return this;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        BoundComponent boundComponent = BoundComponent.MAPPER.get(entity);
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rectLine(transformComponent.x - boundComponent.x / 2, transformComponent.y + boundComponent.y / 2, transformComponent.x + boundComponent.x / 2, transformComponent.y + boundComponent.y / 2, 2);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        float width=boundComponent.x*(remainMaterial/initialRequiredMaterial);
        shapeRenderer.rectLine(transformComponent.x - boundComponent.x / 2, transformComponent.y + boundComponent.y / 2, transformComponent.x - boundComponent.x / 2 + width, transformComponent.y + boundComponent.y / 2, 2);
    }

    @Override
    public void reset() {
        TowerAiComponent.MAPPER.get(entity).stateMachine.changeState(BasicTowerState.IDLING);
    }
}
