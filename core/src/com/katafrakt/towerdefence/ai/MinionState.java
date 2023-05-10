package com.katafrakt.towerdefence.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.ai.steer.TryToStop;
import com.katafrakt.towerdefence.ai.steer.VectorFollowPath;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.ai.MinionAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.PlayerComponent;
import com.katafrakt.towerdefence.screens.GameManager;

public enum MinionState implements State<MinionAiComponent> {
    IDLE() {
        @Override
        public void enter(MinionAiComponent entity) {

        }

        @Override
        public void update(MinionAiComponent entity) {
            super.update(entity);
        }

        @Override
        public void exit(MinionAiComponent entity) {

        }
    },
    WALK() {
        @Override
        public void enter(MinionAiComponent aiComponent) {
            SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(aiComponent.entity);
            //RaycastObstacleAvoidance<Vector2> obstacleAvoidance =
            //    new RaycastObstacleAvoidance<Vector2>(steeringComponent,
            //    new CentralRayWithWhiskersConfiguration<>(steeringComponent, 8, 4, MathUtils.HALF_PI / 2),
            //    GameManager.getInstance().getMap().allyGridGraph.raycastCollisionDetector);
            //Arrive<Vector2> arrive = new Arrive<>(steeringComponent, entity.getTargetLocation());
            //arrive.setArrivalTolerance(0.1f).setDecelerationRadius(2);
            //steeringComponent.behavior = new PrioritySteering<>(steeringComponent).add(obstacleAvoidance).add(arrive);
            //steeringComponent.unUsedBehaviors.add(arrive);
            ;
            aiComponent.path = GameManager.getInstance().getMap().allyGridGraph.getSmoothPath(aiComponent.transformComponent, GameManager.getInstance().getMap().allyGridGraph.setLocation(aiComponent.targetPosition.getPosition()));

            if (aiComponent.path.size > 1) {
                VectorFollowPath followPath = new VectorFollowPath(steeringComponent, aiComponent.path);
                RaycastObstacleAvoidance<Vector2> obstacleAvoidance =
                        new RaycastObstacleAvoidance<>(steeringComponent,
                                new CentralRayWithWhiskersConfiguration<>(steeringComponent, 4, 2, MathUtils.HALF_PI / 2),
                                GameManager.getInstance().getMap().allyGridGraph.raycastCollisionDetector);
                steeringComponent.behavior = new PrioritySteering<>(steeringComponent).add(obstacleAvoidance).add(followPath);
                steeringComponent.unUsedBehaviors.clear();
                steeringComponent.unUsedBehaviors.add(followPath);
            }

        }

        @Override
        public void update(MinionAiComponent aiComponent) {
            super.update(aiComponent);
            if (aiComponent.targetPosition.getPosition().dst2(aiComponent.transformComponent) < 1) {
                aiComponent.stateMachine.changeState(IDLE);
            }
        }

        @Override
        public void exit(MinionAiComponent aiComponent) {
            SteeringComponent.MAPPER.get(aiComponent.entity).behavior = new TryToStop(steeringComponent);
        }
    };
    private static final String TAG = MinionState.class.getSimpleName();
    protected PlayerComponent playerComponent;
    protected TransformComponent transformComponent;
    protected SteeringComponent steeringComponent;

    @Override
    public void update(MinionAiComponent aiComponent) {
        playerComponent = PlayerComponent.MAPPER.get(aiComponent.entity);
        transformComponent = TransformComponent.MAPPER.get(aiComponent.entity);
        steeringComponent = SteeringComponent.MAPPER.get(aiComponent.entity);
    }

    @Override
    public boolean onMessage(MinionAiComponent entity, Telegram telegram) {
        if (telegram.message == Message.SET_WALK_TARGET.index) {
            entity.stateMachine.changeState(WALK);
        }
        return false;
    }

    public enum Message {
        SET_WALK_TARGET(1000);
        public final int index;

        Message(int index) {
            this.index = index;
        }
    }
}
