package com.katafrakt.towerdefence.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.TowerComponent;
import com.katafrakt.towerdefence.screens.GameManager;

public enum BasicTowerState implements State<TowerAiComponent> {
    UNDER_CONSTRUCTION(){
        @Override
        public void enter(TowerAiComponent entity) {

        }

        @Override
        public void exit(TowerAiComponent entity) {

        }
    },
    IDLING() {
        @Override
        public void enter(TowerAiComponent entity) {

        }

        @Override
        public void update(TowerAiComponent towerAiComponent) {
            super.update(towerAiComponent);
            scan(towerAiComponent);
            if (towerAiComponent.getTarget() != null)
                towerAiComponent.stateMachine.changeState(ATTACKING);
        }

        @Override
        public void exit(TowerAiComponent entity) {

        }

    },
    ATTACKING() {
        @Override
        public void enter(TowerAiComponent entity) {

        }

        @Override
        public void update(TowerAiComponent towerAiComponent) {
            super.update(towerAiComponent);
            if (towerAiComponent.getTarget() == null || !HealthComponent.MAPPER.get(towerAiComponent.getTarget()).isAlive()) {
                scan(towerAiComponent);
                if (towerAiComponent.getTarget() == null || !HealthComponent.MAPPER.get(towerAiComponent.getTarget()).isAlive()) {
                    towerAiComponent.stateMachine.changeState(BasicTowerState.IDLING);
                    return;
                }
            }
            TransformComponent targetTransform = TransformComponent.MAPPER.get(towerAiComponent.getTarget());
            if (targetTransform.dst2(transformComponent) > towerComponent.weapon.getRange2()) {
                towerAiComponent.setTarget(null);
                towerAiComponent.stateMachine.changeState(BasicTowerState.IDLING);
                return;
            }
            towerComponent.weapon.update();
        }

        @Override
        public void exit(TowerAiComponent entity) {
            entity.setTarget(null);
        }

    };
    protected static final String TAG = BasicTowerState.class.getSimpleName();
    protected TowerComponent towerComponent;
    protected TransformComponent transformComponent;

    @Override
    public void update(TowerAiComponent towerAiComponent) {
        towerComponent = TowerComponent.MAPPER.get(towerAiComponent.entity);
        transformComponent = TransformComponent.MAPPER.get(towerAiComponent.entity);
    }

    @Override
    public boolean onMessage(TowerAiComponent aiComponent, Telegram telegram) {
        if (telegram.message==MessageTypes.CONSTRUCT.ordinal()){
            aiComponent.stateMachine.changeState(UNDER_CONSTRUCTION);
        }
        Gdx.app.log(TAG,"Message arrive");
        return false;
    }

    public void scan(TowerAiComponent towerAiComponent) {
        Array<Entity> potentialTargets = new Array<>();
        for (Entity potentialTarget : GameManager.getInstance().getEngine().getEntitiesFor(EnemyComponent.FAMILY)) {
            if (!HealthComponent.MAPPER.get(potentialTarget).isAlive())
                continue;
            TransformComponent potentialTargetTransform = TransformComponent.MAPPER.get(potentialTarget);
            if (potentialTargetTransform.dst2(transformComponent) < towerComponent.weapon.getRange() * towerComponent.weapon.getRange()) {
                potentialTargets.add(potentialTarget);
            }
        }
        potentialTargets.sort(towerAiComponent.enemyComparator);
        if (!potentialTargets.isEmpty())
            towerAiComponent.setTarget(potentialTargets.get(0));
    }


    public enum MessageTypes{
        CONSTRUCT
    }
}
