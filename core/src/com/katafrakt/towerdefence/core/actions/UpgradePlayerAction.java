package com.katafrakt.towerdefence.core.actions;

import com.badlogic.ashley.core.Entity;
import com.katafrakt.towerdefence.ai.BasicTowerState;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.TowerComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.UnderConstructionComponent;
import com.katafrakt.towerdefence.screens.GameManager;

public class UpgradePlayerAction extends PlayerAction {
    public UpgradePlayerAction(Entity entity) {
        super(entity);
    }

    @Override
    public void act() {
        UnderConstructionComponent component = GameManager.getInstance().getEngine().createComponent(UnderConstructionComponent.class).init(entity,10);
        entity.add(component);
    }
}
