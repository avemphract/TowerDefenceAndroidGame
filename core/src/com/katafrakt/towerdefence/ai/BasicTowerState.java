package com.katafrakt.towerdefence.ai;

import com.badlogic.ashley.core.Entity;
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

        @Override
        public boolean onMessage(TowerAiComponent entity, Telegram telegram) {
            return false;
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
            if (targetTransform.dst2(transformComponent) > towerComponent.weapon.getRange() * towerComponent.weapon.getRange()) {
                towerAiComponent.setTarget(null);
                towerAiComponent.stateMachine.changeState(BasicTowerState.IDLING);
                return;
            }
            if (towerComponent.weapon.remainPercent() > 1) {
                towerComponent.weapon.attack(towerAiComponent.getTarget());
            }

        }

        @Override
        public void exit(TowerAiComponent entity) {

        }

        @Override
        public boolean onMessage(TowerAiComponent entity, Telegram telegram) {
            return false;
        }
    };
    protected static final String TAG = BasicTowerState.class.getSimpleName();
    protected TowerAiComponent towerAiComponent;
    protected TowerComponent towerComponent;
    protected TransformComponent transformComponent;

    @Override
    public void update(TowerAiComponent towerAiComponent) {
        this.towerAiComponent = towerAiComponent;
        towerComponent = TowerComponent.MAPPER.get(towerAiComponent.entity);
        transformComponent = TransformComponent.MAPPER.get(towerAiComponent.entity);
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
}
