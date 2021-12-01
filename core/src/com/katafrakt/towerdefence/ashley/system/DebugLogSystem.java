package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.TowerComponent;
import com.katafrakt.towerdefence.entities.spawner.EnemySpawner;
import com.katafrakt.towerdefence.input.InputProcessorModified;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.screens.GameManager;

public class DebugLogSystem extends IntervalIteratingSystem {
    private static final String TAG = DebugLogSystem.class.getSimpleName();
    private static final float TIME_SPAN = 4f;

    EnemySpawner enemySpawner;
    Map map;

    public DebugLogSystem() {
        super(Family.all(TowerAiComponent.class).get(), TIME_SPAN);
        enemySpawner = new EnemySpawner();
        map = GameManager.getInstance().getMap();
    }

    @Override
    protected void updateInterval() {
        super.updateInterval();
        //Gdx.app.log(TAG,"Size: "+GameManager.getInstance().getEngine().getEntities().size());
        if (InputProcessorModified.RESUME = true)
            enemySpawner.spawn(map.startNode.x, map.startNode.y);

    }

    @Override
    protected void processEntity(Entity entity) {
        TowerComponent towerComponent = TowerComponent.MAPPER.get(entity);
        TowerAiComponent towerAiComponent = TowerAiComponent.MAPPER.get(entity);
        //Main.getMain().getInputProcessor().setTabType(TabType.values()[(int) (Main.getMain().getTotalTime()) % TabType.values().length]);
        //Gdx.app.log(TAG, towerAiComponent.stateMachine.getCurrentState().name() + " Remain time: " + towerComponent.weapon.remainPercent());

    }
}
