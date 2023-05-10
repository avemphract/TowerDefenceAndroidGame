package com.katafrakt.towerdefence.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.ParallelSideRayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ai.steer.NodeFollowPath;
import com.katafrakt.towerdefence.ai.steer.TryToStop;
import com.katafrakt.towerdefence.ai.steer.VectorFollowPath;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.SpawnComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.TowerComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.UnderConstructionComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.ashley.components.entities.PlayerComponent;
import com.katafrakt.towerdefence.screens.GameManager;

public enum PlayerState implements State<PlayerAiComponent> {
    IDLE() {
        @Override
        public void enter(PlayerAiComponent entity) {

        }

        @Override
        public void update(PlayerAiComponent aiComponent) {
            super.update(aiComponent);
            scan(aiComponent);
            if (aiComponent.getCurrentNode().getBuilding() != null && UnderConstructionComponent.MAPPER.has(aiComponent.getCurrentNode().getBuilding())) {
                aiComponent.stateMachine.changeState(BUILDING);
                return;
            }
            if (aiComponent.getTarget() != null) {
                aiComponent.stateMachine.changeState(ATTACKING);
                return;
            }
            if (aiComponent.lastChangedStateTime + 1 < GdxAI.getTimepiece().getTime()) {
                if (aiComponent.formation != null && SpawnComponent.MAPPER.has(aiComponent.entity)) {
                    if (SpawnComponent.MAPPER.get(aiComponent.entity).maxSpawn >= aiComponent.formation.getSlotAssignmentCount()) {
                        aiComponent.stateMachine.changeState(SPAWN);
                    }
                }
            }

        }

        @Override
        public void exit(PlayerAiComponent entity) {
        }
    },
    SPAWN() {
        @Override
        public void enter(PlayerAiComponent aiComponent) {
            SpawnComponent spawnComponent = SpawnComponent.MAPPER.get(aiComponent.entity);
            spawnComponent.lastSpawnTime = GdxAI.getTimepiece().getTime();
        }

        @Override
        public void update(PlayerAiComponent aiComponent) {
            super.update(aiComponent);
            SpawnComponent spawnComponent = SpawnComponent.MAPPER.get(aiComponent.entity);
            if (spawnComponent.lastSpawnTime + 1f / spawnComponent.spawnRate < GdxAI.getTimepiece().getTime()) {
                spawnComponent.lastSpawnTime = GdxAI.getTimepiece().getTime();
                spawnComponent.type.createEntity(aiComponent.entity, aiComponent.transformComponent.x, aiComponent.transformComponent.y);
                aiComponent.formation.updateSlots();
                MessageManager.getInstance().dispatchMessage(0,aiComponent,null,MinionState.Message.SET_WALK_TARGET.index,null,false);
                if (aiComponent.formation.getSlotAssignmentCount() >= spawnComponent.maxSpawn) {
                    aiComponent.stateMachine.changeState(IDLE);
                }
            }
        }

        @Override
        public void exit(PlayerAiComponent entity) {

        }
    },
    WALK() {
        @Override
        public void enter(PlayerAiComponent aiComponent) {
            aiComponent.path = GameManager.getInstance().getMap().allyGridGraph.getSmoothPath(aiComponent.transformComponent, aiComponent.walkTarget.getPosition());
            if (aiComponent.path.size > 1) {
                Gdx.app.log(TAG, aiComponent.path.toString());
                SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(aiComponent.entity);
                VectorFollowPath followPath = new VectorFollowPath(steeringComponent, aiComponent.path);
                RaycastObstacleAvoidance<Vector2> obstacleAvoidance =
                    new RaycastObstacleAvoidance<Vector2>(steeringComponent,
                    new CentralRayWithWhiskersConfiguration<>(steeringComponent, 8, 4, MathUtils.HALF_PI / 2),
                    GameManager.getInstance().getMap().allyGridGraph.raycastCollisionDetector);
                steeringComponent.behavior = new PrioritySteering<Vector2>(steeringComponent).add(obstacleAvoidance).add(followPath);
                steeringComponent.unUsedBehaviors.clear();
                steeringComponent.unUsedBehaviors.add(obstacleAvoidance);
                steeringComponent.unUsedBehaviors.add(followPath);
            }
        }

        @Override
        public void update(PlayerAiComponent aiComponent) {
            super.update(aiComponent);
            if (aiComponent.walkTarget.getPosition().dst2(aiComponent.transformComponent) < 1f) {
                aiComponent.stateMachine.changeState(IDLE);
            }
        }

        @Override
        public void exit(PlayerAiComponent aiComponent) {
            SteeringComponent.MAPPER.get(aiComponent.entity).behavior = new TryToStop(steeringComponent);

        }
    },
    ATTACKING() {
        @Override
        public void enter(PlayerAiComponent entity) {

        }

        @Override
        public void update(PlayerAiComponent aiComponent) {
            super.update(aiComponent);
            if (aiComponent.getCurrentNode().getBuilding() != null && UnderConstructionComponent.MAPPER.has(aiComponent.getCurrentNode().getBuilding())) {
                aiComponent.stateMachine.changeState(BUILDING);
                return;
            }
            if (aiComponent.getTarget() == null || !HealthComponent.MAPPER.get(aiComponent.getTarget()).isAlive()) {
                scan(aiComponent);
                if (aiComponent.getTarget() == null || !HealthComponent.MAPPER.get(aiComponent.getTarget()).isAlive()) {
                    aiComponent.stateMachine.changeState(IDLE);
                    return;
                }
            }
            TransformComponent targetTransform = TransformComponent.MAPPER.get(aiComponent.getTarget());
            if (targetTransform.dst2(transformComponent) > playerComponent.weapon.getRange() * playerComponent.weapon.getRange()) {
                aiComponent.setTarget(null);
                aiComponent.stateMachine.changeState(IDLE);
                return;
            }
            playerComponent.weapon.update();
        }

        @Override
        public void exit(PlayerAiComponent aiComponent) {
            aiComponent.setTarget(null);
        }
    },
    BUILDING() {
        @Override
        public void enter(PlayerAiComponent entity) {

        }

        @Override
        public void update(PlayerAiComponent aiComponent) {
            super.update(aiComponent);
            if (aiComponent.getCurrentNode().getBuilding() != null && UnderConstructionComponent.MAPPER.has(aiComponent.getCurrentNode().getBuilding())) {
                UnderConstructionComponent underConstructionComponent = UnderConstructionComponent.MAPPER.get(aiComponent.getCurrentNode().getBuilding());
                underConstructionComponent.remainMaterial -= playerComponent.tool.buildingSpeed * Gdx.graphics.getDeltaTime();
                return;
            } else {
                aiComponent.stateMachine.changeState(IDLE);
            }


        }

        @Override
        public void exit(PlayerAiComponent entity) {

        }
    };
    protected static final String TAG = PlayerState.class.getSimpleName();
    protected PlayerComponent playerComponent;
    protected TransformComponent transformComponent;
    protected SteeringComponent steeringComponent;

