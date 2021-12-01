package com.katafrakt.towerdefence.ashley.system.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.MortarComponent;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.Overlapping;

public class MortarSystem extends IteratingSystem {
    private PooledEngine engine;
    private Map map;


    public MortarSystem() {
        super(Family.all(MortarComponent.class).get());
        this.engine = GameManager.getInstance().getEngine();
        this.map = GameManager.getInstance().getMap();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MortarComponent mortarComponent=MortarComponent.MAPPER.get(entity);
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        BoundComponent boundComponent = BoundComponent.MAPPER.get(entity);

        mortarComponent.mortarRemainTime -= deltaTime;
        if (mortarComponent.mortarRemainTime < 0){
            Node node = map.findNode(transformComponent);
            if (node != null) {
                for (Entity enemy : map.enemyInNodes(map.getAllNodeFromInsideRange(node,1))) {
                    TransformComponent enemyTransform = TransformComponent.MAPPER.get(enemy);
                    BoundComponent enemyBound = BoundComponent.MAPPER.get(enemy);
                    if (Overlapping.overlapsRectangle(
                            transformComponent, boundComponent,
                            enemyTransform, enemyBound)) {
                        HealthComponent.MAPPER.get(enemy).addCurrentHealth(-mortarComponent.attackAmount);
                        engine.removeEntity(entity);
                        return;
                    }
                }
            }
            engine.removeEntity(entity);
        }

    }
}