    @Override
    public void update(PlayerAiComponent aiComponent) {
        playerComponent = PlayerComponent.MAPPER.get(aiComponent.entity);
        transformComponent = TransformComponent.MAPPER.get(aiComponent.entity);
        steeringComponent = SteeringComponent.MAPPER.get(aiComponent.entity);
    }

    @Override
    public boolean onMessage(PlayerAiComponent aiComponent, Telegram telegram) {
        if (telegram.message==Message.SET_WALK_TARGET.index){
            aiComponent.walkTarget.getPosition().set((Vector2) telegram.extraInfo);
            float f = new Vector2().add(aiComponent.walkTarget.getPosition()).mulAdd(TransformComponent.MAPPER.get(aiComponent.entity),-1).angleRad();
            aiComponent.walkTarget.setOrientation(f);
            aiComponent.formation.updateSlots();
            aiComponent.stateMachine.changeState(PlayerState.WALK);
            MessageManager.getInstance().dispatchMessage(0,aiComponent,null,MinionState.Message.SET_WALK_TARGET.index,null,false);
        }
        return false;
    }

    public void scan(PlayerAiComponent aiComponent) {
        Array<Entity> potentialTargets = new Array<>();
        for (Entity potentialTarget : GameManager.getInstance().getEngine().getEntitiesFor(EnemyComponent.FAMILY)) {
            if (!HealthComponent.MAPPER.get(potentialTarget).isAlive())
                continue;
            TransformComponent potentialTargetTransform = TransformComponent.MAPPER.get(potentialTarget);
            if (potentialTargetTransform.dst2(transformComponent) < playerComponent.weapon.getRange2()) {
                potentialTargets.add(potentialTarget);
            }
        }
        potentialTargets.sort(playerComponent.enemyComparator);
        if (!potentialTargets.isEmpty()) {
            aiComponent.setTarget(potentialTargets.get(0));
        }
    }

    public enum Message{
        SET_WALK_TARGET(100);
        public final int index;
        Message(int index){
            this.index=index;
        }
    }
}
